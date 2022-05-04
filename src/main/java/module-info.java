module com.orbix {
    requires javafx.controls;
    requires javafx.fxml;
    requires aparapi;
    requires java.desktop;


    opens com.orbix to javafx.fxml;
    exports com.orbix;
}