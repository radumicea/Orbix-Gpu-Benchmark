package com.orbix;

import java.util.List;
import java.util.stream.Collectors;

import com.aparapi.device.OpenCLDevice;
import com.aparapi.device.Device.TYPE;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class Controller {
    @FXML
    private ChoiceBox gpu_label;
    @FXML
    private ChoiceBox method_label;

    public void initialize() {
        List<OpenCLDevice> devices = OpenCLDevice.listDevices(TYPE.GPU);
        gpu_label.getItems().addAll(devices.stream().map(OpenCLDevice::getName).collect(Collectors.toList()));
        method_label.getItems().addAll("Standard", "Fractals", "Mandals");
    }
}
