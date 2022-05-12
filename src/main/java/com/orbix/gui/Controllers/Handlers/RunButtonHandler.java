package com.orbix.gui.Controllers.Handlers;

import com.aparapi.device.OpenCLDevice;
import com.aparapi.device.Device.TYPE;
import com.orbix.bench.DataTransferBenchmark;
import com.orbix.bench.MatrixMultiplicationBenchmark;
import com.orbix.gui.AlertDisplayer;
import com.orbix.gui.Controllers.BenchmarkingMethods;
import com.orbix.logging.BenchResult;
import com.orbix.logging.CSVLogger;
import com.orbix.logging.ConsoleLogger;
import com.orbix.logging.ILogger;
import com.orbix.testbench.TestBench;

import java.io.IOException;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;

@SuppressWarnings("rawtypes")
public class RunButtonHandler implements EventHandler<ActionEvent>
{
    static volatile Task<BenchResult> testBench;

    private final ChoiceBox GPULabel;
    private final ChoiceBox methodLabel;
    private final String logsFileName;

    private ILogger log;

    public RunButtonHandler(String logsFileName, ChoiceBox GPULabel,
                            ChoiceBox methodLabel)
    {
        this.GPULabel = GPULabel;
        this.methodLabel = methodLabel;
        this.logsFileName = logsFileName;
    }

    @Override
    public void handle(ActionEvent event)
    {
        if (testBench != null && testBench.isRunning())
        {
            AlertDisplayer.displayInfo(
                "Running",
                null,
                "The benchmark is already running.");
            return;
        }

        String GPUName = (String)GPULabel.getSelectionModel()
                                         .getSelectedItem();

        if (GPUName == null)
        {
            AlertDisplayer.displayInfo(
                "GPU Not Selected",
                null,
                "Please select a GPU first.");
            return;
        }

        BenchmarkingMethods benchMethod =
            (BenchmarkingMethods)methodLabel.getSelectionModel()
                                            .getSelectedItem();

        if (benchMethod == null)
        {
            AlertDisplayer.displayInfo(
                "Benchmark Not Selected",
                null,
                "Please select a benchmarking method first.");
            return;
        }

        OpenCLDevice GPU = OpenCLDevice.listDevices(TYPE.GPU).stream()
                                       .filter(d -> d.getName().equals(GPUName))
                                       .findFirst().get();

        switch (benchMethod)
        {
            case MatrixMultiplication:
            {
                log = getCSVLogger(logsFileName);
                testBench = new TestBench(GPU,
                    MatrixMultiplicationBenchmark.class);
                setTestBench(log);
                Thread t = new Thread(testBench);
                t.setDaemon(true);
                t.start();
                break;
            }

            case DataTransfer:
            {
                log = getCSVLogger(logsFileName);
                testBench = new TestBench(GPU,
                    DataTransferBenchmark.class);
                setTestBench(log);
                Thread t = new Thread(testBench);
                t.setDaemon(true);
                t.start();
                break;
            }

            // TODO: Add std, use enums instead of passing class
            // class array only in testbench, len == 1 if non-std
            // len == nr_benches is std
            // here: case std: log = dblogger
            //       case default: log = csvlogger
            default:
            {
                AlertDisplayer.displayError(
                    "Not Implemented Error",
                    null,
                    "The selected method is not yet implemented!");
                return;
            }
        }
    }

    private static ILogger getCSVLogger(String logsFileName)
    {
        ILogger log;

        try
        {
            log = new CSVLogger(logsFileName);
        }
        catch (IOException e)
        {
            log = new ConsoleLogger();
            AlertDisplayer.displayWarning(
                "File Open Warning",
                null,
                "Can not open the " + logsFileName +
                    " file. Will write to the console instead.");
            e.printStackTrace();
        }

        return log;
    }

    private static void setTestBench(ILogger log)
    {
        testBench.setOnSucceeded((s) -> {
            log.write(testBench.getValue());
            AlertDisplayer.displayInfo(
                "Success",
                testBench.getValue().getResult(),
                "Benchmark finished successfully!");
            log.close();
        });
    }
}
