module com.example.gpacalculatorbuilder {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gpacalculatorbuilder to javafx.fxml;
    exports com.example.gpacalculatorbuilder;
}