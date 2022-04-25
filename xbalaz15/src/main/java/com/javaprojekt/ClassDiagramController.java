package com.javaprojekt;

import com.component.ClassComponent;
import com.component.EditClassComponent;
import com.uml.ClassDiagram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;

public class ClassDiagramController{

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private AnchorPane rootPane;

    private String Name;

    @FXML
    private void initialize() {
        ClassDiagram d = new ClassDiagram("Class Diagram");
    }


    public void InsertClass(ActionEvent event) {
        rootPane.setOnMousePressed(this::onGraphPressed);
    }

    public void onGraphPressed(MouseEvent mouseEvent){
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            rootPane.getChildren().add(createClassBox(mouseEvent));
            rootPane.setOnMousePressed(null);
        }
    }

    private Node createClassBox(MouseEvent mouseEvent){
        ClassComponent box = new ClassComponent(mouseEvent.getX(), mouseEvent.getY());

        box.setOnDragDetected(e -> onBoxDragDetected(box));
        box.setOnMouseDragged(e -> onBoxDragged(e, box));
        box.setOnKeyPressed(e -> handleKeyboard(e, box));
        box.setOnMousePressed(e -> handleMouse(e, box));

        return box;
    }

    private void onBoxDragDetected(ClassComponent box) {
        box.toFront();
    }

    private void onBoxDragged(MouseEvent e, ClassComponent box) {
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            box.setLayoutX(box.getLayoutX() + e.getX() + box.getTranslateX());
            box.setLayoutY(box.getLayoutY() + e.getY() + box.getTranslateY());
        }
    }

    public void handleMouse(MouseEvent evt, ClassComponent box){
        if(evt.getButton().equals(MouseButton.PRIMARY)){
            if(evt.getClickCount() == 2){
                Label label = new Label();
                box.setName(EditClassComponent.display(box).get(0));
                //box.setText(box.getName());
                box.setNameProperty(box.getName());
            }
        }
    }

    public void Undo(){

    }

    public void handleKeyboard(KeyEvent evt, ClassComponent box){
        KeyCode k = evt.getCode();
        switch (k) {
            case W -> moveUp(box);
            case S -> moveDown(box);
            case A -> moveLeft(box);
            case D -> moveRight(box);
            case DELETE -> delete(box);
        }
    }

    public void moveUp(ClassComponent box){
        box.setLayoutY(box.getLayoutY()-10);
    }

    public void moveDown(ClassComponent box){
        box.setLayoutY(box.getLayoutY()+10);
    }

    public void moveLeft(ClassComponent box){
        box.setLayoutX(box.getLayoutX()-10);
    }

    public void moveRight(ClassComponent box){
        box.setLayoutX(box.getLayoutX()+10);
    }

    public void delete(ClassComponent box){
        rootPane.getChildren().remove(box);
    }

    public void SwitchToSeqDiagram(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sequence-diagram.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    public void SaveJson(ActionEvent event){

    }

    public void LoadJson(ActionEvent event){

    }

}