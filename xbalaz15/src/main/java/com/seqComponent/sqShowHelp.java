package com.seqComponent;

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
public class sqShowHelp {
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

    // TODO DESTRUCT message
    private static String textBlocks() {
        return  "Note: Clicking with mouse is meant for left mouse button, unless its stated otherwise.\n" +
                "Usage: To create a class, you must click on button called Add Class\n" +
                "and then click somewhere on panel. That will create an empty class box with time line and call-box.\n" +
                "You may select class that is in Class diagram by clicking on it.\n" +
                "You can move with classes by dragging them with middle mouse button to desired position.\n" +
                "If you select call-box, you can make it smaller or bigger with keyboard keys: W, S.\n" +
                "You can also move with call-box using mouse drag. For class to be deleted use key: DELETE. \n" +
                "Next step is creating messages between call-boxes and time lines/objects and that is achieved by\n" +
                "clicking right mouse button on them. Default message is Synchronous, if you want to change\n" +
                "message type, double click on line and pick one of these: Asynchronous, Return, Create.\n" +
                "For message to be deleted, right click on it.\n" +
                "If you wish to undo your move, simply click on Undo button.\n" +
                "For saving to a file and loading from a file, you may use Save and Load buttons.\n" +
                "And lastly, if you want to create more Sequence diagrams for this class diagram,\n" +
                "select different tab in tab pane, and if you want to see Class diagram click on Class diagram button.";

    }
}
