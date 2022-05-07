/**
 * @author Josef Kuba
 */
package com.seqComponent;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Třída pro změnu typu zprávy (šipky).
 */
public class EditMessageArrows {

    /**
     * Tato metoda zobrazí okno s výběrem typu zprávy pro předanou šipku
     * @param arrow Šipka které chceme změnit typ
     * @return vrací vybraný typ
     */
    public static String display(Messages arrow){
        Stage arrowWindow = new Stage();
        arrowWindow.initModality(Modality.APPLICATION_MODAL);
        arrowWindow.setTitle("Arrow window");

        // Inicializace komponent
        GridPane layout = new GridPane();
        Button confirm = new Button("Confirm");
        Label name = new Label("Arrow type:");
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        String[] arrows = {"Synchronous", "Asynchronous", "Return"};
        choiceBox.setValue(arrow.getArrowType().toString());
        choiceBox.getItems().addAll(arrows);

        // Velikost fontu
        confirm.setStyle("-fx-font-size:15");
        name.setStyle(("-fx-font-size:15"));

        // Přidá komponenty do gridpane řádky a sloupce
        layout.add(name, 0, 0);
        layout.add(choiceBox, 1, 0);
        layout.add(confirm, 2, 0);

        // Padding etc.
        layout.setPadding(new Insets(15,15,15,15));
        layout.setHgap(10);
        layout.setVgap(10);

        // Vytvoření řádků a sloupců
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();

        // Nastavení výšky a šířky
        column1.setPrefWidth(100);
        column2.setPrefWidth(125);
        column3.setPrefWidth(100);
        row1.setPrefHeight(25);

        // Pozicování v řádcích a sloupcích
        row1.setValignment(VPos.CENTER);
        column1.setHalignment(HPos.CENTER);
        column2.setHalignment(HPos.CENTER);
        column3.setHalignment(HPos.CENTER);

        // Přidání do layoutu
        layout.getColumnConstraints().addAll(column1, column2, column3);
        layout.getRowConstraints().addAll(row1);

        // Tlačítko pro potvrzení výběru
        confirm.setOnAction(e->{
            arrowWindow.close();
        });

        Scene scene = new Scene(layout);
        arrowWindow.setScene(scene);
        arrowWindow.showAndWait();

        return choiceBox.getValue();
    }

}
