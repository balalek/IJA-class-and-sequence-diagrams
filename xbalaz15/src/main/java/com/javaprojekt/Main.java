package com.javaprojekt;


import java.io.IOException;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        ViewModel viewModel = new ViewModel();

        FXMLLoader aLoader = new FXMLLoader(getClass().getResource("class-diagram.fxml"));
        Parent a = aLoader.load();
        ClassDiagramController aController = aLoader.getController();
        aController.setViewModel(viewModel);

        FXMLLoader bLoader = new FXMLLoader(getClass().getResource("sequence-diagram.fxml"));
        Parent b = bLoader.load();
        SequenceDiagramController bController = bLoader.getController();
        bController.setViewModel(viewModel);

        Scene scene = new Scene(a, 1324.0, 795.0);

        scene.rootProperty().bind(Bindings.createObjectBinding(() -> {
            if (viewModel.getCurrentView() == ViewModel.View.A) {
                primaryStage.setTitle("Class Diagram");
                return a ;
            } else if (viewModel.getCurrentView() == ViewModel.View.B) {
                primaryStage.setTitle("Sequence Diagram");
                return b ;
            } else {
                return null ;
            }
        }, viewModel.currentViewProperty()));

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}