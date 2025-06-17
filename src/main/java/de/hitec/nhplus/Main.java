package de.hitec.nhplus;

import de.hitec.nhplus.datastorage.ConnectionBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        mainWindow();
    }

    public void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/LoginView.fxml"));
            AnchorPane pane = loader.load();

            Scene scene = new Scene(pane);
            primaryStage.setTitle("NHPlus");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            primaryStage.setOnCloseRequest(event -> {
                ConnectionBuilder.closeConnection();
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void setRoot(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/de/hitec/nhplus/" + fxmlName + ".fxml"));
            BorderPane pane = loader.load();

            primaryStage.getScene().setRoot(pane);
            primaryStage.sizeToScene();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}