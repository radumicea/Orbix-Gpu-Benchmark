module com.orbix {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires aparapi;
    requires java.desktop;

    opens com.orbix.gui to javafx.graphics;
    opens com.orbix.gui.Controllers to javafx.fxml, aparapi;
    opens com.orbix.bench to aparapi;
    exports com.orbix;
}
