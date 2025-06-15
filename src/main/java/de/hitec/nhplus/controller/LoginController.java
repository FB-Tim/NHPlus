package de.hitec.nhplus.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.hitec.nhplus.Main;
import de.hitec.nhplus.datastorage.ConnectionBuilder;
import de.hitec.nhplus.model.Admin;
import de.hitec.nhplus.model.Nurse;
import de.hitec.nhplus.model.Person;
import de.hitec.nhplus.datastorage.AdminDao;
import de.hitec.nhplus.datastorage.NurseDao;
import de.hitec.nhplus.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.sql.Connection;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private final Connection connection = ConnectionBuilder.getConnection();

    @FXML
    private void handleLogin(ActionEvent event) {
        String firstName = txtUsername.getText().trim();
        String enteredPassword = txtPassword.getText();

        if (firstName.isEmpty() || enteredPassword.isEmpty()) {
            showAlert("Fehler", "Bitte alle Felder ausfüllen.");
            return;
        }

        Person person = tryLogin(firstName, enteredPassword);

        if (person == null) {
            showAlert("Login fehlgeschlagen", "Benutzername oder Passwort ist falsch.");
            return;
        }

        SessionManager.setCurrentUser(person);


        Main.setRoot("MainWindowView");

    }

    private Person tryLogin(String firstName, String enteredPassword) {
        AdminDao adminDao = new AdminDao(connection);
        Admin admin = adminDao.findByFirstName(firstName);
        if (admin != null && checkPassword(enteredPassword, admin.getPassword())) {
            return admin;
        }

        NurseDao nurseDao = new NurseDao(connection);
        Nurse nurse = nurseDao.findByFirstName(firstName);
        if (nurse != null && checkPassword(enteredPassword, nurse.getPassword())) {
            return nurse;
        }

        return null;
    }

    private boolean checkPassword(String plain, String hashed) {
        return BCrypt.verifyer().verify(plain.toCharArray(), hashed).verified;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}