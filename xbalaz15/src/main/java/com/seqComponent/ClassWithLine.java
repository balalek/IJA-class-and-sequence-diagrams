package com.seqComponent;

import com.component.Arrow;
import com.google.gson.annotations.Expose;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.shape.Line;

public class ClassWithLine extends Group {

    @Expose
    public Double x;
    @Expose
    public Double y;
    private Double yTime;
    private ComboBox classButton = new ComboBox();
    private Button timeLineButton = new Button();
    private Line line = new Line();
    private String className;
    public ObservableList<Messages> edges = FXCollections.observableArrayList();
    private static int count = 1;
    private int ID;

    // Getters and setter
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
    public ComboBox getClassButton() {
        return classButton;
    }
    public void setClassButton(ComboBox classButton) {
        this.classButton = classButton;
    }
    public Button getTimeLineButton() {
        return timeLineButton;
    }
    public void setTimeLineButton(Button timeLineButton) {
        this.timeLineButton = timeLineButton;
    }

    // Constructor
    public ClassWithLine(Double x, Double y) {
        ID = count++;
        this.x = x;
        this.y = y;
        //classButton.setText(className);

        classButton.setLayoutX(x);
        classButton.setLayoutY(y);

        //timeLineButton.setLayoutX(x);
        //timeLineButton.setLayoutY(y - 50);

        // Appear in the cursor spike
        classButton.translateXProperty().bind(classButton.widthProperty().divide(-2));
        classButton.translateYProperty().bind(classButton.heightProperty().divide(-2));

        timeLineButton.setLayoutX(classButton.getLayoutX());
        timeLineButton.setLayoutY(classButton.getLayoutY() + 50);

        // Appear in the cursor spike
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

    public void Styling(){
        classButton.getStyleClass().setAll("sqClassBox");
        line.getStyleClass().setAll("sqLifeLine");
        timeLineButton.getStyleClass().setAll("sqTimeLineButton");
    }

    public void Update(){
        classButton.setId("ID" + ID);
        ID = count ++;
        timeLineButton.setId("ID" + ID);
        getChildren().setAll(line, classButton, timeLineButton);
    }
}
