package com.component;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Polyline;
import java.util.LinkedList;
import java.util.List;

/**
 * Třída reprezentuje vstahy (šipky) mezi třídami a rozšiřuje třídu Group.
 * Má seznam všech šipek, odkaz na počáteční, cílový box (reprezentující třídu), název počáteční
 * a cílové třídy, typ vstahu (asociace, agregace, kompozice a generalizace), souřadnice počátku a konce.
 * Lze využít na vyznačení vztahů v diagramu tříd.
 */

public class Arrow extends Group {
    private Polyline mainLine = new Polyline();
    private Polyline headInher = new Polyline();
    private Polyline headInher2 = new Polyline();
    private Polyline headAggr = new Polyline();
    private Polyline headAggr2 = new Polyline();
    private Polyline headComp = new Polyline();
    private Polyline headComp2 = new Polyline();
    private Polyline headAssoc = new Polyline();
    private Polyline headAssoc2 = new Polyline();
    private SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private final double ARROWHEAD_ANGLE = Math.toRadians(31);
    private final double ARROWHEAD_LENGTH = 25;
    private String arrowType = "association";
    private String from;
    private String to;
    public static List<Arrow> ListOfArrows = new LinkedList<>();
    private ClassComponent FromBox;
    private ClassComponent ToBox;

    // GETry a SETry
    public String getArrowType() {
        return arrowType;
    }
    public void setArrowType(String arrowType) {
        this.arrowType = arrowType;
    }

    public SimpleDoubleProperty x1Property() {
        return x1;
    }
    public void setX1(double x1) {
        this.x1.set(x1);
    }

    public SimpleDoubleProperty y1Property() {
        return y1;
    }
    public void setY1(double y1) {
        this.y1.set(y1);
    }

