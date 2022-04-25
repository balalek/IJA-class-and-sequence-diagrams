package com.component;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

public class ClassComponent extends Button {

    private int width;
    private int length;
    private boolean className;
    private boolean classAttributes;
    private boolean classOperations;

    public void setName(String name) {
        Name = name;
    }

    public String getName() {
        return Name;
    }

    private String Name = "";

    private String Attributes = "";
    private StringProperty nameProperty = new SimpleStringProperty();
    private static int count = 1;

    private int ID;

    // Getters and Setters
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
        setLayoutX(x);
        setLayoutY(y);

        // Appear in the cursor spike
        translateXProperty().bind(widthProperty().divide(-2));
        translateYProperty().bind(widthProperty().divide(-2));

        // TODO
        className = false;
        classAttributes = false;
        classOperations = false;

        // Vbox create
        ClassContent classContent = new ClassContent();
        // Content create and update
        classNameUpdate(classContent);
        classAttributesUpdate(classContent);
        classOperationsUpdate(classContent);
    }

    public void classNameUpdate(ClassContent classContent){
        ID = count++;
        setName("Class" + ID);

        // Updatable lable inside Vbox
        Label myLabel = new Label();
        nameProperty.setValue(Name);
        myLabel.textProperty().bind(nameProperty);

        classContent.getChildren().add(myLabel);

        setGraphic(classContent);
        getStyleClass().add("classBox");
    }

    private void classAttributesUpdate(ClassContent classContent) {
        //setAttributes();
    }

    private void classOperationsUpdate(ClassContent classContent) {
    }

}
