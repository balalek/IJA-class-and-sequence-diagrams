package com.seqComponent;

import com.component.Arrow;
import com.component.ClassComponent;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Polyline;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

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
    private String createMessage = "<<create>>";
    private String asOrSynMessage = "";
    private String returnMessage = "";
    private StringProperty createProperty = new SimpleStringProperty();
    private StringProperty asOrSynProperty = new SimpleStringProperty();
    private StringProperty returnProperty = new SimpleStringProperty();
    private Button CreateObjectButton = new Button();
    private Button AsynAndSynClassButton = new Button();
    private Button ReturnButton = new Button();
    private String arrowType = "Synchronous";
    private SimpleDoubleProperty x1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y1 = new SimpleDoubleProperty();
    private SimpleDoubleProperty x2 = new SimpleDoubleProperty();
    private SimpleDoubleProperty y2 = new SimpleDoubleProperty();
    private String from;
    private String to;
    public static List<Messages> ListOfArrows = new LinkedList<>();


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
    public Button getCreateObjectButton() {
        return CreateObjectButton;
    }
    public void setCreateObjectButton(Button createObjectButton) {
        CreateObjectButton = createObjectButton;
    }
    public Button getAsynAndSynClassButton() {
        return AsynAndSynClassButton;
    }
    public void setAsynAndSynClassButton(Button asynAndSynClassButton) {
        AsynAndSynClassButton = asynAndSynClassButton;
    }
    public Button getReturnButton() {
        return ReturnButton;
    }
    public void setReturnButton(Button returnButton) {
        ReturnButton = returnButton;
    }
    public String getArrowType() {
        return arrowType;
    }
    public void setArrowType(String arrowType) {
        this.arrowType = arrowType;
    }
    public String getCreateMessage() {
        return createMessage;
    }
    public void setCreateMessage(String createMessage) {
        this.createMessage = createMessage;
    }
    public String getCreateProperty() {
        return createProperty.get();
    }
    public StringProperty createPropertyProperty() {
        return createProperty;
    }
    public void setCreateProperty(String createProperty) {
        this.createProperty.set(createProperty);
    }
    public String getAsOrSynMessage() {
        return asOrSynMessage;
    }
    public void setAsOrSynMessage(String asOrSynMessage) {
        this.asOrSynMessage = asOrSynMessage;
    }
    public String getReturnMessage() {
        return returnMessage;
    }
    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }
    public String getAsOrSynProperty() {
        return asOrSynProperty.get();
    }
    public StringProperty asOrSynPropertyProperty() {
        return asOrSynProperty;
    }
    public void setAsOrSynProperty(String asOrSynProperty) {
        this.asOrSynProperty.set(asOrSynProperty);
    }
    public String getReturnProperty() {
        return returnProperty.get();
    }
    public StringProperty returnPropertyProperty() {
        return returnProperty;
    }
    public void setReturnProperty(String returnProperty) {
        this.returnProperty.set(returnProperty);
    }
    public Polyline getMainLine() {
        return mainLine;
    }
    public Polyline getHeadSynch() {
        return headSynch;
    }
    public Polyline getHeadSynch2() {
        return headSynch2;
    }
    public Polyline getHeadAsynch() {
        return headAsynch;
    }
    public Polyline getHeadAsynch2() {
        return headAsynch2;
    }
    public Polyline getHeadReturn() {
        return headReturn;
    }
    public Polyline getHeadReturn2() {
        return headReturn2;
    }

    public static List<Messages> getListOfArrows() {
        return ListOfArrows;
    }

    // Konstruktor
    public Messages(double x1, double y1, double x2) {
        ListOfArrows.add(this);
        setX1(x1);
        setY1(y1);
        setX2(x2);
        setY2(y1);
        mainLine.getPoints().setAll(getX1(), getY1(), getX2(), getY2());

        // Jaktaž prostředek čáry
        AsynAndSynClassButton.setLayoutX(((x2 + x1) / 2));
        // Trochu nad čáru
        AsynAndSynClassButton.setLayoutY(y1 - 15);
        // Appear in the cursor spike
        AsynAndSynClassButton.translateXProperty().bind(AsynAndSynClassButton.widthProperty().divide(-2));
        AsynAndSynClassButton.translateYProperty().bind(AsynAndSynClassButton.heightProperty().divide(-2));

        CreateObjectButton.setLayoutX(((x2 + x1) / 2));
        CreateObjectButton.setLayoutY(y1 - 15);
        CreateObjectButton.translateXProperty().bind(CreateObjectButton.widthProperty().divide(-2));
        CreateObjectButton.translateYProperty().bind(CreateObjectButton.heightProperty().divide(-2));

        ReturnButton.setLayoutX(((x2 + x1) / 2));
        ReturnButton.setLayoutY(y1 - 15);
        ReturnButton.translateXProperty().bind(ReturnButton.widthProperty().divide(-2));
        ReturnButton.translateYProperty().bind(ReturnButton.heightProperty().divide(-2));

        toFront();
        arrowHeadAndBoxStyles();
        update();
    }

    public void arrowHeadAndBoxStyles() {
        mainLine.getStyleClass().setAll("mainLine");
        AsynAndSynClassButton.getStyleClass().setAll("comboBox");
        ReturnButton.getStyleClass().setAll("comboBox");
        CreateObjectButton.getStyleClass().setAll("comboBox");
        headSynch.getStyleClass().setAll("blackArrowHead");
        headAsynch.getStyleClass().setAll("normalArrowHead");
        headReturn.getStyleClass().setAll("dashedLineArrow");
    }

    public void update() {
        mainLine.getPoints().setAll(getX1(), getY1(), getX2(), getY2());
        double[] start = scale(getX1(), getY1(), getX2(), getY2());
        double[] end = scale(getX2(), getY2(), getX1(), getY1());
        double x1 = start[0];
        double y1 = start[1];
        double x2 = end[0];
        double y2 = end[1];
        double theta = Math.atan2(y2 - y1, x2 - x1);
        toBack();

        // Draw line with arrowhead by arrow type
        switch (getArrowType()) {
            // Asynchronní zpráva
            case "Asynchronous": {
                getChildren().removeAll(headAsynch, headReturn, headSynch, AsynAndSynClassButton, CreateObjectButton, ReturnButton);
                mainLine.getStyleClass().setAll("mainLine");
                mainLine.getStyleClass().remove("dashedMainLine");
                // Drawing arrowhead
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAsynch.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAsynch.getPoints().addAll(x, y);

                // Aktualizovatelný nápis v boxu
                Label myLabel = new Label();
                asOrSynProperty.setValue(asOrSynMessage);
                myLabel.textProperty().bind(asOrSynProperty);
                AsynAndSynClassButton.setGraphic(myLabel);

                getChildren().setAll(mainLine,headAsynch, AsynAndSynClassButton);
                break;
            }
            // Synchronní zpráva
            case "Synchronous": {
                getChildren().removeAll(headAsynch, headReturn, headSynch, AsynAndSynClassButton, CreateObjectButton, ReturnButton);
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

                Label myLabel = new Label();
                asOrSynProperty.setValue(asOrSynMessage);
                myLabel.textProperty().bind(asOrSynProperty);
                AsynAndSynClassButton.setGraphic(myLabel);

                getChildren().setAll(mainLine, headSynch, AsynAndSynClassButton);
                break;
            }
            // Návratová zpráva
            case "Return": {
                getChildren().removeAll(headAsynch, headReturn, headSynch, AsynAndSynClassButton, CreateObjectButton, ReturnButton);
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

                Label myLabel = new Label();
                returnProperty.setValue(returnMessage);
                myLabel.textProperty().bind(returnProperty);
                ReturnButton.setGraphic(myLabel);

                getChildren().setAll(mainLine, headReturn, ReturnButton);
                break;
            }
            // Vytvářecí zpráva
            case "Create": {
                getChildren().removeAll(headAsynch, headReturn, headSynch, AsynAndSynClassButton, CreateObjectButton, ReturnButton);
                mainLine.getStyleClass().setAll("mainLine");
                mainLine.getStyleClass().remove("dashedMainLine");
                // Drawing arrowhead
                //mainLine.getPoints().setAll(x1, y1, x2, y2);
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headReturn.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headReturn.getPoints().addAll(x, y);

                Label myLabel = new Label();
                createProperty.setValue(createMessage);
                myLabel.textProperty().bind(createProperty);
                CreateObjectButton.setGraphic(myLabel);

                getChildren().setAll(mainLine, headReturn, CreateObjectButton);
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

    /**
     * Tato funkce kontroluje zda existuje operace napsaná nad šipkou (a počet argumentů).
     * Pokud neexistuje, obarví šipku a rámeček zprávy červeně jinak černě.
     */
    public void CheckArrowMessage(){
        Messages arrow = this;
        if(Objects.equals(arrow.getArrowType(), "Synchronous") || Objects.equals(arrow.getArrowType(), "Asynchronous")){
            //System.out.println(arrow.getAsOrSynMessage());
            //System.out.println(ClassComponent.getListofBoxes());
            List<String> AllOperations = new LinkedList<>();
            for (ClassComponent box: ClassComponent.getListofBoxes()) {
                if(!Objects.equals(box.getOperations(), "")){
                    String  Operations[] = box.getOperations().split("\n");
                    //AllOperations.add(Operations);
                    for (String oper: Operations) {
                        if(oper.indexOf(" ", oper.indexOf(" ") + 1) < oper.indexOf("("))
                        {
                            //Normální metoda
                            //System.out.println(oper.split(" ", 3)[2]);
                            oper = (oper.split(" ", 3)[2]);

                        }else{
                            //Konstruktor
                            //System.out.println(oper.split(" ", 2)[1]);
                            oper = (oper.split(" ", 2)[1]);
                        }
                        int count = 0;
                        for (int i = 0; i < oper.length(); i++) {

                            char temp = oper.charAt(i);
                            if (temp == ',')
                                count++;
                        }
                        if(!oper.contains("()")) count++;
                        oper = oper.replaceFirst("\\(.*\\)", String.format("(%d)", count));
                        AllOperations.add(oper);
                    }
                }

            }
            //System.out.println(AllOperations);
            String message = arrow.getAsOrSynMessage();
            int count = 0;
            for (int i = 0; i < message.length(); i++) {

                char temp = message.charAt(i);
                if (temp == ',')
                    count++;
            }
            if(!message.contains("()")) count++;
            message = message.replaceFirst("\\(.*\\)", String.format("(%d)", count));
            //System.out.println(message);
            if(!AllOperations.contains(message)){
                arrow.getAsynAndSynClassButton().setStyle("-fx-border-color: red;");
                arrow.getMainLine().setStyle("-fx-stroke-width: 2px; -fx-stroke: red");

                if(Objects.equals(arrow.getArrowType(), "Synchronous")){
                    arrow.getHeadSynch().setStyle("-fx-stroke-width: 2px; -fx-stroke: red; -fx-fill: red;");
                }else{ //asynchr
                    arrow.getHeadAsynch().setStyle("-fx-stroke-width: 2px; -fx-stroke: red");
                    arrow.getHeadAsynch2().setStyle("-fx-stroke-width: 2px; -fx-stroke: red");
                }


                //System.out.println("Neplatna funkce");
            }else{
                arrow.getAsynAndSynClassButton().setStyle("");
                arrow.getMainLine().setStyle("");
                arrow.getHeadAsynch().setStyle("");
                arrow.getHeadAsynch2().setStyle("");
                arrow.getHeadSynch().setStyle("");
                arrow.getHeadSynch2().setStyle("");
                //System.out.println("Platna funkce");
            }
        }else{
            arrow.getAsynAndSynClassButton().setStyle("");
            arrow.getMainLine().setStyle("");
            arrow.getHeadAsynch().setStyle("");
            arrow.getHeadAsynch2().setStyle("");
            arrow.getHeadSynch().setStyle("");
            arrow.getHeadSynch2().setStyle("");
            //System.out.println("Platna funkce");
        }
    }
}