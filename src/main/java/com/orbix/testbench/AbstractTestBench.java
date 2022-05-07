package com.orbix.testbench;

import com.orbix.logging.BenchResult;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public abstract class AbstractTestBench extends Task<BenchResult>
{
    protected final static void displayFileOpenWarningAlert(String logsFileName)
    {
        Alert a = new Alert(AlertType.WARNING,
                            "Can not open the " + logsFileName +
                            " file. Will write to the console instead.",
                            ButtonType.OK);
        a.setTitle("File Open Warning");
        a.setHeaderText(null);
        a.show();
    }

    protected final static void displayFileWriteWarningAlert(String logsFileName)
    {
        Alert a = new Alert(AlertType.WARNING,
                            "Can not write to the " + logsFileName +
                            " file. Will write to the console instead.",
                            ButtonType.OK);
        a.setTitle("File Write Warning");
        a.setHeaderText(null);
        a.show();
    }
}
