package com.javaprojekt;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SequenceDiagramController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void SwitchToClassDiagram(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("class-diagram.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Sequence diagram");
        stage.setScene(scene);
        stage.show();

    }

    public void SwitchToNextSeqDiagram(ActionEvent event) throws IOException {

    }

    public void SaveJson(ActionEvent event){}

    public void LoadJson(ActionEvent event){}

}
