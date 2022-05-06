package com.seqComponent;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.LinkedList;
import java.util.List;

/**
 * Třída pro zobrazení a editaci objektů.
 */
public class EditObjectName {

    /**
     * Tato metoda zobrazí okno pro vytváření a editaci objektů (zobrazí editovatelný aktuální stav)
     * a po stisknutí tlačítka vrátí obsah.
     * @param box Odkaz na objekt který editujeme
     * @return Vrací název objektu(pokud nějaký je) a název třídy, kterou instancuje (obsah oken)
     */
    public static List<String> displayObject(ClassWithLine box){
        List<String> content = new LinkedList<>();
        Stage objectWindow = new Stage();
        objectWindow.initModality(Modality.APPLICATION_MODAL);
        objectWindow.setTitle("Object window");

        // Inicializace komponent
        GridPane layout = new GridPane();
        Button confirm = new Button("Confirm");
        Label name = new Label("Enter Object name:");
        TextField objectText = new TextField(box.getNameObject());
        Label name2 = new Label("Enter Class name:");
        TextField classText = new TextField(box.getNameClass());

        // Velikost fontu
        confirm.setStyle("-fx-font-size:15");
        name.setStyle(("-fx-font-size:15"));
        name2.setStyle(("-fx-font-size:15"));
        objectText.setStyle(("-fx-font-size:15"));
        classText.setStyle(("-fx-font-size:15"));

        // Přidání komponent do gridpane
        layout.add(name, 0, 0);
        layout.add(objectText, 1, 0);
        layout.add(name2, 2, 0);
        layout.add(classText, 3, 0);
        layout.add(confirm, 4, 0);

        // Padding etc.
        layout.setPadding(new Insets(15,15,15,15));
        layout.setHgap(10);
        layout.setVgap(10);

        // Nové řádky a sloupce
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        ColumnConstraints column4 = new ColumnConstraints();
        ColumnConstraints column5 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();

        // Nastavení výšky a šířky
        column1.setPrefWidth(150);
        column2.setPrefWidth(200);
        column3.setPrefWidth(150);
        column4.setPrefWidth(200);
        column5.setPrefWidth(100);
        row1.setPrefHeight(40);

        // Pozicování v řádcích a sloupcích
        row1.setValignment(VPos.CENTER);
        column1.setHalignment(HPos.CENTER);
        column2.setHalignment(HPos.CENTER);
        column3.setHalignment(HPos.CENTER);
        column4.setHalignment(HPos.CENTER);
        column5.setHalignment(HPos.CENTER);

        // Horizontální roztažení
        column2.setHgrow(Priority.ALWAYS);
        column4.setHgrow(Priority.ALWAYS);

        // Přidá do layoutu
        layout.getColumnConstraints().addAll(column1, column2, column3, column4, column5);
        layout.getRowConstraints().addAll(row1);

        // Tlačítko pro potvrzení a odeslání dat (zavře okno)
        confirm.setOnAction(e->{
            objectWindow.close();
        });

        Scene scene = new Scene(layout);
        objectWindow.setScene(scene);
        objectWindow.showAndWait();

        content.add(objectText.getText());
        content.add(classText.getText());
        return content;
    }
}