    public SimpleDoubleProperty x2Property() {
        return x2;
    }
    public void setX2(double x2) {
        this.x2.set(x2);
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

    public ClassComponent getFromBox() {
        return FromBox;
    }

    public ClassComponent getToBox() {
        return ToBox;
    }

    /**
     * Konstruktor pro vytvoření vztahu mezi třídami
     * @param From Odkaz na třídu od kud povede šipka
     * @param To Odkaz na třídu kam povede šipka
     */
    public Arrow(ClassComponent From, ClassComponent To) {
        ListOfArrows.add(this);
        this.FromBox = From;
        this.ToBox = To;
        setX1(From.getX());
        setY1(From.getY());
        setX2(To.getX());
        setY2(To.getY());

        for (SimpleDoubleProperty s : new SimpleDoubleProperty[]{x1Property(), y1Property(), x2Property(), y2Property()}) {
            s.addListener((l, o, n) -> update());
        }
        arrowHeadStyles();
        update();
    }

    /**
     * Konstruktor pro vytvoření vztahu mezi třídami pro načítání ze souboru
     * @param From Odkaz na třídu od kud povede šipka
     * @param To Odkaz na třídu kam povede šipka
     * @param arrowType Typ vztahu mezi třídami
     */
    public Arrow(ClassComponent From, ClassComponent To, String arrowType) {
        ListOfArrows.add(this);
        this.arrowType = arrowType;
        this.FromBox = From;
        this.ToBox = To;
        setX1(From.getX());
        setY1(From.getY());
        setX2(To.getX());
        setY2(To.getY());

        for (SimpleDoubleProperty s : new SimpleDoubleProperty[]{x1Property(), y1Property(), x2Property(), y2Property()}) {
            s.addListener((l, o, n) -> update());
        }
        arrowHeadStyles();
        update();
    }

    /**
     * Nastavení css stylů
     */
    private void arrowHeadStyles() {
        mainLine.getStyleClass().setAll("mainLine");
        headInher.getStyleClass().setAll("whiteArrowHead");
        headInher2.getStyleClass().setAll("whiteArrowHead");
        headAggr.getStyleClass().setAll("whiteArrowHead");
        headAggr2.getStyleClass().setAll("whiteArrowHead");
        headComp.getStyleClass().setAll("blackDiamondHead");
        headComp2.getStyleClass().setAll("blackDiamondHead");
        headAssoc.getStyleClass().setAll("arrowHead");
        headAssoc2.getStyleClass().setAll("arrowHead");
    }

    /**
     * Vykreslí danou šipku při vytvoření nebo pohybu objektu na který je připojena
     */
    public void update() {
        getChildren().setAll(mainLine, headInher, headInher2, headAggr, headAggr2, headComp, headComp2, headAssoc, headAssoc2);
        double[] start = scale(this.FromBox, this.ToBox);
        double[] end = scale(this.ToBox, this.FromBox);
        double x1 = start[0];
        double y1 = start[1];
        double x2 = end[0];
        double y2 = end[1];
        double theta = Math.atan2(y2 - y1, x2 - x1);
        toBack();

        // Zakreslí úsečku s zakončením/šipkou podle typu vztahu (arrowType)
        switch (getArrowType()) {
            // Zakončení asociace
            case "association": {
                getChildren().removeAll(headAggr, headAggr2, headComp, headComp2, headInher, headInher2, headAssoc, headAssoc2);

                // Drawing arrowhead
                mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAssoc.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAssoc.getPoints().addAll(x, y);
                getChildren().addAll(headAssoc, headAssoc2);
                break;
            }
            // Zakončení generalizace
            case "generalization": {
                getChildren().removeAll(headAggr, headAggr2, headComp, headComp2, headInher, headInher2, headAssoc, headAssoc2);

                // Zakreslení šipky
                mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headInher.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headInher.getPoints().addAll(x, y);

                // Připojení šipky
                theta = Math.atan2(y2 - y, x2 - x);
                double x3 = x2 + Math.cos(theta - 90) * 26;
                double y3 = y2 + Math.sin(theta - 90) * 26;
                headInher2.getPoints().setAll(x, y, x3, y3);
                getChildren().addAll(headInher, headInher2);
                break;
            }
            // Zakončení kompozice (plný diamant)
            case "composition": {
                getChildren().removeAll(headInher, headInher2, headAggr, headAggr2, headComp, headComp2, headAssoc, headAssoc2);

                // Zakreslení šipky
                mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headComp.getPoints().setAll(x, y, x2, y2);
                double x3 = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y3 = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headComp.getPoints().addAll(x3, y3);

                // Připojení opačné šipky k šipce
                theta = Math.atan2(y2 - y, x2 - x);
                double x4 = x - Math.cos(theta - 45) * 27;
                double y4 = y - Math.sin(theta - 45) * 27;
                headComp2.getPoints().setAll(x, y, x3, y3, x4, y4);
                getChildren().addAll(headComp, headComp2);
                break;
            }
            // Zakončení agregace (Prázdný diamant)
            case "aggregation": {
                getChildren().removeAll(headInher, headInher2, headComp, headComp2, headAggr, headAggr2, headAssoc, headAssoc2);

                // Zakreslení šipky
                mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAggr.getPoints().setAll(x, y, x2, y2);
                double x3 = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y3 = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAggr.getPoints().addAll(x3, y3);

                // Připojení opačné šipky k šipce
                theta = Math.atan2(y2 - y, x2 - x);
                double x4 = x - Math.cos(theta - 45) * 27;
                double y4 = y - Math.sin(theta - 45) * 27;
                headAggr2.getPoints().setAll(x, y, x4, y4, x3, y3);
                getChildren().addAll(headAggr, headAggr2);
                break;
            }
        }
    }

    /**
     * Metoda scale se používá k získání souřadnic (x,y) kde šipka začíná
     * @param box1 Odkaz na třídu1
     * @param box2 Odkaz na třídu2
     * @return Vrací souřadnice průsečíku nejbližší hrany třídy1 a spojnice středů třídy1 a třídy2
     */
    private double[] scale(ClassComponent box1, ClassComponent box2) {
        double beta = Math.atan2(box1.getX() - box2.getX(), box1.getY() - box2.getY());
        double len1 = (box1.getWidth() / 2) / Math.sin(beta);
        double len2 = (box1.getHeight() / 2) / Math.cos(beta);
        double finalX = 0;
        double finalY = 0;

        if(Math.abs(len1) < Math.abs(len2)) {
            if(Math.toDegrees(beta) >= 0){
                // Levá strana
                finalX = box1.getX() - (box1.getWidth() / 2);
                if(Math.toDegrees(beta) <= 90) finalY = box1.getY() - Math.sqrt(Math.pow(len1, 2) - Math.pow(box1.getWidth()/2, 2));
                else finalY = box1.getY() + Math.sqrt(Math.pow(len1, 2) - Math.pow(box1.getWidth()/2, 2));
            }else{
                // Pravá strana
                finalX = box1.getX() + (box1.getWidth() / 2);
                if(Math.toDegrees(beta) <= -90) finalY = box1.getY() + Math.sqrt(Math.pow(len1, 2) - Math.pow(box1.getWidth()/2, 2));
                else finalY = box1.getY() - Math.sqrt(Math.pow(len1, 2) - Math.pow(box1.getWidth()/2, 2));
            }
        }else {
            if(Math.abs(Math.toDegrees(beta)) <= 90){
                // Vršek
                finalY = box1.getY() - (box1.getHeight() / 2);
                finalX = box1.getX() - box1.getHeight()/2 * (Math.tan(beta));
            }else {
               // Spodek
                finalY = box1.getY() + (box1.getHeight() / 2);
                finalX = box1.getX() + box1.getHeight()/2 * Math.tan(beta);
            }

        }
        return new double[]{
                finalX,
                finalY
        };
    }
}