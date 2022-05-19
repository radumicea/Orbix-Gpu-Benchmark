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



public class HistoryController implements Initializable{

    @FXML
    private Button close;

    @FXML
    private TableView<Database> table;
    @FXML
    private TableColumn<Database,String> time;
    @FXML
    private TableColumn<Database,String> user;
    @FXML
    private TableColumn<Database,String> gpu;
    @FXML
    private TableColumn<Database,String> bench;
    @FXML
    private TableColumn<Database,Long> score;

    //BenchResult benchresult;

    /*public void writeData(BenchResult benchresult)
    {
        this.benchresult=benchresult;
    }*/

    public void initialize(URL url, ResourceBundle resourceBundle) {
        Database d1= new Database();
        //Database d2= new Database();
        d1.setName("name1");
        d1.setGpu("ssss");
        d1.setTime("123");
        d1.setBench("gpu1");
        d1.setScore(123L);

        /*d2.setName("name2");
        d2.setGpu("ssss");
        d2.setTime("123");
        d2.setBench("gpu2");
        d2.setScore(123L);*/

        //d1.setGpu("gpu1");
        ObservableList<Database> list = FXCollections.observableArrayList(d1);

        time.setCellValueFactory(new PropertyValueFactory<Database,String>("time"));
        user.setCellValueFactory(new PropertyValueFactory<Database,String>("user"));
        gpu.setCellValueFactory(new PropertyValueFactory<Database,String>("gpu"));
        bench.setCellValueFactory(new PropertyValueFactory<Database,String>("bench"));
        score.setCellValueFactory(new PropertyValueFactory<Database,Long>("score"));

        table.setItems(list);
    }

    public void closeButtonOnAction(ActionEvent event){
        Stage stage = (Stage)close.getScene().getWindow();
        stage.close();
        //Platform.exit();
    }



}

