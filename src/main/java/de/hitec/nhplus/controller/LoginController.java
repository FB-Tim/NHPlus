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

/**
 * Controller class responsible for handling user login interactions.
 * It verifies credentials for Admins and Nurses and initializes the user session.
 */
public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private final Connection connection = ConnectionBuilder.getConnection();

    /**
     * Event handler for the login button.
     * Verifies user credentials and, if valid, logs the user in and loads the main window.
     *
     * @param event the action event triggered by the login button
     */
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


    /**
     * Attempts to authenticate a user using the provided first name and password.
     * Checks both Admin and Nurse user types.
     *
     * @param firstName       the first name (used as username)
     * @param enteredPassword the entered password
     * @return the authenticated {@link Person} if login is successful; {@code null} otherwise
     */
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

    /**
     * Verifies a plain password against a hashed password using BCrypt.
     *
     * @param plain  the plain text password entered by the user
     * @param hashed the hashed password stored in the database
     * @return {@code true} if the password matches; {@code false} otherwise
     */
    private boolean checkPassword(String plain, String hashed) {
        return BCrypt.verifyer().verify(plain.toCharArray(), hashed).verified;
    }

    /**
     * Displays an alert dialog with the specified title and message content.
     *
     * @param title   the title of the alert dialog
     * @param content the message content of the alert
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}