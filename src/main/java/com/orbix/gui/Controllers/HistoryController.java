package com.orbix.gui.controllers;

import static com.orbix.bench.BenchmarkingMethods.*;

import com.aparapi.device.Device.TYPE;
import com.aparapi.device.OpenCLDevice;
import com.orbix.gui.controllers.handlers.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.orbix.logging.BenchResult;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;



public class HistoryController{

    @FXML
    private Button close;

    @FXML
    private TableView<BenchResult> table;
    @FXML
    private TableColumn<BenchResult,String> time;
    @FXML
    private TableColumn<BenchResult,String> user;
    @FXML
    private TableColumn<BenchResult,String> gpu;
    @FXML
    private TableColumn<BenchResult,String> bench;
    @FXML
    private TableColumn<BenchResult,Long> score;

    //BenchResult benchresult;

    /*public void writeData(BenchResult benchresult)
    {
        this.benchresult=benchresult;
    }*/

    //public void initialize(URL url, ResourceBundle resourceBundle) {
        //Database d1= new Database();
        //d1.setName("name1");
        //d1.setTime(12);
        //d1.setGpu("gpu1");
        /*ObservableList<BenchResult> list = FXCollections.observableArrayList(benchresult);

        time.setCellValueFactory(new PropertyValueFactory<BenchResult,String>("time"));
        user.setCellValueFactory(new PropertyValueFactory<BenchResult,String>("user"));
        gpu.setCellValueFactory(new PropertyValueFactory<BenchResult,String>("gpu"));
        bench.setCellValueFactory(new PropertyValueFactory<BenchResult,String>("bench"));
        score.setCellValueFactory(new PropertyValueFactory<BenchResult,Long>("score"));

        table.setItems(list);
    }
*/
    public void closeButtonOnAction(ActionEvent event){
        Stage stage = (Stage)close.getScene().getWindow();
        stage.close();
        //Platform.exit();
    }



}

