package com.example.gpacalculatorbuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import java.lang.classfile.instruction.NewMultiArrayInstruction;

public class MainController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToInfoPage(ActionEvent event) throws IOException {
        Parent root= FXMLLoader.load(getClass().getResource("/com/example/gpacalculatorbuilder/Add_Course.fxml"));
        stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
        scene= new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}