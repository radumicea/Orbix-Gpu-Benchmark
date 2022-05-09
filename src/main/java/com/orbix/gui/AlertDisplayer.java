package com.orbix.gui;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

public final class AlertDisplayer
{
    private AlertDisplayer() { }

    public static void displayInfo(String title, String header, String content)
    {
        Alert a = new Alert(AlertType.INFORMATION, content, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(header);
        a.show();
    }

    public static void displayWarning(String title, String header, String content)
    {
        Alert a = new Alert(AlertType.WARNING, content, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(header);
        a.show();
    }

    public static void displayError(String title, String header, String content)
    {
        Alert a = new Alert(AlertType.ERROR, content, ButtonType.OK);
        a.setTitle(title);
        a.setHeaderText(header);
        a.show();
    }
}
