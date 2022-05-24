package com.orbix.gui.controllers.handlers;

import com.orbix.bench.Mandelbrot64;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class FractalsButtonHandler implements EventHandler<ActionEvent> {
    @Override
    public void handle(ActionEvent event) {
        new Mandelbrot64();
    }

}
