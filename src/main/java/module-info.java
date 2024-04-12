module org.tfg_apb.tfg_apb_cliente {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.json;
    requires java.sql;

    opens org.tfg_apb.tfg_apb_cliente to javafx.fxml;
    exports org.tfg_apb.tfg_apb_cliente;
    exports Controladores;
    opens Controladores to javafx.fxml;
}