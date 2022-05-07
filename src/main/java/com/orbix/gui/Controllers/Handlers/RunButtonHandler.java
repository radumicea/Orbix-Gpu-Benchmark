package com.orbix.gui.Controllers.Handlers;

import com.orbix.testbench.GPUTestBench;

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

    public RunButtonHandler(ChoiceBox GPULabel)
    {
        this.GPULabel = GPULabel;
    }

    @Override
    public void handle(ActionEvent event)
    {
        String GPUName =(String)GPULabel.getSelectionModel()
                                        .getSelectedItem();
        if (GPUName == null)
        {
            displayGPUNotSelectedAlert();
        }
        else
        {
            try
            {
                String result = GPUTestBench.runMatrixMultBench(GPUName).getResult();
                displaySuccessAlert(result);
                
            }
            catch (Exception e)
            {
                displayBenchmarkError();
                e.printStackTrace();
            }
        }
    }

    private static void displayGPUNotSelectedAlert()
    {
        Alert a = new Alert(AlertType.INFORMATION,
                            "Please select a GPU first.",
                            ButtonType.OK);
        a.setTitle("GPU not selected");
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
                            "There was an error running the benchmark. Please check the console for the stack trace.",
                            ButtonType.OK);
        a.setTitle("Error");
        a.setHeaderText(null);
        a.show();
    }
}
