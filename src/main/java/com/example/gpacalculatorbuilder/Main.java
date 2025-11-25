package com.example.gpacalculatorbuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        database.setDatabaseFileName("gpa-app.db");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Scene scene = new Scene(loader.load(), 700, 600);

        stage.setTitle("GPA Calculator");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
