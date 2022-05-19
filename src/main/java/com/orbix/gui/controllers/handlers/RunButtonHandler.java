package com.orbix.gui.controllers.handlers;

import com.aparapi.device.Device.TYPE;
import com.aparapi.device.OpenCLDevice;
import com.mongodb.MongoException;
import com.orbix.bench.BenchmarkingMethods;
import com.orbix.gui.AlertDisplayer;
import com.orbix.logging.BenchResult;
import com.orbix.logging.ConsoleLogger;
import com.orbix.logging.DatabaseLogger;
import com.orbix.logging.FileLogger;
import com.orbix.logging.ILogger;
import com.orbix.testbench.TestBench;
import java.io.IOException;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;

@SuppressWarnings("rawtypes")
public class RunButtonHandler implements EventHandler<ActionEvent> {

  static volatile Task<BenchResult> testBench;

  private final ChoiceBox GPULabel;
  private final ChoiceBox methodLabel;
  private final String logsFileName;

  private ILogger log;

  public RunButtonHandler(
    String logsFileName,
    ChoiceBox GPULabel,
    ChoiceBox methodLabel
  ) {
    this.GPULabel = GPULabel;
    this.methodLabel = methodLabel;
    this.logsFileName = logsFileName;
  }

  @Override
  public void handle(ActionEvent event) {
    if (testBench != null && testBench.isRunning()) {
      AlertDisplayer.displayInfo(
        "Running",
        null,
        "The benchmark is already running."
      );
      return;
    }

    String GPUName = (String) GPULabel.getSelectionModel().getSelectedItem();

    if (GPUName == null) {
      AlertDisplayer.displayInfo(
        "GPU Not Selected",
        null,
        "Please select a GPU first."
      );
      return;
    }

    BenchmarkingMethods benchMethod = (BenchmarkingMethods) methodLabel
      .getSelectionModel()
      .getSelectedItem();

    if (benchMethod == null) {
      AlertDisplayer.displayInfo(
        "Benchmark Not Selected",
        null,
        "Please select a benchmarking method first."
      );
      return;
    }

    OpenCLDevice GPU = OpenCLDevice
      .listDevices(TYPE.GPU)
      .stream()
      .filter(d -> d.getName().equals(GPUName))
      .findFirst()
      .get();

    if (benchMethod == BenchmarkingMethods.StandardBenchmark)
    {
      log = getDatabaseLogger();
    }
    else
    {
      log = getCSVLogger();
    }

    testBench = new TestBench(GPU, benchMethod);
    setTestBench();
    Thread t = new Thread(testBench);
    t.setDaemon(true);
    t.start();
  }

  private ILogger getDatabaseLogger(){
    ILogger log;
    try {
      log = new DatabaseLogger();
    }
    catch (MongoException mongoException)
    {
      AlertDisplayer.displayWarning(
              "File Write Warning",
              null,
              "Cannot connect to the MongoDB server. Will write to the file logger instead."
      );
      try {
        log = new FileLogger(logsFileName);
      }
      catch (IOException e)
      {
        log = new ConsoleLogger();
        AlertDisplayer.displayWarning(
                "File Open Warning",
                null,
                "Can not open the " +
                        logsFileName +
                        " file. Will write to the console instead."
        );
        e.printStackTrace();
      }
      mongoException.printStackTrace();
    }
    return log;
  }

  private ILogger getCSVLogger() {
    ILogger log;
    try {
      log = new FileLogger(logsFileName);
    } catch (IOException e) {
      log = new ConsoleLogger();
      AlertDisplayer.displayWarning(
        "File Open Warning",
        null,
        "Can not open the " +
        logsFileName +
        " file. Will write to the console instead."
      );
      e.printStackTrace();
    }
    return log;
  }

  private void setTestBench() {
    testBench.setOnSucceeded(s -> {
      log.write(testBench.getValue());

      AlertDisplayer.displayInfo(
        "Success",
        testBench.getValue().getResult(),
        "Benchmark finished successfully!"
      );
      log.close();
    });
  }
}