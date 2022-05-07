/**
 * @author Martin Baláž
 */
package com.javaprojekt;

import java.io.IOException;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Třída Main ve které aplikace začíná, tato třída dědí od Application
 */
public class Main extends Application {

    /**
     * Vytvoří se dvě okna, mezi kterými se dá libovolně přepínat bez ztráty dat
     * @param primaryStage Primární stage pro tuto aplikace, do které může být nastavena scéna,
     *                     aplikace mohou vytvářet i jiné stage, jestli jsou potřeba, ale nebudou primární.
     * @throws IOException
     */
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

        // Defaultní výška a šířka aplikace
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

    /**
     * Nastartování aplikace
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}