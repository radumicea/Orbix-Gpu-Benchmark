module com.orbix {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.orbix to aparapi, javafx.fxml;
    exports com.orbix;
}