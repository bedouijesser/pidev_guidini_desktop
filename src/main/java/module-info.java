module com.guidini.guidini_dektop {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.guidini.guidini_dektop to javafx.fxml;
    exports com.guidini.guidini_dektop;
}