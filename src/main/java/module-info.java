module com.guidini.guidini_dektop {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires java.prefs;

    opens com.guidini.guidini_desktop to javafx.fxml;
    exports com.guidini.guidini_desktop;
    exports com.guidini.guidini_desktop.authentication;
    exports com.guidini.guidini_desktop.application;
    opens com.guidini.guidini_desktop.application to javafx.fxml;
    opens com.guidini.guidini_desktop.authentication to javafx.fxml;
}