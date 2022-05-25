package com.orbix.gui.controllers.handlers;

import com.orbix.bench.Mandelbrot64;
import com.orbix.gui.AlertDisplayer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class FractalsButtonHandler implements EventHandler<ActionEvent> {

  @Override
  public void handle(ActionEvent event) {
    if (
      RunButtonHandler.testBench != null &&
      RunButtonHandler.testBench.isRunning()
    ) {
      AlertDisplayer.displayInfo(
        "Running",
        null,
        "You can not visualize the fractals while the benchmark is running."
      );
      return;
    }
    new Mandelbrot64();
  }
}
