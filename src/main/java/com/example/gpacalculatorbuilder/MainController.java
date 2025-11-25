package com.example.gpacalculatorbuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    private Stage stage;


    private void loadPage(ActionEvent event, String fxml) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/gpacalculatorbuilder/" + fxml));
        Parent root = loader.load();

        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void switchToInfoPage(ActionEvent event) throws IOException {
        loadPage(event, "Add_Course.fxml");
    }

    @FXML
    private void switchToDatabasePage(ActionEvent event) throws IOException {
        loadPage(event, "database_view.fxml");
    }
}
