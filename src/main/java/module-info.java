module com.orbix {
    requires javafx.controls;
    requires javafx.fxml;
    requires aparapi;
    requires java.desktop;

    opens com.orbix to javafx.fxml;
    opens com.orbix.bench to aparapi;
    exports com.orbix;
}
