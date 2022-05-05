package com.orbix;

import java.util.stream.Collectors;

import com.aparapi.device.OpenCLDevice;
import com.aparapi.device.Device.TYPE;
import com.orbix.testbench.ITestBench;
import com.orbix.testbench.MatrixMultTestbench;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;

@SuppressWarnings("rawtypes")
public class Controller {
    @FXML
    private ChoiceBox gpu_label;
    @FXML
    private ChoiceBox method_label;
    @FXML
    private Button run_button;

    private final ITestBench matrixMultTb = new MatrixMultTestbench();

    @SuppressWarnings("unchecked")
    public void initialize() {
        gpu_label.getItems()
                 .addAll(OpenCLDevice.listDevices(TYPE.GPU)
                                     .stream()
                                     .map(OpenCLDevice::getName)
                                     .collect(Collectors.toList()));

        method_label.getItems()
                    .addAll("Standard", "Fractals", "Mandals");

        run_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Object GPUName = gpu_label.getSelectionModel()
                                          .getSelectedItem();
                if (GPUName == null)
                {
                    Alert a = new Alert(AlertType.INFORMATION,
                                        "Please select a GPU first.",
                                        ButtonType.OK);
                    a.setTitle(null);
                    a.setHeaderText(null);
                    a.show();
                }
                else { matrixMultTb.run(GPUName); }
            };
        });
    }
}
