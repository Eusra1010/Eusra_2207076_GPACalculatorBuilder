package com.example.gpacalculatorbuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DatabaseViewController {

    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, Integer> colId;
    @FXML private TableColumn<Course, String> colName;
    @FXML private TableColumn<Course, Integer> colCredit;
    @FXML private TableColumn<Course, String> colTeacher1;
    @FXML private TableColumn<Course, String> colTeacher2;
    @FXML private TableColumn<Course, String> colGrade;

    private ObservableList<Course> courseList;
    private Stage stage;

    @FXML
    public void initialize() {

        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        if (colName != null) colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        if (colCredit != null) colCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
        if (colTeacher1 != null) colTeacher1.setCellValueFactory(new PropertyValueFactory<>("teacher1"));
        if (colTeacher2 != null) colTeacher2.setCellValueFactory(new PropertyValueFactory<>("teacher2"));
        if (colGrade != null) colGrade.setCellValueFactory(new PropertyValueFactory<>("grade"));

        loadCourses();
    }

    private void loadCourses() {
        courseList = FXCollections.observableArrayList(database.getAllCourses());
        courseTable.setItems(courseList);
    }

    @FXML
    private void handleUpdate() {
        Course old = courseTable.getSelectionModel().getSelectedItem();
        if (old == null) {
            showAlert("Select a course to update.");
            return;
        }

        Course edited = editCourseDialog(old);
        if (edited == null) return;
        try {
            database.updateCourse(edited);
            loadCourses();
            showAlert("Course Updated!\nDB: " + database.getDatabaseFilePath() + "\nRows: " + database.getRowCount());
        } catch (RuntimeException ex) {
            showAlert("Update failed: " + ex.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        Course c = courseTable.getSelectionModel().getSelectedItem();
        if (c == null) {
            showAlert("Select a course to delete.");
            return;
        }
        try {
            database.deleteCourse(c.getId());
            loadCourses();
            showAlert("Deleted!");
        } catch (RuntimeException ex) {
            showAlert("Delete failed: " + ex.getMessage());
        }
    }

    @FXML
    private void handleRefresh() { loadCourses(); }

    @FXML
    private void handleBackToHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpacalculatorbuilder/main.fxml"));
        Parent root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }


    private Course editCourseDialog(Course existing) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Course");

        TextField name = new TextField(existing.getName());
        TextField code = new TextField(existing.getCode());
        TextField credit = new TextField(String.valueOf(existing.getCredit()));
        TextField t1 = new TextField(existing.getTeacher1());
        TextField t2 = new TextField(existing.getTeacher2());
        ComboBox<String> grade = new ComboBox<>(FXCollections.observableArrayList(
                "A+", "A", "A-", "B+", "B", "B-", "C+", "C", "D+", "D", "F"
        ));
        grade.setValue(existing.getGrade());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("Name:"), name);
        grid.addRow(1, new Label("Code:"), code);
        grid.addRow(2, new Label("Credit:"), credit);
        grid.addRow(3, new Label("Teacher 1:"), t1);
        grid.addRow(4, new Label("Teacher 2:"), t2);
        grid.addRow(5, new Label("Grade:"), grade);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Button ok = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        ok.addEventFilter(javafx.event.ActionEvent.ACTION, evt -> {
            try {
                Integer.parseInt(credit.getText());
                if (name.getText().isEmpty() || code.getText().isEmpty() || grade.getValue() == null) {
                    throw new IllegalArgumentException("Fill name, code, grade");
                }
            } catch (Exception ex) {
                evt.consume();
                showAlert("Invalid input: " + ex.getMessage());
            }
        });

        var result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            int cr = Integer.parseInt(credit.getText());
            return new Course(existing.getId(), name.getText(), code.getText(), cr, t1.getText(), t2.getText(), grade.getValue());
        }
        return null;
    }
}
