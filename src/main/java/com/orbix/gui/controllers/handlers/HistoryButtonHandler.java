package com.orbix.gui.controllers.handlers;

import com.orbix.gui.AlertDisplayer;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HistoryButtonHandler implements EventHandler<ActionEvent> {

  private final String logsFileName;

  public HistoryButtonHandler(String logsFileName) {
    this.logsFileName = logsFileName;
  }

  public void initStage()
  {
    try{
      Parent root = FXMLLoader.load(getClass().getResource("/com/orbix/table.fxml"));
      Stage historyStage = new Stage();
      historyStage.initStyle(StageStyle.UNDECORATED);
      // registerStage.setTitle("Application");
      historyStage.setScene(new Scene(root, 1000, 750));
      historyStage.show();
    }catch (Exception e){
      e.printStackTrace();
      e.getCause();
    }
  }
  @Override
  public void handle(ActionEvent event) {
    if (
      RunButtonHandler.testBench != null &&
      RunButtonHandler.testBench.isRunning()
    ) {
      AlertDisplayer.displayInfo(
        "Running",
        null,
        "You can't open the history while the benchmark is running."
      );
      return;
    }
    initStage();
    try {
      Desktop.getDesktop().open(new File(logsFileName + ".csv"));
    } catch (IOException e) {
      AlertDisplayer.displayError(
        "File Open Error",
        null,
        "Can not open the " +
        logsFileName +
        " file. Check the console for more information."
      );
      e.printStackTrace();
    }
  }
}
