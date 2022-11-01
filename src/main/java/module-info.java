module anhthu.tpbank {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
//    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.base;
//    requires eu.hansolo.tilesfx;

    opens anhthu.tpbank to javafx.fxml;
    exports anhthu.tpbank;
}