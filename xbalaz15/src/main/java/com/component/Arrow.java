package com.component;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;

import java.util.LinkedList;
import java.util.List;

public class Arrow extends Group {
    private Polyline mainLine = new Polyline();
    private Polyline headInher = new Polyline();
    private Polyline headInher2 = new Polyline();
    private Polyline headAggr = new Polyline();
    private Polyline headAggr2 = new Polyline();
    private Polyline headComp = new Polyline();
    private Polyline headComp2 = new Polyline();
    private SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private final double ARROWHEAD_ANGLE = Math.toRadians(31);
    private final double ARROWHEAD_LENGTH = 25;
    private String arrowType = "association";
    private String from;
    private String to;
    public static List <Arrow> ListOfArrows = new LinkedList<>();

    // Getters and setters
    public String getArrowType() {
        return arrowType;
    }
    public void setArrowType(String arrowType) {
        this.arrowType = arrowType;
    }
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
    public void setFrom(String from) {
        this.from = from;
    }
    public void setTo(String to) {
        this.to = to;
    }
    public static List<Arrow> getListOfArrows() {
        return ListOfArrows;
    }
    public String getFrom() {
        return from;
    }
    public String getTo() {
        return to;
    }

    // Constructor
    public Arrow(double x1, double y1, double x2, double y2) {
        ListOfArrows.add(this);
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

    // Constructor for file loading
    public Arrow(double x1, double y1, double x2, double y2, String arrowType){
        ListOfArrows.add(this);
        this.arrowType = arrowType;

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

    private void arrowHeadStyles() {
        mainLine.getStyleClass().setAll("mainLine");
        headInher.getStyleClass().setAll("whiteArrowHead");
        headInher2.getStyleClass().setAll("whiteArrowHead");
        headAggr.getStyleClass().setAll("whiteArrowHead");
        headAggr2.getStyleClass().setAll("whiteArrowHead");
        headComp.getStyleClass().setAll("blackDiamondHead");
        headComp2.getStyleClass().setAll("blackDiamondHead");
    }

    public void update() {
        getChildren().setAll(mainLine, headInher, headInher2, headAggr, headAggr2, headComp, headComp2);
        double[] start = scale(getX1(), getY1(), getX2(), getY2(), 50, 50);
        double[] end = scale(getX2(), getY2(), getX1(), getY1(), 50, 50);
        double x1 = start[0];
        double y1 = start[1];
        double x2 = end[0];
        double y2 = end[1];
        double theta;

        // NORMAL LINE -> ASSOCIATION
        mainLine.getPoints().setAll(x1, y1, x2, y2);
        toBack();

        // Draw line with arrowhead by arrow type
        switch (getArrowType()) {
            // NORMAL LINE -> ASSOCIATION
            case "association" -> {
                getChildren().removeAll(headAggr, headAggr2, headComp, headComp2, headInher, headInher2);
                break;
            }
            // GENERALIZATION ARROWHEAD
            case "generalization" -> {
                getChildren().removeAll(headAggr, headAggr2, headComp, headComp2, headInher, headInher2);

                // Hide end line behind box
                end = scale(getX2(), getY2(), getX1(), getY1(), 110, 110);
                x2 = end[0];
                y2 = end[1];
                theta = Math.atan2(y2 - y1, x2 - x1);

                // Drawing arrowhead
                mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headInher.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headInher.getPoints().addAll(x, y);

                // Arrow joining line
                theta = Math.atan2(y2 - y, x2 - x);
                double x3 = x2 + Math.cos(theta - 90) * 26;
                double y3 = y2 + Math.sin(theta - 90) * 26;
                headInher2.getPoints().setAll(x, y, x3, y3);
                getChildren().addAll(headInher, headInher2);
                break;
            }
            // BLACK DIAMOND ARROWHEAD
            case "composition" -> {
                getChildren().removeAll(headInher, headInher2, headAggr, headAggr2, headComp, headComp2);

                // Hide end line behind box
                end = scale(getX2(), getY2(), getX1(), getY1(), 150, 150);
                x2 = end[0];
                y2 = end[1];
                theta = Math.atan2(y2 - y1, x2 - x1);

                // Drawing arrowhead
                mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x4 = x2 + Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y4 = y2 + Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headComp.getPoints().setAll(x4, y4, x2, y2);
                double x = x2 + Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 + Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headComp.getPoints().addAll(x, y);

                // Join arrow to reverse arrow
                theta = Math.atan2(y2 - y, x2 - x);
                double x3 = x - Math.cos(theta - 45) * 27;
                double y3 = y - Math.sin(theta - 45) * 27;
                headComp2.getPoints().setAll(x, y, x3, y3, x4, y4);
                getChildren().addAll(headComp, headComp2);
                break;
            }
            // WHITE DIAMOND ARROWHEAD
            case "aggregation" -> {
                getChildren().removeAll(headInher, headInher2, headComp, headComp2, headAggr, headAggr2);

                // Hide end line behind box
                end = scale(getX2(), getY2(), getX1(), getY1(), 150, 150);
                x2 = end[0];
                y2 = end[1];
                theta = Math.atan2(y2 - y1, x2 - x1);

                // Drawing arrowhead
                mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x4 = x2 + Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y4 = y2 + Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAggr.getPoints().setAll(x4, y4, x2, y2);
                double x = x2 + Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 + Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAggr.getPoints().addAll(x, y);

                // Join arrow to reverse arrow
                theta = Math.atan2(y2 - y, x2 - x);
                double x3 = x - Math.cos(theta - 45) * 27;
                double y3 = y - Math.sin(theta - 45) * 27;
                headAggr2.getPoints().setAll(x, y, x3, y3, x4, y4);
                getChildren().addAll(headAggr, headAggr2);
                break;
            }
        }
    }

    private double[] scale ( double x1, double y1, double x2, double y2, int angle, int length){
        double theta = Math.atan2(y2 - y1, x2 - x1);
        return new double[]{
                x1 + Math.cos(theta) * angle,
                y1 + Math.sin(theta) * length
        };
    }
}