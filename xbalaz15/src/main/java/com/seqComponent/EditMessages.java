package com.seqComponent;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EditMessages {
    public static String displayCreate(Messages createMessage){
        String returnCreate;
        Stage classWindow = new Stage();
        classWindow.initModality(Modality.APPLICATION_MODAL);
        classWindow.setTitle("Create message window");

        // Inicializace komponent
        GridPane layout = new GridPane();
        Button confirm = new Button("Confirm");
        Label name = new Label("Enter create message:");
        TextField createText = new TextField(createMessage.getCreateMessage());

        // Velikost fontu
        confirm.setStyle("-fx-font-size:15");
        name.setStyle(("-fx-font-size:15"));
        createText.setStyle(("-fx-font-size:15"));

        // Přidání komponent do gridpane
        layout.add(name, 0, 0);
        layout.add(createText, 1, 0);
        layout.add(confirm, 2, 0);

        // Padding etc.
        layout.setPadding(new Insets(15,15,15,15));
        layout.setHgap(10);
        layout.setVgap(10);

        // Nové řádky a sloupce
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();

        // Nastavení výšky a šířky
        column1.setPrefWidth(200);
        column2.setPrefWidth(200);
        column3.setPrefWidth(100);
        row1.setPrefHeight(40);

        // Pozicování v řádcích a sloupcích
        row1.setValignment(VPos.CENTER);
        column1.setHalignment(HPos.CENTER);
        column2.setHalignment(HPos.CENTER);
        column3.setHalignment(HPos.CENTER);

        // Horizontální roztažení
        column2.setHgrow(Priority.ALWAYS);

        // Přidá do layoutu
        layout.getColumnConstraints().addAll(column1, column2, column3);
        layout.getRowConstraints().addAll(row1);

        // Tlačítko pro potvrzení a odeslání dat (zavře okno)
        confirm.setOnAction(e->{
            classWindow.close();
        });

        Scene scene = new Scene(layout);
        classWindow.setScene(scene);
        classWindow.showAndWait();

        returnCreate = createText.getText();
        return returnCreate;
    }

    public static String displayAsynchOrSynch(Messages asOrSynMessage){
        String returnAsynchOrSynch;
        Stage classWindow = new Stage();
        classWindow.initModality(Modality.APPLICATION_MODAL);
        classWindow.setTitle("A/Synchronous message window");

        // Inicializace komponent
        GridPane layout = new GridPane();
        Button confirm = new Button("Confirm");
        Label name = new Label("Enter A/Synchronous message:");
        TextField asynOrSynText = new TextField(asOrSynMessage.getAsOrSynMessage());

        // Velikost fontu
        confirm.setStyle("-fx-font-size:15");
        name.setStyle(("-fx-font-size:15"));
        asynOrSynText.setStyle(("-fx-font-size:15"));

        // Přidání komponent do gridpane
        layout.add(name, 0, 0);
        layout.add(asynOrSynText, 1, 0);
        layout.add(confirm, 2, 0);

        // Padding etc.
        layout.setPadding(new Insets(15,15,15,15));
        layout.setHgap(10);
        layout.setVgap(10);

        // Nové řádky a sloupce
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();

        // Nastavení výšky a šířky
        column1.setPrefWidth(250);
        column2.setPrefWidth(200);
        column3.setPrefWidth(100);
        row1.setPrefHeight(40);

        // Pozicování v řádcích a sloupcích
        row1.setValignment(VPos.CENTER);
        column1.setHalignment(HPos.CENTER);
        column2.setHalignment(HPos.CENTER);
        column3.setHalignment(HPos.CENTER);

        // Horizontální roztažení
        column2.setHgrow(Priority.ALWAYS);

        // Přidá do layoutu
        layout.getColumnConstraints().addAll(column1, column2, column3);
        layout.getRowConstraints().addAll(row1);

        // Tlačítko pro potvrzení a odeslání dat (zavře okno)
        confirm.setOnAction(e->{
            classWindow.close();
        });

        Scene scene = new Scene(layout);
        classWindow.setScene(scene);
        classWindow.showAndWait();

        returnAsynchOrSynch = asynOrSynText.getText();
        return returnAsynchOrSynch;
    }


    public static String displayReturn(Messages returnMessage){
        String returnReturn;
        Stage classWindow = new Stage();
        classWindow.initModality(Modality.APPLICATION_MODAL);
        classWindow.setTitle("Create message window");

        // Inicializace komponent
        GridPane layout = new GridPane();
        Button confirm = new Button("Confirm");
        Label name = new Label("Enter Return message:");
        TextField returnText = new TextField(returnMessage.getReturnMessage());

        // Velikost fontu
        confirm.setStyle("-fx-font-size:15");
        name.setStyle(("-fx-font-size:15"));
        returnText.setStyle(("-fx-font-size:15"));

        // Přidání komponent do gridpane
        layout.add(name, 0, 0);
        layout.add(returnText, 1, 0);
        layout.add(confirm, 2, 0);

        // Padding etc.
        layout.setPadding(new Insets(15,15,15,15));
        layout.setHgap(10);
        layout.setVgap(10);

        // Nové řádky a sloupce
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        RowConstraints row1 = new RowConstraints();

        // Nastavení výšky a šířky
        column1.setPrefWidth(200);
        column2.setPrefWidth(200);
        column3.setPrefWidth(100);
        row1.setPrefHeight(40);

        // Pozicování v řádcích a sloupcích
        row1.setValignment(VPos.CENTER);
        column1.setHalignment(HPos.CENTER);
        column2.setHalignment(HPos.CENTER);
        column3.setHalignment(HPos.CENTER);

        // Horizontální roztažení
        column2.setHgrow(Priority.ALWAYS);

        // Přidá do layoutu
        layout.getColumnConstraints().addAll(column1, column2, column3);
        layout.getRowConstraints().addAll(row1);

        // Tlačítko pro potvrzení a odeslání dat (zavře okno)
        confirm.setOnAction(e->{
            classWindow.close();
        });

        Scene scene = new Scene(layout);
        classWindow.setScene(scene);
        classWindow.showAndWait();

        returnReturn = returnText.getText();
        return returnReturn;
    }
}