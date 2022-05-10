/**
 * @author Josef Kuba
 */
package com.seqComponent;

import com.component.ClassComponent;
import com.google.gson.annotations.Expose;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.shape.Polyline;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Třída reprezentuje zprávy (šipky) mezi call-boxy, časovými osami a objekty. Třída rozšiřuje třídu Group.
 * Má id počátečního a cílového objektu, typ zprávy (asynchronní, synchronní, vytvářecí, vracející), souřadnice počátku a konce.
 * Lze využít na vyznačení průběhu zpráv v sekvenčním diagramu.
 */
public class Messages extends Group {

    // Atributy
    private final double ARROWHEAD_ANGLE = Math.toRadians(20);
    private final double ARROWHEAD_LENGTH = 25;
    private Polyline mainLine = new Polyline();
    private Polyline headSynch = new Polyline();
    private Polyline headSynch2 = new Polyline();
    private Polyline headAsynch = new Polyline();
    private Polyline headAsynch2 = new Polyline();
    private Polyline headReturn = new Polyline();
    private Polyline headReturn2 = new Polyline();
    //@Expose
    public String createMessage = "<<create>>";
    private String asOrSynMessage = "";
    private String returnMessage = "";
    @Expose
    public String msg = "";
    public StringProperty property = new SimpleStringProperty();
    private StringProperty createProperty = new SimpleStringProperty();
    private StringProperty asOrSynProperty = new SimpleStringProperty();
    private StringProperty returnProperty = new SimpleStringProperty();
    private Button CreateObjectButton = new Button();
    private Button AsynAndSynClassButton = new Button();
    private Button ReturnButton = new Button();
    @Expose
    public Double x1;
    @Expose
    public Double y1;
    @Expose
    public Double x2;
    @Expose
    public String arrowType = "Synchronous";
    private Double y2;
    private String from;
    private String to;
    public static List<Messages> ListOfArrows = new LinkedList<>();

