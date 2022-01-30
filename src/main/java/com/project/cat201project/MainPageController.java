package com.project.cat201project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainPageController implements Initializable
{

    @FXML private Pane pane;
    @FXML private Label audioLabel, currentTime, totalDuration;
    @FXML private Button playBttn, pauseBttn, resetBttn, previousBttn, nextBttn, browseBttn;
    //@FXML private ComboBox<String> speedBox;
    @FXML private Slider volSlider;
    @FXML private ProgressBar audioProgressBar;
    //@FXML private Duration duration;

    @FXML
    private void openDirectoryChooser(ActionEvent event)
    {
        final DirectoryChooser dirChooser = new DirectoryChooser();
        Stage stage = (Stage) browseBttn.getScene().getWindow();
        File file = dirChooser.showDialog(stage);


        String pathname = file.getAbsolutePath();
        fileDirectory = new File(pathname);
        files = fileDirectory.listFiles();

        if (files != null)
        {
            audios.clear();
            for (File fil : files)
            {
                audios.add(fil);
            }
            audioLabel.setText(audios.get(audioNum).getName());
        }
    }

    private Media media;
    private MediaPlayer mediaPlayer;

    private File fileDirectory;
    private File[] files;
    private ArrayList<File> audios;

    private int audioNum;
    private double[] audioSpeeds = {0.25, 0.50, 0.75, 1.00, 1.25, 1.50, 1.75, 2.00};

    private Timer timer;
    private TimerTask task;
    private boolean run;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) // play list initialization
    {
        audios = new ArrayList<File>();
        fileDirectory = new File("music");
        files = fileDirectory.listFiles();

        if (files != null)
        {
            for (File file: files)
            {
                audios.add(file);
            }
        }

        media = new Media(audios.get(audioNum).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        audioLabel.setText(audios.get(audioNum).getName());
/*

        for(int i = 0; i < audioSpeeds.length; i++)
        {
            speedBox.getItems().add(Double.toString(audioSpeeds[i]));
        }

        speedBox.setOnAction(this::changeAudioSpeed);
*/

        volSlider.valueProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1)
            {
                mediaPlayer.setVolume(volSlider.getValue() * 0.01);
            }
        });

        audioProgressBar.setStyle("-fx-accent: #00FF00;");
    }

    public void playAudio()
    {
        startTimer();
        //changeAudioSpeed(null);
        mediaPlayer.setVolume(volSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    public void pauseAudio()
    {
        cancelTimer();
        mediaPlayer.pause();
    }

    public void previousAudio()
    {
        if(audioNum > 0)
        {
            audioNum--;
            mediaPlayer.stop();

            if(run)
                cancelTimer();

            media = new Media(audios.get(audioNum).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            audioLabel.setText(audios.get(audioNum).getName());

            playAudio();
        }
        else
        {
            audioNum = audios.size() - 1;
            mediaPlayer.stop();

            if(run)
                cancelTimer();

            media = new Media(audios.get(audioNum).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            audioLabel.setText(audios.get(audioNum).getName());

            playAudio();
        }
    }

    public void nextAudio()
    {
        if(audioNum < audios.size() - 1)
        {
            audioNum++;
            mediaPlayer.stop();

            if(run)
                cancelTimer();

            media = new Media(audios.get(audioNum).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            audioLabel.setText(audios.get(audioNum).getName());

            playAudio();
        }
        else
        {
            audioNum = 0;
            mediaPlayer.stop();

            if(run)
                cancelTimer();

            media = new Media(audios.get(audioNum).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            audioLabel.setText(audios.get(audioNum).getName());

            playAudio();
        }
    }

    public void stopAudio()
    {
        audioProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }
/*
    public void changeAudioSpeed(ActionEvent event)
    {
        if(speedBox.getValue() == null)
        {
            mediaPlayer.setRate(1);
        }
        else
        {
            mediaPlayer.setRate(Integer.parseInt(speedBox.getValue()));
        }

    }
*/
    public void startTimer()
    {
        timer = new Timer();
        task = new TimerTask()
        {
            public void run()
            {
                run = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                audioProgressBar.setProgress(current/end);

                if (current/end == 1)
                    cancelTimer();
            }
        };

        timer.scheduleAtFixedRate(task, 0,1000);
    }

    public void cancelTimer()
    {
        run = false;
        timer.cancel();
    }
/*
    public ArrayList<File> findSong (File file)
    {
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();
        for (File singlefile: files)
        {
            if (singlefile.isDirectory() && !singlefile.isHidden())
            {
                arrayList.addAll(findSong(singlefile));
            }
            else
            {
                if (singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav"))
                {
                    arrayList.add(singlefile);
                }
            }
        }
        return arrayList;
    }

    void displaySongs()
    {
        final ArrayList<File> mySongs = findSong()
    }
*/
}