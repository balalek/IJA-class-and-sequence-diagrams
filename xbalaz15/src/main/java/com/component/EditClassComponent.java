package com.component;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class EditClassComponent {

    public static List<Object> display(ClassComponent classComponent){
        List<Object> content = new LinkedList<>();
        Stage classWindow = new Stage();
        classWindow.initModality(Modality.APPLICATION_MODAL);
        classWindow.setTitle("Class window");

        // Init components
        GridPane layout = new GridPane();
        GridPane radioLayout = new GridPane();
        Button confirm = new Button("Confirm");
        Label name = new Label("Name:");
        Label attrs = new Label("Attributes:");
        Label methods = new Label("Methods:");
        TextField nameText = new TextField(classComponent.getName());
        TextArea attrsText = new TextArea(classComponent.getAttributes());
        TextArea operationText = new TextArea(classComponent.getOperations());
        ToggleGroup tg = new ToggleGroup();
        RadioButton normal = new RadioButton("Normal");
        RadioButton abstractClass = new RadioButton("Abstract");
        RadioButton interfaceClass = new RadioButton("Interface");

        // Add radio buttons to toggle group
        normal.setToggleGroup(tg);
        abstractClass.setToggleGroup(tg);
        interfaceClass.setToggleGroup(tg);

        // Default is normal class
        normal.setSelected(classComponent.getNormal());
        abstractClass.setSelected(classComponent.getAbstractClass());
        interfaceClass.setSelected(classComponent.getInterface());

        // Font sizes
        confirm.setStyle("-fx-font-size:15");
        name.setStyle(("-fx-font-size:15"));
        attrs.setStyle(("-fx-font-size:15"));
        methods.setStyle(("-fx-font-size:15"));

        // Add components to a specific gridpane rows and columns
        layout.add(name, 0, 0);
        layout.add(attrs, 0, 1);
        layout.add(methods, 0, 2);
        layout.add(nameText, 1, 0);
        layout.add(attrsText, 1, 1);
        layout.add(operationText, 1, 2);
        layout.add(radioLayout, 1, 3);
        radioLayout.add(normal, 0, 0);
        radioLayout.add(abstractClass, 1, 0);
        radioLayout.add(interfaceClass, 2, 0);
        radioLayout.add(confirm, 3, 0);

        // Padding etc.
        layout.setPadding(new Insets(15,15,15,15));
        layout.setHgap(10);
        layout.setVgap(10);
        radioLayout.setPadding(new Insets(15,15,15,15));
        radioLayout.setHgap(10);
        radioLayout.setVgap(10);


        // New row a column objects
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        RowConstraints row4 = new RowConstraints();

        ColumnConstraints columnRadio1 = new ColumnConstraints();
        ColumnConstraints columnRadio2 = new ColumnConstraints();
        ColumnConstraints columnRadio3 = new ColumnConstraints();
        ColumnConstraints columnRadio4 = new ColumnConstraints();
        RowConstraints rowRadio1 = new RowConstraints();

        // Set height and width
        column1.setPrefWidth(100);
        column2.setPrefWidth(500);
        columnRadio1.setPrefWidth(125);
        columnRadio2.setPrefWidth(125);
        columnRadio3.setPrefWidth(125);
        columnRadio4.setPrefWidth(125);
        row1.setPrefHeight(25);
        row4.setPrefHeight(50);

        rowRadio1.setPrefHeight(25);

        // Positioning in rows and columns
        row1.setValignment(VPos.TOP);
        row2.setValignment(VPos.TOP);
        row3.setValignment(VPos.TOP);
        column1.setHalignment(HPos.RIGHT);
        column2.setHalignment(HPos.RIGHT);

        columnRadio4.setHalignment(HPos.RIGHT);

        // Horizontal stretch by window size
        column2.setHgrow(Priority.ALWAYS);

        // Add to layout
        layout.getColumnConstraints().addAll(column1, column2);
        layout.getRowConstraints().addAll(row1, row2, row3);
        radioLayout.getColumnConstraints().addAll(columnRadio1, columnRadio2, columnRadio3, columnRadio4);
        radioLayout.getRowConstraints().add(rowRadio1);

        // Button action
        confirm.setOnAction(e->{
            classWindow.close();
        });

        Scene scene = new Scene(layout);
        classWindow.setScene(scene);
        classWindow.showAndWait();

        content.add(nameText.getText());
        content.add(attrsText.getText());
        content.add(operationText.getText());
        if(normal.isSelected()) content.add("");
        else if(abstractClass.isSelected()) content.add("<<Abstract>>");
        else if(interfaceClass.isSelected()) content.add("<<Interface>>");
        return content;
    }
}