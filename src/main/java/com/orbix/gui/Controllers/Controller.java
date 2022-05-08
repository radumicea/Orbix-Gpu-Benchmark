package com.orbix.gui.Controllers;

import static com.orbix.gui.Controllers.BenchmarkingMethods.*;

import com.orbix.gui.Controllers.Handlers.*;

import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;

import com.aparapi.device.OpenCLDevice;
import com.aparapi.device.Device.TYPE;

@SuppressWarnings("rawtypes")
public class Controller
{
    private static final String logsFileName = "logs";

    @FXML
    private ChoiceBox gpu_label;
    @FXML
    private ChoiceBox method_label;
    @FXML
    private Button run_button;
    @FXML
    private Button cancel_button;
    @FXML
    private Button history_button;

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
                            DataTransfer, MatrixMultiplication, Fractals);

        run_button.setOnAction(new RunButtonHandler(logsFileName, gpu_label, method_label));
        cancel_button.setOnAction(new CancelButtonHandler());
        history_button.setOnAction(new HistoryButtonHandler(logsFileName));
    }
}
