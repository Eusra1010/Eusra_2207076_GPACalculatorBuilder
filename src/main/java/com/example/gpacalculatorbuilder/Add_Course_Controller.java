package com.example.gpacalculatorbuilder;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Add_Course_Controller {

    @FXML private TextField totalCreditInputField;
    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseCreditField;
    @FXML private TextField teacher1Field;
    @FXML private TextField teacher2Field;
    @FXML private ComboBox<String> gradeCombo;
    @FXML private Label creditStatusLabel;
    @FXML private Button calculateButton;

    private int requiredTotalCredits = 0;
    private int currentCredits = 0;

    private final List<Course> courseList = new ArrayList<>();

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private void handleAddCourse() {

        if (totalCreditInputField.getText().isEmpty()) {
            showAlert("Enter total required credits first");
            return;
        }

        try {
            requiredTotalCredits = Integer.parseInt(totalCreditInputField.getText());
        } catch (NumberFormatException e) {
            showAlert("Total credit must be a number");
            return;
        }

        if (courseNameField.getText().isEmpty()
                || courseCodeField.getText().isEmpty()
                || courseCreditField.getText().isEmpty()
                || gradeCombo.getSelectionModel().isEmpty()) {
            showAlert("Fill all fields before adding");
            return;
        }

        int credit;
        try {
            credit = Integer.parseInt(courseCreditField.getText());
        } catch (NumberFormatException e) {
            showAlert("Course credit must be a number");
            return;
        }

        Course c = new Course(
                courseNameField.getText(),
                courseCodeField.getText(),
                credit,
                teacher1Field.getText(),
                teacher2Field.getText(),
                gradeCombo.getValue()
        );

        courseList.add(c);
        currentCredits += credit;

        creditStatusLabel.setText("Total Credits Entered: " + currentCredits);

        if (currentCredits == requiredTotalCredits)
            calculateButton.setDisable(false);
        else
            calculateButton.setDisable(true);

        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeCombo.getSelectionModel().clearSelection();

        showAlert("Course Added Successfully!");
    }

    @FXML
    private void handleReset() {
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeCombo.getSelectionModel().clearSelection();
        totalCreditInputField.clear();
        currentCredits = 0;
        courseList.clear();
        creditStatusLabel.setText("Total Credits Entered: 0");
        calculateButton.setDisable(true);
    }

    @FXML
    private void handleCalculateGPA(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Result.fxml"));
        Parent resultRoot = loader.load();

        Result_Controller controller = loader.getController();
        controller.receiveCourseData(courseList);

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(resultRoot);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void switchToMainPage(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("main.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
