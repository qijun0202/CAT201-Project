package com.project.cat201project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainPageController implements Initializable
{

    @FXML private Pane pane;
    @FXML private Label audioLabel;
    @FXML private Button playBttn, pauseBttn, resetBttn, previousBttn, nextBttn;
    @FXML private ComboBox<String> speed;
    @FXML private Slider volume;
    @FXML private ProgressBar audioProgressBar;
    @FXML private AnchorPane anchorId;

    @FXML
    private void openDirectoryChooser(ActionEvent event)
    {
        final DirectoryChooser dirChooser = new DirectoryChooser();
        Stage stage = (Stage) anchorId.getScene().getWindow();
        File file = dirChooser.showDialog(stage);

        if(file != null)
        {
            System.out.println("Path: " + file.getAbsolutePath());
        }
    }

    private Media media;
    private MediaPlayer mediaPlayer;

    private File fileDirectory;
    private File[] files;
    private ArrayList<File> audioFiles;

    private int audioNum;
    private int[] audioSpeeds = {25, 50, 75, 100, 125, 150, 175, 200};

    private Timer timer;
    private TimerTask task;
    private boolean run;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) // play list initialization
    {
        audioFiles = new ArrayList<File>();
        fileDirectory = new File("music");
        files = fileDirectory.listFiles();

        if (files != null)
        {
            for (File file: files)
            {
                audioFiles.add(file);
            }
        }

        media = new Media(audioFiles.get(audioNum).toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        audioLabel.setText(audioFiles.get(audioNum).getName());
    }
}