module com.orbix {
    requires javafx.controls;
    requires javafx.fxml;
    requires aparapi;
    requires java.desktop;

    opens com.orbix to javafx.fxml, aparapi;
    opens com.orbix.bench to aparapi;
    opens com.orbix.logging to aparapi;
    exports com.orbix;
}
