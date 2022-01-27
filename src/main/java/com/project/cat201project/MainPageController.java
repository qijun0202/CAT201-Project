package com.project.cat201project;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainPageController{
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

    private File fileDirectory;
    private File[] files;
    private ArrayList<File> audioFiles;

    private int audioNum;
    private int[] audioSpeeds = {25, 50, 75, 100, 125, 150, 175, 200};

    private Timer timer;
    private TimerTask task;
    private boolean run;

    public void initializePlayList(URL x, ResourceBundle y)
    {
        ArrayList<File> audios = new ArrayList<File>();
        fileDirectory = new File("audio");
        files = fileDirectory.listFiles();
    }
}