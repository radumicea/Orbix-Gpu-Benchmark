package com.orbix.gui.Controllers.Handlers;

import com.orbix.gui.Controllers.BenchmarkingMethods;
import com.orbix.testbench.MatrixMultTestbench;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("rawtypes")
public class RunButtonHandler implements EventHandler<ActionEvent>
{
    private final ChoiceBox GPULabel;
    private final ChoiceBox methodLabel;

    public RunButtonHandler(ChoiceBox GPULabel, ChoiceBox methodLabel)
    {
        this.GPULabel = GPULabel;
        this.methodLabel = methodLabel;
    }

    @Override
    public void handle(ActionEvent event)
    {
        String GPUName = (String)GPULabel.getSelectionModel()
                                         .getSelectedItem();

        BenchmarkingMethods benchMethod = (BenchmarkingMethods)methodLabel.getSelectionModel()
                                                                          .getSelectedItem();

        if (GPUName == null)
        {
            displayNotSelectedAlert(
                "Please select a GPU first.", "GPU not selected");
            return;
        }

        if (benchMethod == null)
        {
            displayNotSelectedAlert(
                "Please select a benchmarking method first.", "Method not selected");
            return;
        }

        try
        {
            String result = null;

            switch (benchMethod)
            {
                case MatrixMultiplication:
                    result = MatrixMultTestbench.run(GPUName).getResult();
                    break;

                default:
                    displayNotImplementedMethod();
                    return;
            }

            displaySuccessAlert(result);
            
        }
        catch (Exception e)
        {
            displayBenchmarkError();
            e.printStackTrace();
        }
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

    private static void displayNotImplementedMethod()
    {
        Alert a = new Alert(AlertType.ERROR,
                            "The selected method is not implemented!",
                            ButtonType.OK);
        a.setTitle("Method not implemented");
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

    private static void displayBenchmarkError()
    {
        Alert a = new Alert(AlertType.ERROR,
                            "There was an error running the benchmark. " + 
                            "Please check the console for the stack trace.",
                            ButtonType.OK);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.show();
    }
}
