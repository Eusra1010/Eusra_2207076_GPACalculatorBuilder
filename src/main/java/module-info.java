module com.example.gpacalculatorbuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.gpacalculatorbuilder to javafx.fxml;
    exports com.example.gpacalculatorbuilder;
}