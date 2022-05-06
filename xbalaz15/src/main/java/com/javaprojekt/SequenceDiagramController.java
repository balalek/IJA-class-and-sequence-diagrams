package com.javaprojekt;

import com.component.EditClassComponent;
import com.seqComponent.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class SequenceDiagramController{

    // Attributes
    private ViewModel viewModel ;
    private String arrowType;
    private List<String> ObjectNames = new LinkedList<>();
    private List<String> content = new LinkedList<>();
    @FXML
    private TabPane tabPane;
    private static int count = 0;
    Button firstBox;
    Line firstLine;
    static Double x1;
    Double y1;
    Messages arrow;

    // Getters and setters
    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    /**
     * Tlačítko, které vás po kliknutí přesměruje na class diagram
     * @param event zmáčknutí tlačítka
     * @throws IOException
     */
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
        //box.setOnKeyPressed(e -> handleKeyboard(e, box));
        //box.getClassButton().setOnMousePressed(e -> handleMouse(e, box.getClassButton()));
        //box.getTimeLineButton().setOnMousePressed(e -> handleMouseArrow(e, box));
        //box.getClassButton().setOnMousePressed(e -> handleMouseArrow(e, box));
        box.getClassButton().setOnMouseDragged(e -> onBoxDragged(e, box));
        box.getTimeLineButton().setOnMouseDragged(e -> onCallBoxDragged(e, box));
        box.getTimeLineButton().setOnKeyPressed(e -> handleKeyboardTimeLine(e, box.getTimeLineButton()));
        box.setOnMouseEntered(e -> onTimeLineHover(e, box));
        return box;
    }

    public void onTimeLineHover(MouseEvent evt, ClassWithLine timeBox){
        timeBox.getTimeLineButton().setOnMousePressed(e -> handleMouseArrow(e, timeBox, true, false));
        timeBox.getClassButton().setOnMousePressed(e -> handleMouseArrow(e, timeBox, false, false));
        timeBox.getLine().setOnMousePressed(e -> handleMouseArrow(e, timeBox, false, true));
    }

    public void handleMouseArrow(MouseEvent evt, ClassWithLine box, Boolean isTimeBox, Boolean isTimeLine){
        // Kliknutí na objektový box
        if (evt.getButton().equals(MouseButton.PRIMARY)) {
            if(evt.getClickCount() == 2) {
                if (!isTimeBox && !isTimeLine) {
                    // Udržování aktuálních dat
                    content.addAll(EditObjectName.displayObject(box));
                    box.setNameObject(content.get(0));
                    box.setNameClass(content.get(1));
                    content.removeAll(content);
                    box.setNameAndObjectProperty(box.getNameObject() + ":" + box.getNameClass());

                    ClassDiagramController.d.addName("Main");
                    ObjectNames = ClassDiagramController.d.getListOfClassNames();
                    System.out.println(ObjectNames);
                    //box.getClassButton().getItems().setAll(FXCollections.observableArrayList(ObjectNames));
                }
            }
        }

        // Vytvoření zprávy mezi objekty, čárami a aktivačními boxy
        if(evt.getButton().equals(MouseButton.SECONDARY)){
            count++;
            if(count == 1){
                // První může být časová osa, nebo call-box
                if(!isTimeBox && !isTimeLine) count = 0;
                if(isTimeBox) {
                    // Přesné souřadnice
                    Bounds boundsInScene = box.getTimeLineButton().localToScene(box.getTimeLineButton().getBoundsInLocal());
                    // Odečítání z důvodu, že na scéně na levo a nahoře je menu a nepatří do kreslící plochy
                    x1 = (boundsInScene.getMinX() - 157 + evt.getX());
                    y1 = (boundsInScene.getMinY() - 28 + evt.getY());
                    firstBox = box.getTimeLineButton();
                    firstLine = null;
                // První je časová čára
                } else if(isTimeLine){
                    Bounds boundsInScene = box.getLine().localToScene(box.getLine().getBoundsInLocal());
                    x1 = (boundsInScene.getMinX() - 157);
                    firstLine = box.getLine();
                    firstBox = null;
                }
            }else if(count == 2){
                // Když první byl aktivační box, můžes spojit s čímkoli
                if(firstBox != box.getTimeLineButton() && firstBox != null) {
                    if(isTimeBox) {
                        Bounds boundsInScene = box.getTimeLineButton().localToScene(box.getTimeLineButton().getBoundsInLocal());
                        Double x2 = (boundsInScene.getMinX() - 157 + evt.getX());
                        arrow = createMessage(firstBox, box.getTimeLineButton(), x1, y1, x2);
                    } else if(isTimeLine) {
                        Bounds boundsInScene = box.getLine().localToScene(box.getLine().getBoundsInLocal());
                        Double x2 = (boundsInScene.getMinX() - 157);
                        arrow = createMessageToLine(firstBox, box.getLine(), x1, y1, x2);
                    }
                    else {
                        Bounds boundsInScene = box.getClassButton().localToScene(box.getClassButton().getBoundsInLocal());
                        Double x2 = (boundsInScene.getMinX() - 157 + evt.getX());
                        arrow = CreateMessageToObject(firstBox, box.getClassButton(), x1, y1, x2);
                    }

                    //objectStack.push(rootPane.getChildren().get(rootPane.getChildren().size() - 1));
                    //operationStack.push(operation.CREATE);

                // Z čáry na call-box
                } else if(firstLine != box.getLine() && firstLine != null){
                    if(isTimeBox) {
                        Bounds boundsInScene = box.getTimeLineButton().localToScene(box.getTimeLineButton().getBoundsInLocal());
                        Double x2 = (boundsInScene.getMinX() - 157 + evt.getX());
                        Double y2 = (boundsInScene.getMinY() - 28 + evt.getY());
                        arrow = createMessageWithLineFirst(firstLine, box.getTimeLineButton(), x1, y2, x2);
                    }
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
    }
    private void reduce(Button timeBox){
        timeBox.setText("\n");
    }
    private void onCallBoxDragged(MouseEvent e, ClassWithLine box){
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            box.getTimeLineButton().setLayoutY(box.getTimeLineButton().getLayoutY() + e.getY() + box.getTimeLineButton().getTranslateY());
            //box.setY(box.getTimeLineButton().getLayoutY() + e.getY() + box.getTimeLineButton().getTranslateY());
        }
    }
    private void onMessageDragged(MouseEvent e, Button box){
        if(e.getButton().equals(MouseButton.MIDDLE)) {
            box.setLayoutX(box.getLayoutX() + e.getX() + box.getTranslateX());
        }
    }
    private void onBoxDragged(MouseEvent e, ClassWithLine box) {
        if(e.getButton().equals(MouseButton.MIDDLE)) {
            //box.Inconsistencies();
            box.setLayoutX(box.getLayoutX() + e.getX() + box.getClassButton().getTranslateX());
            box.setLayoutY(box.getLayoutY() + e.getY() + box.getClassButton().getTranslateY());
            box.setX(box.getLayoutX() + e.getX() + box.getTranslateX());
            box.setY(box.getLayoutY() + e.getY() + box.getTranslateY());
            /*Bounds boundsInScene = box.getTimeLineButton().localToScene(box.getTimeLineButton().getBoundsInLocal());
            System.out.println(boundsInScene);*/
        }
    }

    public void onReturnMessageBoxPressed(MouseEvent e, Button msgBox, Messages arrow){
        if(e.getButton().equals(MouseButton.PRIMARY)){
            if(e.getClickCount() == 2) {
                String msg = EditMessages.displayReturn(arrow);
                arrow.setReturnMessage(msg);
                arrow.setReturnProperty(arrow.getReturnMessage());
            }
        }
    }

    public void onAsynOrSynMessageBoxPressed(MouseEvent e, Button msgBox, Messages arrow){
        if(e.getButton().equals(MouseButton.PRIMARY)){
            if(e.getClickCount() == 2) {
                String msg = EditMessages.displayAsynchOrSynch(arrow);
                arrow.setAsOrSynMessage(msg);
                arrow.setAsOrSynProperty(arrow.getAsOrSynMessage());
            }
        }
    }

    public void onCreateMessageBoxPressed(MouseEvent e, Button msgBox, Messages arrow){
        if(e.getButton().equals(MouseButton.PRIMARY)){
            if(e.getClickCount() == 2) {
                String msg = EditMessages.displayCreate(arrow);
                arrow.setCreateMessage(msg);
                arrow.setCreateProperty(arrow.getCreateMessage());
            }
        }
    }

    public Messages createMessage(Button b1, Button b2, Double x1, Double y1, Double x2){
        Messages arrow = new Messages(x1, y1, x2);
        arrow.setFrom(b1.getId());
        arrow.setTo(b2.getId());
        arrow.setOnMousePressed(e -> handleMouseMessage(e, arrow));
        arrow.getReturnButton().setOnMouseDragged(e -> onMessageDragged(e, arrow.getReturnButton()));
        arrow.getAsynAndSynClassButton().setOnMouseDragged(e -> onMessageDragged(e, arrow.getAsynAndSynClassButton()));
        arrow.getReturnButton().setOnMousePressed(e -> onReturnMessageBoxPressed(e, arrow.getReturnButton(), arrow));
        arrow.getAsynAndSynClassButton().setOnMousePressed(e -> onAsynOrSynMessageBoxPressed(e, arrow.getAsynAndSynClassButton(), arrow));

        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().addAll(arrow);
        //rootPane.getChildren().addAll(arrow);
        return arrow;
    }

    public Messages createMessageWithLineFirst(Line l1, Button b2, Double x1, Double y1, Double x2){
        Messages arrow = new Messages(x1, y1, x2);
        arrow.setFrom(l1.getId());
        arrow.setTo(b2.getId());
        arrow.setOnMousePressed(e -> handleMouseMessage(e, arrow));
        arrow.getReturnButton().setOnMouseDragged((e -> onMessageDragged(e, arrow.getReturnButton())));
        arrow.getAsynAndSynClassButton().setOnMouseDragged((e -> onMessageDragged(e, arrow.getAsynAndSynClassButton())));
        arrow.getReturnButton().setOnMousePressed(e -> onReturnMessageBoxPressed(e, arrow.getReturnButton(), arrow));
        arrow.getAsynAndSynClassButton().setOnMousePressed(e -> onAsynOrSynMessageBoxPressed(e, arrow.getAsynAndSynClassButton(), arrow));

        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().addAll(arrow);
        //rootPane.getChildren().addAll(arrow);
        return arrow;
    }

    public Messages CreateMessageToObject(Button b1, Button b2, Double x1, Double y1, Double x2){
        Messages arrow = new Messages(x1, y1, x2);
        arrow.setFrom(b1.getId());
        arrow.setTo(b2.getId());
        arrow.setArrowType("Create");
        arrow.update();
        arrow.setOnMousePressed(e -> handleMouseMessageDeleteOnly(e, arrow));
        arrow.getCreateObjectButton().setOnMouseDragged((e -> onMessageDragged(e, arrow.getCreateObjectButton())));
        arrow.getCreateObjectButton().setOnMousePressed(e -> onCreateMessageBoxPressed(e, arrow.getCreateObjectButton(), arrow));
        //b2.getItems().setAll(FXCollections.observableArrayList(ObjectNames));

        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().addAll(arrow);
        //rootPane.getChildren().addAll(arrow);
        return arrow;
    }

    public Messages createMessageToLine(Button b1, Line l2, Double x1, Double y1, Double x2){
        Messages arrow = new Messages(x1, y1, x2);
        arrow.setFrom(b1.getId());
        arrow.setTo(l2.getId());
        arrow.setOnMousePressed(e -> handleMouseMessage(e, arrow));
        arrow.getReturnButton().setOnMouseDragged((e -> onMessageDragged(e, arrow.getReturnButton())));
        arrow.getAsynAndSynClassButton().setOnMouseDragged((e -> onMessageDragged(e, arrow.getAsynAndSynClassButton())));
        arrow.getReturnButton().setOnMousePressed(e -> onReturnMessageBoxPressed(e, arrow.getReturnButton(), arrow));
        arrow.getAsynAndSynClassButton().setOnMousePressed(e -> onAsynOrSynMessageBoxPressed(e, arrow.getAsynAndSynClassButton(), arrow));


        ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().addAll(arrow);
        //rootPane.getChildren().addAll(arrow);
        return arrow;
    }

    public void handleMouseMessageDeleteOnly(MouseEvent e, Messages arrow){
        if(e.getButton().equals(MouseButton.SECONDARY)) {
            //Arrow.ListOfArrows.remove(arrow);
            //objectStack.push(arrow);
            //operationStack.push(operation.REMOVE);
            ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().remove(arrow);
        }
    }
    public void handleMouseMessage(MouseEvent e, Messages arrow){
        // Odstranění šipky
        if(e.getButton().equals(MouseButton.SECONDARY)) {
            //Arrow.ListOfArrows.remove(arrow);
            //objectStack.push(arrow);
            //operationStack.push(operation.REMOVE);
            ((AnchorPane)tabPane.getSelectionModel().getSelectedItem().getContent()).getChildren().remove(arrow);
        }
        // Změna šipky (typu zprávy)
        if(e.getButton().equals(MouseButton.PRIMARY)) {
            if (e.getClickCount() == 2) {
                //objectStack.push(arrow);
                //operationStack.push(operation.CHANGE);
                //nameStack.push(arrow.getArrowType());
                arrowType = EditMessageArrows.display(arrow);
                arrow.setArrowType(arrowType);
                arrow.update();
            }
        }
    }


    public void SaveJson(ActionEvent event){}

    public void LoadJson(ActionEvent event){}

    /**
     * Po stisknutí tlačítka se otevře okno s nápovědou
     * @param event Stisknutí tlačítka
     */
    @FXML
    public void Help(ActionEvent event){
        sqShowHelp.display();
    }

}