package com.javaprojekt;

import com.component.*;
import com.uml.ClassDiagram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.*;

enum operation{
    REMOVE,
    CREATE,
    RENAME,
    CHANGE
}

public class ClassDiagramController{

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private AnchorPane rootPane;
    private List<String> content = new LinkedList<String>();
    private Deque<Object> objectStack = new ArrayDeque<>();
    private Deque<String> nameStack = new ArrayDeque<>();
    private Deque<Enum<operation>> operationStack = new ArrayDeque<>();
    private String arrowType;
    private static int count = 0;
    ClassComponent firstBox;
    Arrow arrow;

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
            objectStack.push(rootPane.getChildren().get(rootPane.getChildren().size() - 1));
            operationStack.push(operation.CREATE);
            rootPane.setOnMousePressed(null);
        }
    }

    private ClassComponent createClassBox(MouseEvent mouseEvent){
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
                objectStack.push(box);
                operationStack.push(operation.RENAME);
                nameStack.push(box.getName());
                content.addAll(EditClassComponent.display(box));
                box.setName(content.get(0));
                box.setAttributes(content.get(1));
                box.setOperations(content.get(2));
                content.removeAll(content);
                box.setNameProperty(box.getName());
                box.setAttrProperty(box.getAttributes());
                box.setOperationProperty(box.getOperations());
            }
        }
        if(evt.getButton().equals(MouseButton.SECONDARY)){
            count++;
            if(count == 1){
                firstBox = box;
            }else if(count == 2){
                if(firstBox != box) {
                    arrow = createArrow(firstBox, box);
                    objectStack.push(rootPane.getChildren().get(rootPane.getChildren().size() - 1));
                    operationStack.push(operation.CREATE);
                }
                count = 0;
            }
        }
    }

    public Arrow createArrow(ClassComponent b1, ClassComponent b2){
        Arrow arrow = new Arrow(b1.getLayoutX(), b1.getLayoutY(), b2.getLayoutX(), b2.getLayoutY());
        arrow.x1Property().bind(b1.layoutXProperty());
        arrow.y1Property().bind(b1.layoutYProperty());
        arrow.x2Property().bind(b2.layoutXProperty());
        arrow.y2Property().bind(b2.layoutYProperty());

        arrow.setOnMousePressed(e -> handleMouseArrow(e, arrow));

        b1.edges.add(arrow);
        b2.edges.add(arrow);
        rootPane.getChildren().addAll(arrow);
        return arrow;
    }

    private void handleMouseArrow(MouseEvent e, Arrow arrow) {
        if(e.getButton().equals(MouseButton.SECONDARY)) {
            objectStack.push(arrow);
            operationStack.push(operation.REMOVE);
            rootPane.getChildren().remove(arrow);
        }
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            if (e.getClickCount() == 2) {
                objectStack.push(arrow);
                operationStack.push(operation.CHANGE);
                nameStack.push(arrow.getArrowType());
                arrowType = EditArrowComponent.display(arrow);
                arrow.setArrowType(arrowType);
                arrow.update();
            }
        }
    }

    public void Undo(){
        if(!rootPane.getChildren().isEmpty()){
            if(!objectStack.isEmpty() && !operationStack.isEmpty()) {
                if(operationStack.peekFirst() == operation.CREATE){
                    rootPane.getChildren().remove(objectStack.pop());
                    operationStack.pop();
                }else if(operationStack.peekFirst() == operation.REMOVE){
                    rootPane.getChildren().add((Node) objectStack.pop());
                    operationStack.pop();
                }else if(operationStack.peekFirst() == operation.RENAME){
                    ClassComponent box = (ClassComponent) objectStack.pop();
                    box.setName(nameStack.pop());
                    box.setNameProperty(box.getName());
                    operationStack.pop();
                } else if (operationStack.peekFirst() == operation.CHANGE) {
                    Arrow arrow = (Arrow) objectStack.pop();
                    arrow.setArrowType(nameStack.pop());
                    arrow.update();
                    operationStack.pop();
                }
                //TODO move with boxes
            }
        }
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
        objectStack.push(box);
        operationStack.push(operation.REMOVE);
        rootPane.getChildren().remove(box);
        for(Arrow arrow : box.edges) {
            objectStack.push(arrow);
            operationStack.push(operation.REMOVE);
            rootPane.getChildren().remove(arrow);
        }
    }

    public void SwitchToSeqDiagram(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("sequence-diagram.fxml"));
        root = loader.load();

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Sequence diagram");
        stage.setScene(scene);
        stage.show();

    }

    public void showHelp(ActionEvent event){
        ShowHelp.display();
    }
    public void SaveJson(ActionEvent event){

    }
    public void LoadJson(ActionEvent event){

    }

}