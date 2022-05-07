package com.orbix.gui.Controllers.Handlers;

import com.orbix.gui.Controllers.BenchmarkingMethods;
import com.orbix.testbench.AbstractTestBench;
import com.orbix.testbench.MatrixMultTestBench;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("rawtypes")
public class RunButtonHandler implements EventHandler<ActionEvent>
{
    static volatile AbstractTestBench testBench;
    static volatile boolean running;

    private final ChoiceBox GPULabel;
    private final ChoiceBox methodLabel;
    private final String logsFileName;

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
        String GPUName = (String)GPULabel.getSelectionModel()
                                         .getSelectedItem();

        BenchmarkingMethods benchMethod =
            (BenchmarkingMethods)methodLabel.getSelectionModel()
                                            .getSelectedItem();

        if (GPUName == null)
        {
            displayNotSelectedAlert(
                "Please select a GPU first.",
                "GPU not selected");
            return;
        }

        if (benchMethod == null)
        {
            displayNotSelectedAlert(
                "Please select a benchmarking method first.", "Info");
            return;
        }

        if (running)
        {
            displayRunningAlert("The benchmark is already running.");
            return;
        }

        try
        {
            switch (benchMethod)
            {
                case MatrixMultiplication:
                    testBench = new MatrixMultTestBench(logsFileName, GPUName);
                    setTestBench();
                    new Thread(testBench).start();
                    break;

                default:
                    displayNotImplementedMethod();
                    return;
            }
            
        }
        catch (Exception e)
        {
            displayBenchmarkError();
            e.printStackTrace();
        }
    }

    private static void setTestBench()
    {
        testBench.setOnRunning((r) -> { running = true; });
        testBench.setOnSucceeded((s) -> {
            displaySuccessAlert(testBench.getValue().getResult());
            running = false;
        });
        testBench.setOnCancelled((c) -> {
            displayCancelledAlert();
            running = false;
            return;
        });
        testBench.setOnFailed((f) -> {
            running = false; displayBenchmarkError();
            testBench.getException().printStackTrace();
            return;
        });
    }

    private static void displayNotSelectedAlert(String message, String title)
    {
        Alert a = new Alert(AlertType.INFORMATION,
                            message,
                            ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(null);
        a.show();
    }

    static void displayRunningAlert(String message)
    {
        Alert a = new Alert(AlertType.INFORMATION,
                            message,
                            ButtonType.OK);
        a.setTitle("Running");
        a.setHeaderText(null);
        a.show();
    }

    private static void displayNotImplementedMethod()
    {
        Alert a = new Alert(AlertType.ERROR,
                            "The selected method is not implemented!",
                            ButtonType.OK);
        a.setTitle("Not Implemented Error");
        a.setHeaderText(null);
        a.show();
    }

    private static void displaySuccessAlert(String result)
    {
        Alert a = new Alert(AlertType.INFORMATION,
                            "Benchmark finished successfully!",
                            ButtonType.OK);
        a.setTitle("Succes");
        a.setHeaderText(result);
        a.show();
    }

    private static void displayCancelledAlert()
    {
        Alert a = new Alert(AlertType.INFORMATION,
                            "Benchmark has been cancelled!",
                            ButtonType.OK);
        a.setTitle("Cancelled");
        a.setHeaderText(null);
        a.show();
    }

    private static void displayBenchmarkError()
    {
        Alert a = new Alert(AlertType.ERROR,
                            "There was an error running the benchmark. " + 
                            "Check the console for the stack trace.",
                            ButtonType.OK);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.show();
    }
}
