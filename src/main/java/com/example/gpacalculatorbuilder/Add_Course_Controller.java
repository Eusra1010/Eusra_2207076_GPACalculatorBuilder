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

public class Add_Course_Controller {

    // FXML fields for course input
    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseCreditField;
    @FXML private TextField teacher1Field;
    @FXML private TextField teacher2Field;
    @FXML private ComboBox<String> gradeCombo;

    // NEW → user enters total required credits
    @FXML private TextField totalCreditInputField;

    @FXML private Label creditStatusLabel;
    @FXML private Button calculateButton;

    private int requiredTotalCredits = 0;   // user enters this
    private int currentCredits = 0;

    private Stage stage;
    private Scene scene;
    private Parent root;

    // ------------------------------------
    // ADD COURSE LOGIC
    // ------------------------------------
    @FXML
    private void handleAddCourse() {

        // Check if total credit input is empty
        if (totalCreditInputField.getText().isEmpty()) {
            showAlert("Missing Total Credit",
                    "Please enter the total required credits first.");
            return;
        }

        try {
            requiredTotalCredits = Integer.parseInt(totalCreditInputField.getText());
        } catch (NumberFormatException e) {
            showAlert("Invalid Number", "Total credit must be an integer value.");
            return;
        }

        if (courseCreditField.getText().isEmpty()) {
            showAlert("Error", "Please enter course credit.");
            return;
        }

        int credit;

        try {
            credit = Integer.parseInt(courseCreditField.getText());
        } catch (NumberFormatException e) {
            showAlert("Error", "Course credit must be a number.");
            return;
        }

        currentCredits += credit;

        creditStatusLabel.setText("Total Credits Entered: " + currentCredits +
                " / " + requiredTotalCredits);

        // Enable GPA button only when entered credits match
        if (currentCredits == requiredTotalCredits) {
            calculateButton.setDisable(false);
        } else {
            calculateButton.setDisable(true);
        }

        // Clear fields after adding
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeCombo.getSelectionModel().clearSelection();
    }

    // ------------------------------------
    // CALCULATE GPA BUTTON PRESSED
    // ------------------------------------
    @FXML
    private void handleCalculateGPA() {
        showAlert("GPA Calculation",
                "GPA calculation logic will be implemented next!");
    }

    // ------------------------------------
    // SWITCH BACK TO MAIN SCENE
    // ------------------------------------
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

    // ------------------------------------
    // ALERT POPUP
    // ------------------------------------
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
