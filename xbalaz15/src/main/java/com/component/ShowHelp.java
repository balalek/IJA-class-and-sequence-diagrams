/**
 * @author Martin Baláž
 */
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
     * Metoda zobrazí nápovědu v novým okně s tlačítkem na ukončení.
     */
    public static void display(){
        Stage helpWindow = new Stage();
        helpWindow.initModality(Modality.APPLICATION_MODAL);
        helpWindow.setTitle("Help window");

        // Inicializace komponent
        GridPane layout = new GridPane();
        Button confirm = new Button("Understood");
        Label help = new Label(textBlocks());

        // Velikost fontu
        confirm.setStyle("-fx-font-size:15");
        help.setStyle(("-fx-font-size:15"));

        // Přidání komponent do gridpane
        layout.add(help, 0, 0);
        layout.add(confirm, 0, 1);

        // Padding
        layout.setPadding(new Insets(15,15,15,15));
        layout.setHgap(10);
        layout.setVgap(10);

        // Nový sloupec a řádky
        ColumnConstraints column1 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();

        // Nastavení výšky a šířky
        column1.setPrefWidth(860);
        row2.setPrefHeight(25);

        // Pozicování v řádcích a sloupcích
        row1.setValignment(VPos.CENTER);
        row2.setValignment(VPos.CENTER);
        column1.setHalignment(HPos.CENTER);

        // Přidání do layout
        layout.getColumnConstraints().addAll(column1);
        layout.getRowConstraints().addAll(row1, row2);

        // Tlačítko které zavře okno nápovědy
        confirm.setOnAction(e->{
            helpWindow.close();
        });

        Scene scene = new Scene(layout);
        helpWindow.setScene(scene);
        helpWindow.showAndWait();
    }

    /**
     * Nápověda
     * @return Text, který se vypíše do okna
     */
    private static String textBlocks() {
        return  "Note: Clicking with mouse is meant for left mouse button, unless its stated otherwise.\n" +
                "Usage: To create a class, you must click on button called Insert Class\n" +
                "and then click somewhere on panel. That will create an empty class box.\n" +
                "You can move with classes by dragging them to desired position.\n" +
                "For class to be deleted use key: DELETE. To update a class content you must double click on class.\n" +
                "You may update name, attributes, operations and also select class type(normal, abstract or interface)\n" +
                "Next step is creating relation between classes, and that is achieved by clicking right mouse button\n" +
                "on two separate classes. Default relation is association, if you want to change relation type,\n" +
                "double click on line and pick one of these: aggregation, composition or generalization.\n" +
                "For line (relation) to be deleted, right click on it.\n" +
                "If you wish to undo your move, simply click on Undo button.\n" +
                "For saving to a file and loading from a file, you may use Save and Load buttons.\n" +
                "And lastly, if you want to see Sequence diagram for this class diagram, click on Sequence diagram button.\n\n" +
                "Syntax for Class boxes -> Class has it's generated name, or you can rename it however you wish.\n" +
                "Supported types are Primitive data type, Class name as type or List<anything>\n" +
                "Attributes have following syntax: [[+|-|#|~][Type][name]]\n" +
                "Operations: [[+|-|#|~][Type][name]([_|Type name[, Type name[, ...]]])]\n" +
                "But Operation can also be a constructor: [+|-|#|~][this class name]([_|Type name[, Type name[, ...]]])";
    }
}
