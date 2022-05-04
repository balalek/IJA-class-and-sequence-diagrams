package com.component;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Třída pro vyvolání nápovědy
 */
public class ShowHelp {
    /**
     *
     */
    public static void display(){
        Stage helpWindow = new Stage();
        helpWindow.initModality(Modality.APPLICATION_MODAL);
        helpWindow.setTitle("Help window");

        // Init components
        GridPane layout = new GridPane();
        Button confirm = new Button("Understood");
        Label help = new Label(textBlocks());

        // Font sizes
        confirm.setStyle("-fx-font-size:15");
        help.setStyle(("-fx-font-size:15"));

        // Add components to a specific gridpane rows and columns
        layout.add(help, 0, 0);
        layout.add(confirm, 0, 1);

        // Padding etc.
        layout.setPadding(new Insets(15,15,15,15));
        layout.setHgap(10);
        layout.setVgap(10);

        // New row a column objects
        ColumnConstraints column1 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();

        // Set height and width
        column1.setPrefWidth(810);
        row2.setPrefHeight(25);

        // Positioning in rows and columns
        row1.setValignment(VPos.CENTER);
        row2.setValignment(VPos.CENTER);
        column1.setHalignment(HPos.CENTER);

        // Add to layout
        layout.getColumnConstraints().addAll(column1);
        layout.getRowConstraints().addAll(row1, row2);

        // Button action
        confirm.setOnAction(e->{
            helpWindow.close();
        });

        Scene scene = new Scene(layout);
        helpWindow.setScene(scene);
        helpWindow.showAndWait();
    }

    private static String textBlocks() {
        String help = " Note: Clicking with mouse works only for left mouse button, unless its stated otherwise.\n" +
        "Note: Clicking with mouse works only for left mouse button, unless its stated otherwise.\n" +
        "Usage: To create a class, you must click on button called Insert Class\n" +
        "and then click somewhere on panel. That will create an empty class box.\n" +
        "You can move with classes by clicking on them and dragging them to desired position.\n" +
        "You can also use following keys: W, A, S, D, but first select class box by clicking on it.\n" +
        "For class to be deleted use key: DELETE. To update a class content you must double click on class.\n" +
        "Next step is creating relation between classes, and that is achieved by clicking right mouse button\n" +
        "on two separate classes. Default relation is association, if you want to change relation type,\n" +
        "double click on line and pick one of these: aggregation, composition or generalization.\n" +
        "For line (relation) to be deleted, right click on it.\n" +
        "If you wish to undo your move, simply click on Undo button.\n" +
        "For saving to a file and loading from a file, you may use Save and Load buttons.\n" +
        "And lastly, if you want to see Sequence diagram for this class diagram, click on Sequence diagram button.";
        return help;

    }
}
