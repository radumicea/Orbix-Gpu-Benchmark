package com.orbix.gui.Controllers.Handlers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class CancelButtonHandler implements EventHandler<ActionEvent>
{
    @Override
    public void handle(ActionEvent event)
    {
        if (RunButtonHandler.testBench != null && RunButtonHandler.testBench.isRunning())
        {
            RunButtonHandler.testBench.cancel(true);
        }
    }
}
