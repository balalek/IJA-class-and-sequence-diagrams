module com.javaprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.google.gson;
    requires json.simple;

    opens com.javaprojekt to javafx.fxml;
    exports com.javaprojekt;
    exports com.uml;
    exports com.component;
    opens com.component to javafx.fxml;
}