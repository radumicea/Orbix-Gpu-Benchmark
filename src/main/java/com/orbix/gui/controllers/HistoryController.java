package com.orbix.gui.controllers;

import com.mongodb.MongoException;
import com.orbix.database.DatabaseParser;
import com.orbix.logging.BenchResult;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HistoryController implements Initializable {

  @FXML
  private Button close;

  @FXML
  private TableView<BenchResult> table;

  @FXML
  private TableColumn<BenchResult, String> time;

  @FXML
  private TableColumn<BenchResult, String> user;

  @FXML
  private TableColumn<BenchResult, String> gpu;

  @FXML
  private TableColumn<BenchResult, String> bench;

  @FXML
  private TableColumn<BenchResult, Long> score;

  @FXML
  private TextField search;

  @FXML
  private Button searchButton;

  private static String searchedElement;
  private ArrayList<BenchResult> arr;
  private ObservableList<BenchResult> list;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle)
    throws MongoException {
    DatabaseParser db = new DatabaseParser();

    arr = db.parseDescending();
    db.close();

    list = FXCollections.observableArrayList(arr);

    time.setCellValueFactory(
      new PropertyValueFactory<BenchResult, String>("dateTime")
    );
    user.setCellValueFactory(
      new PropertyValueFactory<BenchResult, String>("userName")
    );
    gpu.setCellValueFactory(
      new PropertyValueFactory<BenchResult, String>("GPUName")
    );
    bench.setCellValueFactory(
      new PropertyValueFactory<BenchResult, String>("benchName")
    );
    score.setCellValueFactory(
      new PropertyValueFactory<BenchResult, Long>("score")
    );

    table.setItems(list);
  }

  public void search() {
    ArrayList<BenchResult> search = new ArrayList<BenchResult>();

    for (BenchResult res : arr) {
      if (res.searchElement(searchedElement)) search.add(res);
    }

    list = FXCollections.observableArrayList(search);
    table.setItems(list);
  }

  public void closeButtonOnAction(ActionEvent event) {
    Stage stage = (Stage) close.getScene().getWindow();
    stage.close();
  }

  public void searchButtonOnAction(ActionEvent event) throws IOException {
    //initialize2();
    searchedElement = search.getText();
    search();
    //initialize2();

  }
}
