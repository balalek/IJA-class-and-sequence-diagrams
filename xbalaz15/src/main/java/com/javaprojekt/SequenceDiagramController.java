package com.javaprojekt;

import com.seqComponent.ClassWithLine;
import com.seqComponent.Messages;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class SequenceDiagramController{

    // Attributes
    private ViewModel viewModel ;
    private List<String> ObjectNames = new LinkedList<>();
    @FXML
    private TabPane tabPane;
    private static int count = 0;
    Button firstBox;
    static Double x1;
    Double y1;
    Messages arrow;

    // Getters and setters
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @FXML
    public void SwitchToClassDiagram(ActionEvent event) throws IOException {
        viewModel.setCurrentView(ViewModel.View.A);
    }

    @FXML
    public void InsertClass(ActionEvent event){
        tabPane.getSelectionModel().getSelectedItem().getContent().setOnMousePressed(this::onTabPanePressed);
    }

    public void onTabPanePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().add(createClassBox(mouseEvent));
            //objectStack.push(rootPane.getChildren().get(tabPane.getChildren().size() - 1));
            //operationStack.push(operation.CREATE);
            tabPane.getSelectionModel().getSelectedItem().getContent().setOnMousePressed(null);
        }
    }

    public ClassWithLine createClassBox(MouseEvent mouseEvent){
        ClassWithLine box = new ClassWithLine(mouseEvent.getX(), mouseEvent.getY());
        //ListofBoxes.add(box);
        //ListofBoxNames.add(box.getName());
        //UMLClass cls = d.createClass(box.getName());
        //ObjectNames = ClassDiagramController.d.getListOfClassNames();
        //box.getClassButton().getItems().addAll(FXCollections.observableArrayList(ObjectNames));
        //box.setOnDragDetected(e -> onBoxDragDetected(e, box));
        box.getClassButton().setOnMouseDragged(e -> onBoxDragged(e, box));
        //box.setOnKeyPressed(e -> handleKeyboard(e, box));
        box.getClassButton().setOnMousePressed(e -> handleMouse(e, box.getClassButton()));
        box.getTimeLineButton().setOnMouseDragged(e -> onTimeLineDragged(e, box));
        box.getTimeLineButton().setOnKeyPressed(e -> handleKeyboardTimeLine(e, box.getTimeLineButton()));
        //box.getTimeLineButton().setOnMousePressed(e -> handleMouseArrow(e, box));
        box.setOnMouseEntered(e -> onTimeLineHover(e, box));
        //box.getClassButton().setOnMousePressed(e -> handleMouseArrow(e, box));


        return box;
    }

    public void onTimeLineHover(MouseEvent evt, ClassWithLine timeBox){
        //System.out.println("heylo");
        timeBox.getTimeLineButton().setOnMousePressed(e -> handleMouseArrow(e, timeBox, true));
        timeBox.getClassButton().setOnMousePressed(e -> handleMouseArrow(e, timeBox, false));
    }

    public void handleMouseArrow(MouseEvent evt, ClassWithLine box, Boolean isTimeBox){
        // Create arrow between two classes
        if(evt.getButton().equals(MouseButton.SECONDARY)){
            count++;
            if(count == 1){
                if(!isTimeBox) count = 0;
                // Přesné souřadnice
                Bounds boundsInScene = box.getTimeLineButton().localToScene(box.getTimeLineButton().getBoundsInLocal());
                // Odečítání z důvodu, že na scéně na levo a nahoře je menu a nepatří do kreslící plochy
                x1 = (boundsInScene.getMinX()-157 + evt.getX());
                y1 = (boundsInScene.getMinY()-28 + evt.getY());
                firstBox = box.getTimeLineButton();
            }else if(count == 2){
                if(firstBox != box.getTimeLineButton()) {
                    Bounds boundsInScene = box.getTimeLineButton().localToScene(box.getTimeLineButton().getBoundsInLocal());
                    Double x2 = (boundsInScene.getMinX()-157 + evt.getX());
                    Double y2 = (boundsInScene.getMinY()-28 + evt.getY());
                    if(isTimeBox) arrow = createMessage(firstBox, box.getTimeLineButton(), x1, y1, x2, y2);
                    else arrow = CreateMessageToClass(firstBox, box.getClassButton(), x1, y1, x2, y2);
                    //objectStack.push(rootPane.getChildren().get(rootPane.getChildren().size() - 1));
                    //operationStack.push(operation.CREATE);
                }
                count = 0;
            }
        }
    }

    private void handleKeyboardTimeLine(KeyEvent e, Button timeBox){
        KeyCode k = e.getCode();
        switch (k) {
            case W:
                expand(timeBox);
                break;
            case S:
                reduce(timeBox);
                break;

            //case DELETE -> delete(box, cls);
        }
    }
    private void expand(Button timeBox){
        timeBox.setText(timeBox.getText() + "\n");
        //timeBox.setLayoutX(timeBox.getLayoutX() + timeBox.getTranslateX());
        //timeBox.setLayoutY(timeBox.getLayoutY() + timeBox.getTranslateY());
    }
    private void reduce(Button timeBox){
        timeBox.setText("\n");
    }
    private void onTimeLineDragged(MouseEvent e, ClassWithLine box){
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            box.getTimeLineButton().setLayoutY(box.getTimeLineButton().getLayoutY() + e.getY() + box.getTimeLineButton().getTranslateY());
            //box.setY(box.getTimeLineButton().getLayoutY() + e.getY() + box.getTimeLineButton().getTranslateY());
        }
    }
    private void onBoxDragged(MouseEvent e, ClassWithLine box) {
        if(e.getButton().equals(MouseButton.MIDDLE)) {
            box.setLayoutX(box.getLayoutX() + e.getX() + box.getClassButton().getTranslateX());
            box.setLayoutY(box.getLayoutY() + e.getY() + box.getClassButton().getTranslateY());
            box.setX(box.getLayoutX() + e.getX() + box.getTranslateX());
            box.setY(box.getLayoutY() + e.getY() + box.getTranslateY());
            Bounds boundsInScene = box.getTimeLineButton().localToScene(box.getTimeLineButton().getBoundsInLocal());
            System.out.println(boundsInScene);
        }
    }

    public void handleMouse(MouseEvent evt, ComboBox box){
        if(evt.getButton().equals(MouseButton.PRIMARY)) {
            //System.out.println("si tu");
            ClassDiagramController.d.addName("Main");
            ObjectNames = ClassDiagramController.d.getListOfClassNames();
            System.out.println(ObjectNames);
            box.getItems().setAll(FXCollections.observableArrayList(ObjectNames));
        }

    }

    public Messages createMessage(Button b1, Button b2, Double x1, Double y1, Double x2, Double y2){
        //Arrow arrow = new Arrow(b1.getLayoutX(), b1.getLayoutY(), b2.getLayoutX(), b2.getLayoutY());
        Messages arrow = new Messages(x1, y1, x2, y2);
        //arrow.x1Property().bind(b1.getTimeLineButton().layoutXProperty());
        //arrow.y1Property().bind(b1.getTimeLineButton().layoutYProperty());
        //arrow.x2Property().bind(b2.getTimeLineButton().layoutXProperty());
        //arrow.y2Property().bind(b2.getTimeLineButton().layoutYProperty());
        arrow.setFrom(b1.getId());
        arrow.setTo(b2.getId());
        arrow.setOnMousePressed(e -> handleMouseMessage(e, arrow));

        //b1.edges.add(arrow);
        //b2.edges.add(arrow);
        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().addAll(arrow);
        //rootPane.getChildren().addAll(arrow);
        return arrow;
    }

    public Messages CreateMessageToClass(Button b1, ComboBox b2, Double x1, Double y1, Double x2, Double y2){
        Messages arrow = new Messages(x1, y1, x2, y2);
        arrow.setFrom(b1.getId());
        arrow.setTo(b2.getId());
        arrow.setOnMousePressed(e -> handleMouseMessage(e, arrow));

        //b1.edges.add(arrow);
        //b2.edges.add(arrow);
        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().addAll(arrow);
        //rootPane.getChildren().addAll(arrow);
        return arrow;
    }

    public void handleMouseMessage(MouseEvent e, Messages arrow){

    }

    public void SaveJson(ActionEvent event){}

    public void LoadJson(ActionEvent event){}

    public void Help(ActionEvent event){}
}