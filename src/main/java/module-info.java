module com.orbix {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires aparapi;
    requires java.desktop;

    opens com.orbix.gui to javafx.fxml, javafx.graphics, aparapi;
    opens com.orbix.bench to aparapi;
    exports com.orbix;
}
