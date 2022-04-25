package com.component;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

public class EditClassComponent {
    //private List<String> content = new LinkedList<String>();

    public static List<String> display(ClassComponent classComponent){
        List<String> content = new LinkedList<String>();
        Stage classWindow = new Stage();
        classWindow.initModality(Modality.APPLICATION_MODAL);
        classWindow.setTitle("Class window");

        // Init components
        GridPane layout = new GridPane();
        Button confirm = new Button("Confirm");
        Label name = new Label("Name:");
        Label attrs = new Label("Attributes:");
        Label methods = new Label("Methods:");
        TextField nameText = new TextField(classComponent.getName());
        TextArea attrsText = new TextArea();
        TextArea methodsText = new TextArea();

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
        layout.add(methodsText, 1, 2);
        layout.add(confirm, 1, 3);

        // Padding etc.
        layout.setPadding(new Insets(15,15,15,15));
        layout.setHgap(10);
        layout.setVgap(10);

        // New row a column objects
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        RowConstraints row3 = new RowConstraints();
        RowConstraints row4 = new RowConstraints();

        // Set height and width
        column1.setPrefWidth(100);
        column2.setPrefWidth(500);
        row1.setPrefHeight(25);
        row4.setPrefHeight(50);

        // Positioning in rows and columns
        row1.setValignment(VPos.TOP);
        row2.setValignment(VPos.TOP);
        row3.setValignment(VPos.TOP);
        column1.setHalignment(HPos.RIGHT);
        column2.setHalignment(HPos.RIGHT);

        // Horizontal stretch by window size
        column2.setHgrow(Priority.ALWAYS);

        // Add to layout
        layout.getColumnConstraints().addAll(column1, column2);
        layout.getRowConstraints().addAll(row1, row2, row3, row4);

        // Button action
        confirm.setOnAction(e->{
            classWindow.close();
        });

        Scene scene = new Scene(layout);
        classWindow.setScene(scene);
        classWindow.showAndWait();

        content.add(nameText.getText());
        return content;
    }
}