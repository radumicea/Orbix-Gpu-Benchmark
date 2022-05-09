package com.orbix.gui.Controllers.Handlers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

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
            RunButtonHandler.displayRunningAlert("You can't open the history while the benchmark is running.");
            return;
        }

        try
        {
            Desktop.getDesktop().open(new File(logsFileName + ".csv"));
        }
        catch (Exception e)
        {
            displayFileOpenErrorAlert(logsFileName);
            e.printStackTrace();
        }
    }

    private static void displayFileOpenErrorAlert(String logsFileName)
    {
        Alert a = new Alert(AlertType.ERROR,
                            "Can not open the " + logsFileName +
                            " file. Check the console for more information.",
                            ButtonType.OK);
        a.setTitle("File Open Error");
        a.setHeaderText(null);
        a.show();
    }
}
