package com.orbix.gui.Controllers.Handlers;

import java.awt.Desktop;
import java.io.File;

import com.orbix.gui.AlertDisplayer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class HistoryButtonHandler implements EventHandler<ActionEvent>
{
    private final String logsFileName;

    public HistoryButtonHandler(String logsFileName)
    {
        this.logsFileName = logsFileName;
    }

    @Override
    public void handle(ActionEvent event)
    {
        if (RunButtonHandler.running)
        {
            AlertDisplayer.displayInfo(
                "Running", null,
                "You can't open the history while the benchmark is running.");
            return;
        }

        try
        {
            Desktop.getDesktop().open(new File(logsFileName + ".csv"));
        }
        catch (Exception e)
        {
            AlertDisplayer.displayError(
                "File Open Error", null,
                "Can not open the " + logsFileName +
                            " file. Check the console for more information.");
            e.printStackTrace();
        }
    }
}
