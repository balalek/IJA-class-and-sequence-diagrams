/**
 * @author Josef Kuba
 */
package com.seqComponent;

import com.google.gson.annotations.Expose;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import java.util.LinkedList;
import java.util.List;

/**
 * Třída reprezentuje instanci třídy z diagramu tříd, časovou osu a aktivační box (call-box).
 * Obsahuje unikátní ID, jméno objektu, jméno třídy, velikost call-boxu a souřadnice call-boxu a objektu(x,y).
 * Používá se pro reprezentaci instanci třídy z diagramu tříd a názvu objektu a názvu třídy.
 */
public class ObjectWithLine extends Group {

    // Atributy
    @Expose
    public Double x;
    @Expose
    public Double y;
    @Expose
    public Boolean isDestroyed = false;
    @Expose
    public String nameObject = "";
    @Expose
    public String nameClass = "";
    @Expose
    public Double yOfCallBox;
    @Expose
    public Integer height;
    private SimpleStringProperty nameAndObjectProperty = new SimpleStringProperty();
    private Polyline destroyLine1 = new Polyline();
    private Polyline destroyLine2 = new Polyline();
    private Button classButton = new Button();
    private Button timeLineButton = new Button();
    private Line line = new Line();
    private String className;
    public ObservableList<Messages> edges = FXCollections.observableArrayList();
    private static int count = 1;
    private int ID;
    public static List<ObjectWithLine> ListObjectWithLine = new LinkedList<>();

    // GETry a SETry
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
    public Integer getHeight() {
        return height;
    }
    public void setHeight(Integer height) {
        this.height = height;
    }
    public Double getyOfCallBox() {
        return yOfCallBox;
    }
    public void setyOfCallBox(Double yOfCallBox) {
        this.yOfCallBox = yOfCallBox;
    }
    public Boolean getDestroyed() {
        return isDestroyed;
    }
    public void setDestroyed(Boolean destroyed) {
        isDestroyed = destroyed;
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
    public static List<ObjectWithLine> getListObjectWithLine() {
        return ListObjectWithLine;
    }

    /**
     * Konstruktor pro vytváření grafického objektu se časovou osou a call-boxu
     * @param x X souřadnice objektu
     * @param y Y souřadnice objektu
     */
    public ObjectWithLine(Double x, Double y) {
        ID = count++;
        this.x = x;
        this.y = y;
        this.height = 0;
        //classButton.setText(className);
        ListObjectWithLine.add(this);
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

    /**
     * Konstruktor pro načítání grafického objektu se časovou osou a call-boxu
     * @param x X souřadnice objektu
     * @param y Y souřadnice objektu
     */
    public ObjectWithLine(Double x, Double y, Boolean isDestroyed, String nameObject, String nameClass, Double yOfCallBox, Integer height) {
        ID = count++;
        this.x = x;
        this.y = y;
        this.isDestroyed = isDestroyed;
        this.nameObject = nameObject;
        this.nameClass = nameClass;
        this.yOfCallBox = yOfCallBox;
        this.height = height;
        //classButton.setText(className);
        ListObjectWithLine.add(this);
        classButton.setLayoutX(x);
        classButton.setLayoutY(y);

        // Při posunu, je kurzor vždy umístěn uprostřed
        classButton.translateXProperty().bind(classButton.widthProperty().divide(-2));
        classButton.translateYProperty().bind(classButton.heightProperty().divide(-2));

        timeLineButton.setLayoutX(classButton.getLayoutX());
        timeLineButton.setLayoutY(classButton.getLayoutY() + yOfCallBox);

        // Při posunu, je kurzor vždy umístěn uprostřed
        timeLineButton.translateXProperty().bind(timeLineButton.widthProperty().divide(-2));
        timeLineButton.translateYProperty().bind(timeLineButton.heightProperty().divide(-2));
        timeLineButton.setText("\n");
        for(int i = 0; i < height; i++) timeLineButton.setText(timeLineButton.getText() + "\n");


        Double x1 = classButton.getLayoutX();
        Double y1 = classButton.getLayoutY();
        line.setStartX(x1);
        line.setStartY(y1);
        line.setEndX(x1);
        line.setEndY(y1 + 600);

        Styling();
        Update();
        if(isDestroyed) setDestroyObject();
    }

    /**
     * Nastavení css stylů
     */
    public void Styling(){
        classButton.getStyleClass().setAll("sqClassBox");
        line.getStyleClass().setAll("sqLifeLine");
        timeLineButton.getStyleClass().setAll("sqTimeLineButton");
    }

    /**
     * Metoda aktualizuje obsah objektu, který upravujeme (vykreslený obsah)
     */
    public void Update(){
        setId("ID" + ID);

        Label myLabel = new Label();
        myLabel.setPadding(new Insets(8,8,8,8));
        nameAndObjectProperty.setValue(nameObject + ":" + nameClass);
        myLabel.textProperty().bind(nameAndObjectProperty);
        classButton.setGraphic(myLabel);

        getChildren().setAll(line, classButton, timeLineButton);
    }

    /**
     * Objeví se křížek na konci časové osy, což signalizuje zánik objektu
     */
    public void setDestroyObject(){
        Double X1 = getLine().getEndX();
        Double Y1 = getLine().getEndY();
        Double ARROWHEAD_ANGLE = Math.toRadians(45);
        Double ARROWHEAD_LENGTH = 25.0;
        getChildren().removeAll(destroyLine1, destroyLine2);
        // Drawing arrowhead
        double x = X1 - Math.cos(ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        double y = Y1 - Math.sin(ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        destroyLine1.getPoints().setAll(x, y, X1, Y1);
        x = X1 - Math.cos(- ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        y = Y1 - Math.sin(- ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        destroyLine1.getPoints().addAll(x, y);

        double x1 = X1 + Math.cos(ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        double y1 = Y1 + Math.sin(ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        destroyLine2.getPoints().setAll(x1, y1, X1, Y1);
        x1 = X1 + Math.cos(- ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        y1 = Y1 + Math.sin(- ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
        destroyLine2.getPoints().addAll(x1, y1);
        getChildren().addAll(destroyLine1, destroyLine2);
    }

    /**
     * Odstraní se křížek, který signalizuje zánik objektu
     */
    public void deleteDestroyObject(){
        getChildren().removeAll(destroyLine1, destroyLine2);
    }
}