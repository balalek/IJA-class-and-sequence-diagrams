package com.seqComponent;

import com.component.Arrow;
import com.google.gson.annotations.Expose;
import com.javaprojekt.ClassDiagramController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.shape.Line;

import java.util.LinkedList;
import java.util.List;

public class ClassWithLine extends Group {

    @Expose
    public Double x;
    @Expose
    public Double y;
    @Expose
    public String nameObject = "";
    @Expose
    public String nameClass = "";
    private SimpleStringProperty nameAndObjectProperty = new SimpleStringProperty();
    private Button classButton = new Button();
    private Button timeLineButton = new Button();
    private Line line = new Line();
    private String className;
    public ObservableList<Messages> edges = FXCollections.observableArrayList();
    private static int count = 1;
    private int ID;
    public static List<ClassWithLine> ListClassWithLine = new LinkedList<>();

    // Getters and setter
    public Double getX() {
        return x;
    }
    public void setX(Double x) {
        this.x = x;
    }
    public Double getY() {
        return y;
    }
    public void setY(Double y) {
        this.y = y;
    }
    public Button getClassButton() {
        return classButton;
    }
    public void setClassButton(Button classButton) {
        this.classButton = classButton;
    }
    public Button getTimeLineButton() {
        return timeLineButton;
    }
    public void setTimeLineButton(Button timeLineButton) {
        this.timeLineButton = timeLineButton;
    }
    public Line getLine() {
        return line;
    }
    public void setLine(Line line) {
        this.line = line;
    }
    public String getNameObject() {
        return nameObject;
    }
    public void setNameObject(String nameObject) {
        this.nameObject = nameObject;
    }
    public String getNameClass() {
        return nameClass;
    }
    public void setNameClass(String nameClass) {
        this.nameClass = nameClass;
    }
    public String getNameAndObjectProperty() {
        return nameAndObjectProperty.get();
    }
    public SimpleStringProperty nameAndObjectPropertyProperty() {
        return nameAndObjectProperty;
    }
    public void setNameAndObjectProperty(String nameAndObjectProperty) {
        this.nameAndObjectProperty.set(nameAndObjectProperty);
    }

    public static List<ClassWithLine> getListClassWithLine() {
        return ListClassWithLine;
    }

    /**
     * Konstruktor
     * @param x
     * @param y
     */
    public ClassWithLine(Double x, Double y) {
        ID = count++;
        this.x = x;
        this.y = y;
        //classButton.setText(className);
        ListClassWithLine.add(this);
        classButton.setLayoutX(x);
        classButton.setLayoutY(y);

        // Při posunu, je kurzor vždy umístěn uprostřed
        classButton.translateXProperty().bind(classButton.widthProperty().divide(-2));
        classButton.translateYProperty().bind(classButton.heightProperty().divide(-2));

        timeLineButton.setLayoutX(classButton.getLayoutX());
        timeLineButton.setLayoutY(classButton.getLayoutY() + 50);

        // Při posunu, je kurzor vždy umístěn uprostřed
        timeLineButton.translateXProperty().bind(timeLineButton.widthProperty().divide(-2));
        timeLineButton.translateYProperty().bind(timeLineButton.heightProperty().divide(-2));


        Double x1 = classButton.getLayoutX();
        Double y1 = classButton.getLayoutY();
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x1);
        line.setEndY(y1 + 600);

        Styling();
        Update();
    }

    public void Styling(){
        classButton.getStyleClass().setAll("sqClassBox");
        line.getStyleClass().setAll("sqLifeLine");
        timeLineButton.getStyleClass().setAll("sqTimeLineButton");
    }

    public void Update(){
        /*classButton.setId("ID" + ID);
        ID = count ++;
        timeLineButton.setId("ID" + ID);
        ID = count ++;
        line.setId("ID" + ID);*/
        setId("ID" + ID);

        Label myLabel = new Label();
        myLabel.setPadding(new Insets(8,8,8,8));
        nameAndObjectProperty.setValue(nameObject + ":" + nameClass);
        myLabel.textProperty().bind(nameAndObjectProperty);
        classButton.setGraphic(myLabel);

        getChildren().setAll(line, classButton, timeLineButton);
    }

    /*public void Inconsistencies(){
        for(String names : ClassDiagramController.d.getListOfClassNames()) {
            if (!classButton.getItems().contains(names)){
                classButton.getStyleClass().setAll("inconsitentClass");
            }
        }
    }*/
}