package com.orbix.gui.controllers.handlers;

import com.orbix.database.HistoryTypes;
import com.orbix.gui.AlertDisplayer;
import java.awt.Desktop;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class HistoryButtonHandler implements EventHandler<ActionEvent> {

  private final String logsFileName;
  private final ChoiceBox historyType;

  public HistoryButtonHandler(String logsFileName, ChoiceBox historyType) {
    this.logsFileName = logsFileName;
    this.historyType = historyType;
  }

  public void initStage()
  {
    try{
      Parent root = FXMLLoader.load(getClass().getResource("/com/orbix/table.fxml"));
      Stage historyStage = new Stage();
      historyStage.initStyle(StageStyle.UNDECORATED);
      // registerStage.setTitle("Application");
      historyStage.setScene(new Scene(root, 800, 600));
      historyStage.initModality(Modality.APPLICATION_MODAL);
      historyStage.showAndWait();
    }
    catch (Exception e){
      AlertDisplayer.displayError(
              "Database Connection Error",
              null,
              "Could not connect to the database! Check the console for more information and your internet connection."
      );
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
    HistoryTypes typeName = (HistoryTypes) historyType .getSelectionModel().getSelectedItem();

    if (typeName == null)
    {
      AlertDisplayer.displayWarning(
              "History Type Not Selected",
              null,
              "Please select a history type first."
      );
      return;
    }

    if (typeName == HistoryTypes.GlobalHistory) {
      initStage();
    }
    else
    {
      try
      {
        Desktop.getDesktop().open(new File(logsFileName + ".csv"));
      }
      catch (Exception e)
      {
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
}
