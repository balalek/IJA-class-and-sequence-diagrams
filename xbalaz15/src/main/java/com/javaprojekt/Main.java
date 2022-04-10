package com.javaprojekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        Parent root = FXMLLoader.load(getClass().getResource("class-diagram.fxml"));
        Scene scene = new Scene(root);

        stage.setTitle("Class Diagram");

        stage.setFullScreen(true);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}