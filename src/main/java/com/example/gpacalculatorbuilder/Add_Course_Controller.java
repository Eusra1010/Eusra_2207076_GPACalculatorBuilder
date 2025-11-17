package com.example.gpacalculatorbuilder;

import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseCreditField;
    @FXML private TextField teacher1Field;
    @FXML private TextField teacher2Field;
    @FXML private ComboBox<String> gradeCombo;

    @FXML private TextField totalCreditInputField;

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
            showAlert("Missing Total Credit", "Please enter the total required credits first.");
            return;
        }

        try {
            requiredTotalCredits = Integer.parseInt(totalCreditInputField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Invalid Number", "Total credit must be an integer.");
            return;
        }

        if (courseNameField.getText().isEmpty()
                || courseCodeField.getText().isEmpty()
                || courseCreditField.getText().isEmpty()
                || gradeCombo.getValue() == null) {

            showAlert("Missing Data", "Please fill all required fields.");
            return;
        }

        int credit;

        try {
            credit = Integer.parseInt(courseCreditField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert("Error", "Course credit must be a number.");
            return;
        }

        Course course = new Course(
                courseNameField.getText(),
                courseCodeField.getText(),
                credit,
                teacher1Field.getText(),
                teacher2Field.getText(),
                gradeCombo.getValue()
        );

        courseList.add(course);

        currentCredits += credit;

        creditStatusLabel.setText("Total Credits Entered: " + currentCredits + " / " + requiredTotalCredits);

        calculateButton.setDisable(currentCredits != requiredTotalCredits);

        showAlert("Success", "Course added successfully!");

        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeCombo.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleCalculateGPA(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Result.fxml"));
            Parent root = loader.load();

            Result_Controller controller = loader.getController();
            controller.receiveCourseData(courseList);

            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToMainPage(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("main.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "Unable to load main page.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
