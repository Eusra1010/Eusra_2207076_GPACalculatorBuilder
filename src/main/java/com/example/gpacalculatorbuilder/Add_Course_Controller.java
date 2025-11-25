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

        calculateButton.setDisable(currentCredits != requiredTotalCredits);

        clearFields();

        showAlert("Course Added Successfully!");
    }

    private void clearFields() {
        courseNameField.clear();
        courseCodeField.clear();
        courseCreditField.clear();
        teacher1Field.clear();
        teacher2Field.clear();
        gradeCombo.getSelectionModel().clearSelection();
    }


    @FXML
    private void handleReset() {
        clearFields();
        totalCreditInputField.clear();
        currentCredits = 0;
        requiredTotalCredits = 0;
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
        stage.setScene(new Scene(resultRoot));
        stage.show();
    }


    @FXML
    private void handleSaveToDatabase() {
        // If there are queued courses, save them all; otherwise save current form inputs as a single course
        if (!courseList.isEmpty()) {
            try {
                for (Course c : courseList) {
                    database.insertCourse(c);
                }
                showAlert("Saved " + courseList.size() + " course(s) to database!\nFile: " + database.getDatabaseFilePath());
            } catch (RuntimeException ex) {
                showAlert("Save to DB failed: " + ex.getMessage());
            }
            return;
        }

        Course single = formCourseFromInputs();
        if (single == null) return;
        try {
            database.insertCourse(single);
            showAlert("Saved 1 course to database!\nFile: " + database.getDatabaseFilePath());
            clearFields();
        } catch (RuntimeException ex) {
            showAlert("Save to DB failed: " + ex.getMessage());
        }
    }


    private Course formCourseFromInputs() {
        if (courseNameField.getText().isEmpty()
                || courseCodeField.getText().isEmpty()
                || courseCreditField.getText().isEmpty()
                || gradeCombo.getSelectionModel().isEmpty()) {
            showAlert("Fill course name, code, credit, and grade before saving");
            return null;
        }

        int credit;
        try {
            credit = Integer.parseInt(courseCreditField.getText());
        } catch (NumberFormatException e) {
            showAlert("Course credit must be a number");
            return null;
        }

        return new Course(
                courseNameField.getText(),
                courseCodeField.getText(),
                credit,
                teacher1Field.getText(),
                teacher2Field.getText(),
                gradeCombo.getValue()
        );
    }

    @FXML
    private void handleShowDatabase(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpacalculatorbuilder/database_view.fxml"));
        Parent root = loader.load();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    private void switchToMainPage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpacalculatorbuilder/main.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText(null);
        a.setContentText(msg);
        a.showAndWait();
    }
}
