module de.hitec.nhplus {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.xerial.sqlitejdbc;
    requires bcrypt;

    requires com.fasterxml.jackson.databind;

    opens de.hitec.nhplus to javafx.fxml;
    opens de.hitec.nhplus.controller to javafx.fxml;
    opens de.hitec.nhplus.model to javafx.base;

    exports de.hitec.nhplus;
    exports de.hitec.nhplus.controller;
    exports de.hitec.nhplus.model;
    exports de.hitec.nhplus.utils;
    opens de.hitec.nhplus.utils to javafx.fxml;
}