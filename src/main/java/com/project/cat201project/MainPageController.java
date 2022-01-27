package com.project.cat201project;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class MainPageController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML private Pane pane;
    @FXML private Label songLabel;
    @FXML private Button playBttn, pauseBttn, resetBttn, previousBttn, nextBttn;
    @FXML private ComboBox<String> speed;
    @FXML private Slider volume;
    @FXML private ProgressBar songProgressBar;

}