package com.component;

import com.google.gson.annotations.Expose;
import com.javaprojekt.ClassDiagramController;
import com.uml.UMLClassifier;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.GridPane;

import java.util.LinkedList;
import java.util.List;

/**
 * Třída reprezentuje třídu v diagramu tříd a rozšiřuje třídu Button.
 * Obsahuje unikátní název, seznam atributů, seznam metod, typ třídy a souřadnice (x,y).
 * Používá se pro reprezentaci třídy v diagramu tříd a zaznamenání atributů a metod.
 */
public class ClassComponent extends Button {

    @Expose
    public Double y;
    @Expose
    public Double x;
    @Expose
    public String Name = "";
    @Expose
    public String Attributes = "";
    @Expose
    public String Operations = "";
    @Expose
    public String ClassType = "";
    public Boolean Normal = true;
    public Boolean AbstractClass = false;
    public Boolean Interface = false;
    private StringProperty nameProperty = new SimpleStringProperty();
    private StringProperty attrProperty = new SimpleStringProperty();
    private StringProperty operationProperty = new SimpleStringProperty();
    private StringProperty classTypeProperty = new SimpleStringProperty();
    private static int count = 1;
    private int ID;
    public ObservableList<Arrow> edges = FXCollections.observableArrayList();
    private Label opLabel = new Label();
    public static List<ClassComponent> ListofBoxes = new LinkedList<>();


    // GETry a SETry
    public String getClassType() {
        return ClassType;
    }
    public void setClassType(String classType) {
        this.ClassType = classType;
    }
    public void setClassTypeProperty(String classTypeProperty) {
        this.classTypeProperty.set(classTypeProperty);
    }
    public Boolean getNormal() {
        return Normal;
    }
    public void setNormal(Boolean normal) {
        Normal = normal;
    }
    public Boolean getAbstractClass() {
        return AbstractClass;
    }
    public void setAbstractClass(Boolean abstractClass) {
        AbstractClass = abstractClass;
    }
    public Boolean getInterface() {
        return Interface;
    }
    public void setInterface(Boolean anInterface) {
        Interface = anInterface;
    }
    public double getX() {
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }
    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
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
    public void setOperationProperty(String operationProperty) {
        this.operationProperty.set(operationProperty);
    }

    public String getAttributes() {
        return Attributes;
    }
    public void setAttributes(String attributes) {
        Attributes = attributes;
    }
    public void setNameProperty(String nameProperty) {
        this.nameProperty.set(nameProperty);
    }

    public Label getOpLabel() {
        return opLabel;
    }

    public void setOpLabel(Label opLabel) {
        this.opLabel = opLabel;
    }

    public static List<ClassComponent> getListofBoxes() {
        return ListofBoxes;
    }

    /**
     * Konstruktor
     * @param x X souřadnice třídy
     * @param y Y souřadnice třídy
     */
    public ClassComponent(Double x, Double y){
        ID = count++;
        this.x = x;
        this.y = y;
        // Pozice tlačítka
        setLayoutX(this.x);
        setLayoutY(this.y);

        // Vykreslí se tak aby získané souřadnice byly ve středu
        translateXProperty().bind(widthProperty().divide(-2));
        translateYProperty().bind(heightProperty().divide(-2));

        // Vytvoření Gridpanu
        GridPane classContent = new GridPane();
        classContent.autosize();
        // Vytvoření a aktualizace obsahu
        //classNameUpdate(classContent);
        classType(classContent);
    }


