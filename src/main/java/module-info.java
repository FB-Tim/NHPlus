module de.hitec.nhplus {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    requires java.sql;
    requires io.github.willena.sqlitejdbc;
    requires io.github.cdimascio.dotenv.java;
    requires bcrypt;

    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens de.hitec.nhplus to javafx.fxml;
    opens de.hitec.nhplus.controller to javafx.fxml;
    opens de.hitec.nhplus.model to javafx.base;

    exports de.hitec.nhplus;
    exports de.hitec.nhplus.controller;
    exports de.hitec.nhplus.model;
    exports de.hitec.nhplus.utils;
    opens de.hitec.nhplus.utils to javafx.fxml;
}