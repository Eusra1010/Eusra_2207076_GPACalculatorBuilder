module com.example.gpacalculatorbuilder {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires org.json;

    opens com.example.gpacalculatorbuilder to javafx.fxml, javafx.base;
    exports com.example.gpacalculatorbuilder;
}
