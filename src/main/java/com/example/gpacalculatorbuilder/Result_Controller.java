package com.example.gpacalculatorbuilder;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Result_Controller {

    @FXML private TableView<Course> resultTable;
    @FXML private TableColumn<Course, String> nameCol;
    @FXML private TableColumn<Course, String> codeCol;
    @FXML private TableColumn<Course, Integer> creditCol;
    @FXML private TableColumn<Course, String> t1Col;
    @FXML private TableColumn<Course, String> t2Col;
    @FXML private TableColumn<Course, String> gradeCol;
    @FXML private Label gpaLabel;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private List<Course> courseList = new ArrayList<>();

    public void initialize() {
        nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
        codeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCode()));
        creditCol.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getCredit()).asObject());
        t1Col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeacher1()));
        t2Col.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTeacher2()));
        gradeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGrade()));
    }

    public void receiveCourseData(List<Course> list) {
        courseList.clear();
        courseList.addAll(list);
        resultTable.setItems(FXCollections.observableArrayList(list));
        gpaLabel.setText("Your GPA: " + String.format("%.2f", calculateGPA(list)));
    }

    private double calculateGPA(List<Course> list) {
        double totalPoints = 0;
        double totalCredits = 0;
        for (Course c : list) {
            totalCredits += c.getCredit();
            totalPoints += c.getCredit() * gradeToPoint(c.getGrade());
        }
        return totalPoints / totalCredits;
    }

    private double gradeToPoint(String grade) {
        switch (grade) {
            case "A+": return 4.0;
            case "A": return 3.75;
            case "A-": return 3.5;
            case "B+": return 3.25;
            case "B": return 3.0;
            case "B-": return 2.75;
            case "C+": return 2.5;
            case "C": return 2.25;
            case "D+": return 2.0;
            case "D": return 1.75;
            default: return 0.0;
        }
    }

    @FXML
    private void handleBackToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleRecalculate(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Add_Course.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void handleClear() {
        resultTable.getItems().clear();
        gpaLabel.setText("Your GPA:");
        courseList.clear();
    }
}