    /**
     * Konstruktor pro načítání ze souboru
     * @param x X souřadnice třídy
     * @param y Y souřadnice třídy
     * @param Name Název třídy
     * @param Attributes Řetězec atributů oddělených odřádkováním
     * @param Operations Řetězec metod oddělených odřádkováním
     * @param ClassType Typ třídy (normální, abstraktní a interface)
     */
    public ClassComponent(Double x, Double y, String Name, String Attributes, String Operations, String ClassType){
        ID = count++;
        this.x = x;
        this.y = y;
        this.Name = Name;
        this.Attributes = Attributes;
        this.Operations = Operations;
        this.ClassType = ClassType;
        setLayoutX(this.x);
        setLayoutY(this.y);

        // Store radio button
        switch (this.ClassType) {
            case "":
                setNormal(true);
                setAbstractClass(false);
                setInterface(false);
                break;
            case "<<Abstract>>":
                setAbstractClass(true);
                setInterface(false);
                setNormal(false);
                break;
            case "<<Interface>>":
                setInterface(true);
                setAbstractClass(false);
                setNormal(false);
                break;
        }

        // Vykreslí se tak aby získané souřadnice byly ve středu
        translateXProperty().bind(widthProperty().divide(-2));
        translateYProperty().bind(heightProperty().divide(-2));

        // Vytvoření Gridpanu
        GridPane classContent = new GridPane();
        classContent.autosize();
        // Content create and update
        classType(classContent);
    }

    /**
     * Metoda aktualizuje obsah horního popisku (typ třídy) a volá classNameUpdate
     * @param classContent Odkaz na objekt který upravujeme
     */
    public void classType(GridPane classContent){

        Label myLabel = new Label();
        classTypeProperty.setValue(ClassType);
        myLabel.textProperty().bind(classTypeProperty);
        myLabel.setPadding(new Insets(0,0,0,0));
        myLabel.setMinWidth(175);
        myLabel.setAlignment(Pos.CENTER);

        classContent.setConstraints(myLabel, 0, 0);
        classContent.getChildren().addAll(myLabel);
        classNameUpdate(classContent);
    }

    /**
     * Metoda aktualizuje jméno třídy, kterou upravujeme (vykreslený obsah)
     * @param classContent Odkaz na objekt který upravujeme
     */
    public void classNameUpdate(GridPane classContent){

        if(Name.equals("")) {
            setName("Class" + ID);
            ClassDiagramController.d.addName(getName());
        }

        // Upravovatelný nápis v boxu
        Label myLabel = new Label();
        nameProperty.setValue(getName());
        myLabel.textProperty().bind(nameProperty);
        myLabel.setPadding(new Insets(0,0,4,0));
        myLabel.setMinWidth(175);
        myLabel.setMinHeight(0);
        myLabel.setAlignment(Pos.CENTER);

        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-border-style: solid;" + "-fx-border-width: 0 0 1 0;");

        classContent.setConstraints(myLabel, 0, 1);
        classContent.setConstraints(separator, 0, 2);
        classContent.getChildren().addAll(myLabel, separator);
        classAttributesUpdate(classContent);
    }

    /**
     * Metoda aktualizuje atributy třídy, kterou upravujeme (vykreslený obsah)
     * @param classContent Odkaz na objekt který upravujeme
     */
    public void classAttributesUpdate(GridPane classContent) {
        if(Attributes.equals("")) setAttributes("");

        // Aktualizovatelný nápis v boxu
        Label myLabel = new Label();
        attrProperty.setValue(Attributes);
        myLabel.textProperty().bind(attrProperty);
        myLabel.setPadding(new Insets(8,0,8,0));

        Separator separator = new Separator(Orientation.HORIZONTAL);
        separator.setStyle("-fx-border-style: solid;" + "-fx-border-width: 0 0 1 0;");
        classContent.setConstraints(myLabel, 0, 3);
        classContent.setConstraints(separator, 0, 4);
        classContent.getChildren().addAll(myLabel, separator);

        classOperationsUpdate(classContent);
    }

    /**
     * Metoda aktualizuje metody třídy, kterou upravujeme (vykreslený obsah)
     * @param classContent Odkaz na objekt který upravujeme
     */
    public void classOperationsUpdate(GridPane classContent) {
        if (Operations.equals("")) setOperations("");

        // Upravovatelný nápis v boxu
        operationProperty.setValue(Operations);
        opLabel.textProperty().bind(operationProperty);
        opLabel.setPadding(new Insets(8, 0, 8, 0));

        classContent.setConstraints(opLabel, 0, 5);
        classContent.getChildren().add(opLabel);
        setGraphic(classContent);
        getStyleClass().add("classBox");
    }
}