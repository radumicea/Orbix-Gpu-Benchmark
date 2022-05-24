package com.orbix.gui.controllers;

import com.mongodb.MongoException;
import com.orbix.database.Database;
import com.orbix.database.DatabaseParser;

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



public class HistoryController{

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

    @FXML
    private TextField search;
    @FXML
    private Button searchButton;

    private static String searchedElement;

    public void initialize2() throws MongoException {
        DatabaseParser db = new DatabaseParser();
        ArrayList<Database> arr;
        arr = db.parseAscending();
        db.close();
        //ArrayList<Database> arr;
        ArrayList<Database> search = new ArrayList<Database>();
        //search.add(arr.get(arr.indexOf("timea")));

        //System.out.println(searchedElement);
        for(Database datab: arr)
        {
            if(datab.getUser().equals(searchedElement)) search.add(datab);
        }
        ObservableList<Database> list;
        if(searchedElement==null)
        {
            list = FXCollections.observableArrayList(arr);
        }
        else
        {
            list = FXCollections.observableArrayList(search);
        }
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
    }

    public void searchButtonOnAction(ActionEvent event) throws IOException
    {
        initialize2();
        searchedElement=search.getText();
        initialize2();


    }



}