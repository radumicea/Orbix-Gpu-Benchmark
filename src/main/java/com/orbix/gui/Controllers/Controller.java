package com.orbix.gui.Controllers;

import static com.orbix.gui.Controllers.BenchmarkingMethods.*;
import com.orbix.gui.Controllers.Handlers.RunButtonHandler;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import com.aparapi.device.OpenCLDevice;
import com.aparapi.device.Device.TYPE;

@SuppressWarnings("rawtypes")
public class Controller
{
    @FXML
    private ChoiceBox gpu_label;
    @FXML
    private ChoiceBox method_label;
    @FXML
    private Button run_button;

    @SuppressWarnings("unchecked")
    public void initialize()
    {
        gpu_label.getItems()
                 .addAll(OpenCLDevice.listDevices(TYPE.GPU)
                                     .stream()
                                     .map(OpenCLDevice::getName)
                                     .collect(Collectors.toList()));

        method_label.getItems()
                    .addAll(StandardBenchmark,
                            MatrixMultiplication, Fractals, Mandals);

        run_button.setOnAction(new RunButtonHandler(gpu_label, method_label));
    }
}
