package com.javaprojekt;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ClassDiagramController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}