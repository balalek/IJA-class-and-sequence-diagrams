package com.seqComponent;

import com.component.Arrow;
import com.uml.arrType;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.control.ComboBox;
import javafx.scene.shape.Polyline;

import java.util.LinkedList;
import java.util.List;

public class Messages extends Group {
    private final double ARROWHEAD_ANGLE = Math.toRadians(20);
    private final double ARROWHEAD_LENGTH = 25;
    // Attributes
    private Polyline mainLine = new Polyline();
    private Polyline headSynch = new Polyline();
    private Polyline headSynch2 = new Polyline();
    private Polyline headAsynch = new Polyline();
    private Polyline headAsynch2 = new Polyline();
    private Polyline headReturn = new Polyline();
    private Polyline headReturn2 = new Polyline();
    private ComboBox comboBox = new ComboBox();
    private String arrowType = "Synchronous";
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

    public String getArrowType() {
        return arrowType;
    }

    public void setArrowType(String arrowType) {
        this.arrowType = arrowType;
    }

    // konstruktor
    public Messages(double x1, double y1, double x2) {
        //ListOfArrows.add(this);
        setX1(x1);
        setY1(y1);
        setX2(x2);
        setY2(y1);
        getChildren().setAll(mainLine, comboBox, headAsynch, headAsynch2, headReturn, headReturn2, headSynch, headSynch2);
        mainLine.getPoints().setAll(getX1(), getY1(), getX2(), getY2());
        // Jaktaž prostředek čáry
        comboBox.setLayoutX(((x2 + x1) / 2));
        // Trochu nad čáru
        comboBox.setLayoutY(y1 - 25);

        // Appear in the cursor spike
        comboBox.translateXProperty().bind(comboBox.widthProperty().divide(-2));
        comboBox.translateYProperty().bind(comboBox.heightProperty().divide(-2));

        /*for (SimpleDoubleProperty s : new SimpleDoubleProperty[]{x1Property(), y1Property(), x2Property(), y2Property()}) {
            s.addListener((l, o, n) -> update());
        }*/
        arrowHeadAndBoxStyles();
        update();
    }

    public void arrowHeadAndBoxStyles() {
        mainLine.getStyleClass().setAll("mainLine");
        comboBox.getStyleClass().setAll("comboBox");
        headSynch.getStyleClass().setAll("blackArrowHead");
        headAsynch.getStyleClass().setAll("normalArrowHead");
        headReturn.getStyleClass().setAll("dashedLineArrow");
    }

    public void update() {
        double[] start = scale(getX1(), getY1(), getX2(), getY2());
        double[] end = scale(getX2(), getY2(), getX1(), getY1());
        double x1 = start[0];
        double y1 = start[1];
        double x2 = end[0];
        double y2 = end[1];
        double theta = Math.atan2(y2 - y1, x2 - x1);
        //double theta;
        toBack();

        // Draw line with arrowhead by arrow type
        switch (getArrowType()) {
            // Asynchronní zpráva
            case "Asynchronous": {
                getChildren().removeAll(headAsynch, headReturn, headSynch);
                mainLine.getStyleClass().setAll("mainLine");
                mainLine.getStyleClass().remove("dashedMainLine");

                // Drawing arrowhead
                //mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAsynch.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAsynch.getPoints().addAll(x, y);
                getChildren().addAll(headAsynch);
                break;
            }
            // Synchronní zpráva
            case "Synchronous": {
                getChildren().removeAll(headAsynch, headReturn, headSynch);
                mainLine.getStyleClass().setAll("mainLine");
                mainLine.getStyleClass().remove("dashedMainLine");

                // Drawing arrowhead
                //mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headSynch.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headSynch.getPoints().addAll(x, y);
                getChildren().addAll(headSynch);
                break;
            }
            // Návratová zpráva
            case "Return": {
                getChildren().removeAll(headAsynch, headReturn, headSynch);
                mainLine.getStyleClass().setAll("dashedMainLine");
                mainLine.getStyleClass().remove("mainLine");


                // Drawing arrowhead
                //mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headReturn.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headReturn.getPoints().addAll(x, y);
                getChildren().addAll(headReturn);
                break;
            }
        }

    }


    private double[] scale ( double x1, double y1, double x2, double y2){
        double theta = Math.atan2(y2 - y1, x2 - x1);
        return new double[]{
                x1 + Math.cos(theta),
                y1 + Math.sin(theta)
        };
    }
}