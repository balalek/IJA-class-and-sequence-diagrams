package com.seqComponent;

import com.component.Arrow;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.shape.Polyline;

import java.util.LinkedList;
import java.util.List;

public class Messages extends Group {
    // Attributes
    private Polyline mainLine = new Polyline();
    private ComboBox comboBox = new ComboBox();
    private SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private String from;
    private String to;
    public static List<Arrow> ListOfArrows = new LinkedList<>();


    // Getters and setters
    public double getX1() {
        return x1.get();
    }
    public SimpleDoubleProperty x1Property() {
        return x1;
    }
    public void setX1(double x1) {
        this.x1.set(x1);
    }
    public double getY1() {
        return y1.get();
    }
    public SimpleDoubleProperty y1Property() {
        return y1;
    }
    public void setY1(double y1) {
        this.y1.set(y1);
    }
    public double getX2() {
        return x2.get();
    }
    public SimpleDoubleProperty x2Property() {
        return x2;
    }
    public void setX2(double x2) {
        this.x2.set(x2);
    }
    public double getY2() {
        return y2.get();
    }
    public SimpleDoubleProperty y2Property() {
        return y2;
    }
    public void setY2(double y2) {
        this.y2.set(y2);
    }
    public String getFrom() {
        return from;
    }
    public void setFrom(String from) {
        this.from = from;
    }
    public String getTo() {
        return to;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public ComboBox getComboBox() {
        return comboBox;
    }
    public void setComboBox(ComboBox comboBox) {
        this.comboBox = comboBox;
    }

    // Constructor
    public Messages(double x1, double y1, double x2, double y2) {
        //ListOfArrows.add(this);
        setX1(x1);
        setY1(y1);
        setX2(x2);
        setY2(y2);

        for (SimpleDoubleProperty s : new SimpleDoubleProperty[]{x1Property(), y1Property(), x2Property(), y2Property()}) {
            s.addListener((l, o, n) -> update());
        }
        arrowHeadStyles();
        update();
    }

    public void arrowHeadStyles(){

    }
    public void update(){
        getChildren().setAll(mainLine);
        double[] start = scale(getX1(), getY1(), getX2(), getY2(), 50);
        double[] end = scale(getX2(), getY2(), getX1(), getY1(), 50);
        double x1 = start[0];
        double y1 = start[1];
        double x2 = end[0];
        double y2 = end[1];
        double theta;

        // NORMAL LINE -> ASSOCIATION
        mainLine.getPoints().setAll(x1, y1, x2, y2);
        toBack();
    }

    private double[] scale ( double x1, double y1, double x2, double y2, int arrow_scaler){
        double theta = Math.atan2(y2 - y1, x2 - x1);
        return new double[]{
                x1 + Math.cos(theta) * arrow_scaler,
                y1 + Math.sin(theta) * arrow_scaler
        };
    }

}
