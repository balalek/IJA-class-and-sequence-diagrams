package com.javaprojekt;

import com.google.gson.annotations.Expose;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;
import com.google.gson.Gson;

public class ClassComponent extends Button {

    @Expose
    public Double y;
    @Expose
    public Double x;
    private String Name = "";
    private String Attributes = "";
    private String Operations = "";
    private StringProperty nameProperty = new SimpleStringProperty();
    private StringProperty attrProperty = new SimpleStringProperty();
    private StringProperty operationProperty = new SimpleStringProperty();
    private static int count = 1;
    private int ID;
    public ObservableList<Arrow> edges = FXCollections.observableArrayList();

    // Getters and Setters
    public void setName(String name) {
        Name = name;
    }
    public String getName() {
        return Name;
    }
    public void setAttrProperty(String attrProperty) {
        this.attrProperty.set(attrProperty);
    }
    public String getOperations() {
        return Operations;
    }
    public void setOperations(String operations) {
        Operations = operations;
    }
    public String getOperationProperty() {
        return operationProperty.get();
    }
    public StringProperty operationPropertyProperty() {
        return operationProperty;
    }
    public void setOperationProperty(String operationProperty) {
        this.operationProperty.set(operationProperty);
    }
    public String getAttrProperty() {
        return attrProperty.get();
    }
    public StringProperty attrPropertyProperty() {
        return attrProperty;
    }
    public String getAttributes() {
        return Attributes;
    }
    public void setAttributes(String attributes) {
        Attributes = attributes;
    }
    public String getNameProperty() {
        return nameProperty.get();
    }
    public StringProperty nameProperty() {
        return nameProperty;
    }
    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    // Constructor
    public ClassComponent(Double x, Double y){
        // Position of button
        this.x = x;
        this.y = y;
        setLayoutX(this.x);
        setLayoutY(this.y);

        // Appear in the cursor spike
        translateXProperty().bind(widthProperty().divide(-2));
        translateYProperty().bind(widthProperty().divide(-2));

        // Vbox create
        GridPane classContent = new GridPane();
        //classContent.setMinSize(175,175);
        classContent.autosize();
        // Content create and update
        classNameUpdate(classContent);
    }

    public void classNameUpdate(GridPane classContent){
        ID = count++;
        setName("Class" + ID);

        // Updatable lable inside Vbox
        Label myLabel = new Label();
        nameProperty.setValue(Name);
        myLabel.textProperty().bind(nameProperty);
        myLabel.setPadding(new Insets(8,0,8,0));
        myLabel.setMinWidth(175);
        myLabel.setAlignment(Pos.CENTER);

        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-border-style: solid;" + "-fx-border-width: 0 0 1 0;");

        classContent.setConstraints(myLabel, 0, 0);
        classContent.setConstraints(separator, 0, 1);
        classContent.getChildren().addAll(myLabel, separator);
        classAttributesUpdate(classContent);
    }

    public void classAttributesUpdate(GridPane classContent) {
        setAttributes("");

        // Updatable lable inside Vbox
        Label myLabel = new Label();
        attrProperty.setValue(Attributes);
        myLabel.textProperty().bind(attrProperty);
        myLabel.setPadding(new Insets(8,0,8,0));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-border-style: solid;" + "-fx-border-width: 0 0 1 0;");
        classContent.setConstraints(myLabel, 0, 2);
        classContent.setConstraints(separator, 0, 3);
        classContent.getChildren().addAll(myLabel, separator);

        classOperationsUpdate(classContent);
    }

    public void classOperationsUpdate(GridPane classContent) {
        setOperations("");

        // Updatable lable inside Vbox
        Label myLabel = new Label();
        operationProperty.setValue(Operations);
        myLabel.textProperty().bind(operationProperty);
        myLabel.setPadding(new Insets(8,0,8,0));

        classContent.setConstraints(myLabel, 0, 4);
        classContent.getChildren().add(myLabel);
        setGraphic(classContent);
        getStyleClass().add("classBox");
    }

}