/**
 * @author Josef Kuba
 */
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
     * Metoda zobrazí nápovědu v novým okně s tlačítkem na ukončení.
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
        column1.setPrefWidth(860);
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
    /**
     * Nápověda
     * @return Text, který se vypíše do okna
     */
    private static String textBlocks() {
        return  "Note: Clicking with mouse is meant for left mouse button, unless its stated otherwise.\n" +
                "Usage: To create a class, you must click on button called Add Object. Use Clear to delete everything.\n" +
                "and then click somewhere on panel. That will create an empty object box with time line and call-box.\n" +
                "For object name double-click on it, and type name of Class that is created in Class diagram already." +
                "You can move with objects by dragging them with left mouse button to desired position.\n" +
                "If you select call-box, you can make it smaller or bigger with keyboard keys: W, S.\n" +
                "You can also move with call-box using mouse drag. For object to be deleted use key: DELETE. \n" +
                "For object to extinct you must click on object with middle mouse button, and for undo double-middle-click\n" +
                "Next step is creating messages between call-boxes and time lines/objects and that is achieved by\n" +
                "clicking right mouse button on them. Default message is Synchronous, if you want to change\n" +
                "message type, double click on line and pick one of these: Asynchronous, Return, synchronous\n" +
                "If you want to create Create message you must create line between call-box and object.\n" +
                "If you create a message line, you must also write a text to it, by double-clicking on grey rectangle.\n" +
                "For message to be deleted, right click on it. For saving to a file and loading from a file, you may use Save\n" +
                "and Load buttons. And lastly, if you want to create more Sequence diagrams for this class diagram,\n" +
                "select different tab in tab pane, and if you want to see Class diagram click on Class diagram button.\n\n" +
                "Syntax for text fields -> Object has its name(or doesn't) and name of class of which he is instance of.\n" +
                "Asynchronous and Synchronous messages can have method name and parameters -> rename(c1, c2)\n" +
                "Create message must start with <<create>> and whatever after it. Return message has no limitation for syntax.";
    }
}
