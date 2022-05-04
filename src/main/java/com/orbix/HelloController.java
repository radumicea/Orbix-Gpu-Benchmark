package com.orbix;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class HelloController {
    @FXML
    private ChoiceBox gpu_label;
    @FXML
    private ChoiceBox method_label;

    public void initialize() {
        gpu_label.getItems().addAll("Integrated", "Dedicated");
        method_label.getItems().addAll("Standard", "Fractals", "Mandals");
    }


}