module com.orbix {
  requires transitive javafx.controls;
  requires javafx.fxml;
  requires javafx.base;
  requires java.desktop;
  requires aparapi;
  requires org.mongodb.driver.sync.client;
  requires org.mongodb.bson;
  requires org.mongodb.driver.core;

  opens com.orbix.gui to javafx.graphics;
  opens com.orbix.gui.controllers to javafx.fxml, aparapi, javafx.base;
  opens com.orbix.bench to aparapi;
  opens com.orbix.logging to com.fasterxml.jackson.databind, javafx.base;
  exports com.orbix ;
  opens com.orbix.database to aparapi, javafx.base, javafx.fxml;
}
