package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;


/**
 * Controller class for the main application window.
 * Handles navigation between different views and manages UI elements based on user roles.
 */
public class MainWindowController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button btnAdmins;


    /**
     * Initializes the main window view.
     * Shows or hides the Admin button depending on whether the current user is an admin.
     */
    @FXML
    private void initialize() {
        if (SessionManager.getCurrentUser().isAdmin()) {
            System.out.println("Admin, verstecke Button nicht.");
            btnAdmins.setVisible(true);
            btnAdmins.setManaged(true);
        }
        if (!SessionManager.getCurrentUser().isAdmin()) {
            System.out.println("Kein Admin, verstecke Button.");
            btnAdmins.setVisible(false);
            btnAdmins.setManaged(false);
        }
    }

    /**
     * Loads the AllPatientView into the center of the main window.
     *
     * @param event the action event triggered by clicking the corresponding button
     */
    @FXML
    private void handleShowAllPatient(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllPatientView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Loads the AllTreatmentView into the center of the main window.
     *
     * @param event the action event triggered by clicking the corresponding button
     */
    @FXML
    private void handleShowAllTreatments(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllTreatmentView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Loads the AllCaregiverView (nurses) into the center of the main window.
     *
     * @param event the action event triggered by clicking the corresponding button
     */
    @FXML
    private void handleShowAllNurses(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllCaregiverView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Loads the AllAdminsView into the center of the main window.
     * Only accessible to admin users.
     *
     * @param event the action event triggered by clicking the corresponding button
     */
    @FXML
    private void handleShowAllAdmins(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllAdminsView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the logout process:
     * Clears the user session, switches back to the login screen,
     * and resizes the stage accordingly.
     *
     * @param event the action event triggered by clicking the logout button
     */
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/LoginView.fxml"));
            javafx.scene.Parent root = loader.load();

            javafx.stage.Stage stage = (javafx.stage.Stage) mainBorderPane.getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.sizeToScene();
            SessionManager.clearSession();

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
