package de.hitec.nhplus.controller;

import de.hitec.nhplus.Main;
import de.hitec.nhplus.utils.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainWindowController {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button btnAdmins;


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

    @FXML
    private void handleShowAllPatient(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllPatientView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleShowAllTreatments(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllTreatmentView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void handleShowAllNurses(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllCaregiverView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    @FXML
    private void handleShowAllAdmins(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/AllAdminsView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


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
