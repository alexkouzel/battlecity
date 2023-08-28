package com.battlecity;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        URL url = getClass().getClassLoader().getResource(Resources.gameView);
        if (url != null) {
            Parent root = FXMLLoader.load(url);
            stage.setTitle("Battle city");
            stage.setScene(new Scene(root, 800, 725));
            stage.setResizable(false);
            stage.show();
            root.requestFocus();
        } else {
            System.out.println("Invalid resource path.");
        }
    }

    @Override
    public void stop() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}