    // GETry a SETry
    public double getX1() {
        return x1;
    }
    public Double x1Property() {
        return x1;
    }
    public void setX1(double x1) {
        this.x1 = x1;
    }
    public double getY1() {
        return y1;
    }
    public Double y1Property() {
        return y1;
    }
    public void setY1(double y1) {
        this.y1 = y1;
    }
    public double getX2() {
        return x2;
    }
    public Double x2Property() {
        return x2;
    }
    public void setX2(double x2) {
        this.x2 = x2;
    }
    public double getY2() {
        return y2;
    }
    public Double y2Property() {
        return y2;
    }
    public void setY2(double y2) {
        this.y2 = y2;
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
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
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

    /**
     * Kontruktor pro vytvoření zprávy (šipky) a napozicování obsahu zprávy
     * @param x1 X souřadnice začátku čáry
     * @param y1 Y souřadnice začátku i konce čáry
     * @param x2 X souřadnice konce čáry
     */
    public Messages(double x1, double y1, double x2) {
        ListOfArrows.add(this);
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y1;
        mainLine.getPoints().setAll(getX1(), getY1(), getX2(), getY2());

        // Jaktaž prostředek čáry
        AsynAndSynClassButton.setLayoutX(((x2 + x1) / 2));
        // Trochu nad čáru
        AsynAndSynClassButton.setLayoutY(y1 - 15);
        // Vykreslí se tak aby získané souřadnice byly ve středu
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

    /**
     * Kontruktor pro načtení zprávy (šipky) a napozicování obsahu zprávy
     * @param x1 X souřadnice začátku čáry
     * @param y1 Y souřadnice začátku i konce čáry
     * @param x2 X souřadnice konce čáry
     */
    public Messages(double x1, double y1, double x2, String arrowType, String msg) {
        ListOfArrows.add(this);
        this.arrowType = arrowType;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y1;
        this.msg = msg;
        mainLine.getPoints().setAll(getX1(), getY1(), getX2(), getY2());

        // Jaktaž prostředek čáry
        AsynAndSynClassButton.setLayoutX(((x2 + x1) / 2));
        // Trochu nad čáru
        AsynAndSynClassButton.setLayoutY(y1 - 15);
        // Vykreslí se tak aby získané souřadnice byly ve středu
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

    /**
     * Nastavení css stylů
     */
    public void arrowHeadAndBoxStyles() {
        mainLine.getStyleClass().setAll("mainLine");
        AsynAndSynClassButton.getStyleClass().setAll("comboBox");
        ReturnButton.getStyleClass().setAll("comboBox");
        CreateObjectButton.getStyleClass().setAll("comboBox");
        headSynch.getStyleClass().setAll("blackArrowHead");
        headAsynch.getStyleClass().setAll("normalArrowHead");
        headReturn.getStyleClass().setAll("dashedLineArrow");
    }

    /**
     * Vykreslí danou šipku a obsah zprávy uprostřed šipky při vytvoření na místa, na které se kliklo
     */
    public void update() {
        mainLine.getPoints().setAll(getX1(), getY1(), getX2(), getY2());
        double[] start = scale(getX1(), getY1(), getX2(), getY2());
        double[] end = scale(getX2(), getY2(), getX1(), getY1());
        double x1 = start[0];
        double y1 = start[1];
        double x2 = end[0];
        double y2 = end[1];

        /*this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;*/

        double theta = Math.atan2(y2 - y1, x2 - x1);
        toBack();

        // Kreslení čáry s hlavičkou podle typu šipky
        switch (getArrowType()) {
            // Asynchronní zpráva
            case "Asynchronous": {
                getChildren().removeAll(headAsynch, headReturn, headSynch, AsynAndSynClassButton, CreateObjectButton, ReturnButton);
                mainLine.getStyleClass().setAll("mainLine");
                mainLine.getStyleClass().remove("dashedMainLine");
                // Kreslení hlavičky
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAsynch.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headAsynch.getPoints().addAll(x, y);

                // Aktualizovatelný nápis v boxu
                Label myLabel = new Label();
                msg = asOrSynMessage;
                asOrSynProperty.setValue(msg);
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
                // Kreslení hlavičky
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headSynch.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headSynch.getPoints().addAll(x, y);

                Label myLabel = new Label();
                msg = asOrSynMessage + getMsg();
                asOrSynProperty.setValue(msg);
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
                // Kreslení hlavičky
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headReturn.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headReturn.getPoints().addAll(x, y);

                Label myLabel = new Label();
                msg = returnMessage + getMsg();
                returnProperty.setValue(msg);
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
                // Kreslení hlavičky
                double x = x2 - Math.cos(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                double y = y2 - Math.sin(theta + ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headReturn.getPoints().setAll(x, y, x2, y2);
                x = x2 - Math.cos(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                y = y2 - Math.sin(theta - ARROWHEAD_ANGLE) * ARROWHEAD_LENGTH;
                headReturn.getPoints().addAll(x, y);

                Label myLabel = new Label();
                //msg = createMessage
                if(msg.contains("<<create>>")) msg = getMsg();
                else msg = createMessage + getMsg();
                createProperty.setValue(msg);
                myLabel.textProperty().bind(createProperty);
                CreateObjectButton.setGraphic(myLabel);

                getChildren().setAll(mainLine, headReturn, CreateObjectButton);
                break;
            }
        }
    }

    /**
     * Metoda scale se používá k získání souřadnic (x,y) kde šipka začíná a končí
     * @param x1 X souřadnice začátku
     * @param y1 Y souřadnice začátku
     * @param x2 X souřadnice konce
     * @param y2 Y souřadnice konce
     * @return Vrací souřadnice kde šipka začíná a končí
     */
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
            if(Objects.equals(arrow.getAsOrSynMessage(), "")){
                arrow.setAsOrSynMessage(arrow.getMsg());
            }
            List<String> AllOperations = new LinkedList<>();
            for (ClassComponent box: ClassComponent.getListofBoxes()) {
                if(!Objects.equals(box.getOperations(), "")){
                    String  Operations[] = box.getOperations().split("\n");
                    for (String oper: Operations) {
                        if(oper.indexOf(" ", oper.indexOf(" ") + 1) < oper.indexOf("("))
                        {
                            //Normální metoda
                            oper = (oper.split(" ", 3)[2]);

                        }else{
                            //Konstruktor
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
            String message = arrow.getAsOrSynMessage();
            int count = 0;
            for (int i = 0; i < message.length(); i++) {

                char temp = message.charAt(i);
                if (temp == ',')
                    count++;
            }
            if(!message.contains("()")) count++;
            message = message.replaceFirst("\\(.*\\)", String.format("(%d)", count));
            System.out.println("AllOperations: " + AllOperations);
            System.out.println("Puvodni zpava syn: " + "\"" + arrow.getAsOrSynMessage() + "\"");
            System.out.println("Zkompilovana zprava: " + message);
            System.out.println(arrow.getMsg());

            if(!AllOperations.contains(message)){
                arrow.getAsynAndSynClassButton().setStyle("-fx-border-color: red;");
                arrow.getMainLine().setStyle("-fx-stroke-width: 2px; -fx-stroke: red");

                if(Objects.equals(arrow.getArrowType(), "Synchronous")){
                    arrow.getHeadSynch().setStyle("-fx-stroke-width: 2px; -fx-stroke: red; -fx-fill: red;");
                }else{ //asynchr
                    arrow.getHeadAsynch().setStyle("-fx-stroke-width: 2px; -fx-stroke: red");
                    arrow.getHeadAsynch2().setStyle("-fx-stroke-width: 2px; -fx-stroke: red");
                }
            }else{
                arrow.getAsynAndSynClassButton().setStyle("");
                arrow.getMainLine().setStyle("");
                arrow.getHeadAsynch().setStyle("");
                arrow.getHeadAsynch2().setStyle("");
                arrow.getHeadSynch().setStyle("");
                arrow.getHeadSynch2().setStyle("");
            }
        }else{
            arrow.getAsynAndSynClassButton().setStyle("");
            arrow.getMainLine().setStyle("");
            arrow.getHeadAsynch().setStyle("");
            arrow.getHeadAsynch2().setStyle("");
            arrow.getHeadSynch().setStyle("");
            arrow.getHeadSynch2().setStyle("");
        }
    }
}