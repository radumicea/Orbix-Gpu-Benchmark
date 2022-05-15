module com.orbix {
  requires transitive javafx.controls;
  requires javafx.fxml;
  requires java.desktop;
  requires com.fasterxml.jackson.databind;
  requires aparapi;

  opens com.orbix.gui to javafx.graphics;
  opens com.orbix.gui.controllers to javafx.fxml, aparapi;
  opens com.orbix.bench to aparapi;
  opens com.orbix.logging to com.fasterxml.jackson.databind;
  exports com.orbix ;
}
