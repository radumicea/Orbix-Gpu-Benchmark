package com.orbix.gui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    FXMLLoader fxmlLoader = new FXMLLoader(
      GUI.class.getResource("/com/orbix/view.fxml")
    );
    Scene scene = new Scene(fxmlLoader.load(), 607, 342);
    stage.setResizable(false);
    stage.setTitle("ORBIX");
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch();
  }
}
