package com.project.cat201project;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class MainPageController implements Initializable
{

    @FXML private Pane pane;
    @FXML private Label audioLabel, currentTime, totalDuration;
    @FXML private ListView<String> songlist;
    @FXML private Button playBttn, pauseBttn, stopBttn, previousBttn, nextBttn, browseBttn;
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

        if(file != null)
        {
            String pathname = file.getAbsolutePath();
            fileDirectory = new File(pathname);
            files = fileDirectory.listFiles();

            for(File file_check: files)
            {
                if (file_check.getName().endsWith(".mp3") || file_check.getName().endsWith(".wav"))
                {
                    folder_valid = true;
                    break;
                }
                else
                {
                    folder_valid = false;
                }
            }

            if (files.length != 0 && folder_valid == true)
            {
                if (run)
                    stopAudio();
                audios.clear();
                songlist.getItems().clear();
                audioNum = 0;
                ObservableList<String> names = FXCollections.observableArrayList();
                for (File fil : files) {
                    if (fil.getName().endsWith("mp3") || fil.getName().endsWith("wav"))
                    {
                        audios.add(fil);
                        names.add(fil.getName().replace(".mp3", ""));
                    }
                }
                songlist.setItems(names);
                media = new Media(audios.get(audioNum).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                playAudio();
                audioLabel.setText(audios.get(audioNum).getName().replace(".mp3", ""));
            }
            else
            {
                MessageDialog();
            }
            if (!init) {
                volSlider.valueProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        mediaPlayer.setVolume(volSlider.getValue() * 0.01);
                    }
                });

                audioProgressBar.setStyle("-fx-accent: #00FF00;");
                init = true;
            }
        }
        else
        {
            ErrorMessage();
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
    private boolean run, init, folder_valid = false;
    private Duration duration;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) // play list initialization
    {
        audios = new ArrayList<File>();
        fileDirectory = new File("music");
        files = fileDirectory.listFiles();

        ObservableList<String> names = FXCollections.observableArrayList();;

        for(File file_check: files)
        {
            if (file_check.getName().endsWith(".mp3") || file_check.getName().endsWith(".wav"))
            {
                folder_valid = true;
                break;
            }
            else
            {
                folder_valid = false;
            }
        }

        if (files.length != 0 && folder_valid == true)
        {
            for (File file: files)
            {
                if(file.getName().endsWith(".mp3") || file.getName().endsWith(".wav"))
                {
                    audios.add(file);
                    names.add(file.getName().replace(".mp3", ""));
                }
            }
            songlist.setItems(names);

        media = new Media(audios.get(audioNum).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        audioLabel.setText(audios.get(audioNum).getName().replace(".mp3", ""));
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

        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable)
            {
                updateValues();
                //showTimer();
            }
        });

        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                duration = mediaPlayer.getMedia().getDuration();
                updateValues();
            }
        });

            audioProgressBar.setStyle("-fx-accent: #00FF00;");
        }
        else {
            ErrorMessage();
            audioLabel.setText("No Song");
            init = false;
        }
    }

    public void playAudio()
    {
        startTimer();
        //changeAudioSpeed(null);
        mediaPlayer.setVolume(volSlider.getValue() * 0.01);
        mediaPlayer.play();

        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable)
            {
                updateValues();
                //showTimer();
            }
        });

        mediaPlayer.setOnReady(new Runnable() {
            @Override
            public void run() {
                duration = mediaPlayer.getMedia().getDuration();
                updateValues();
            }
        });
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
            audioLabel.setText(audios.get(audioNum).getName().replace(".mp3", ""));

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
            audioLabel.setText(audios.get(audioNum).getName().replace(".mp3", ""));

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
            audioLabel.setText(audios.get(audioNum).getName().replace(".mp3", ""));

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
            audioLabel.setText(audios.get(audioNum).getName().replace(".mp3", ""));

            playAudio();
        }
    }

    public void stopAudio()
    {
        cancelTimer();
        mediaPlayer.stop();
        audioProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }
    /*
    public void repeatAudio()
    {
        mediaPlayer.setOnRepeat(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.getOnRepeat();
            }
        });
    }*/
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

        timer.scheduleAtFixedRate(task, 0,100);
    }

    public void cancelTimer()
    {
        run = false;
        timer.cancel();
    }
/*
    public void showTimer()
    {
        Duration currentPlayTime = mediaPlayer.getCurrentTime();
        duration = mediaPlayer.getMedia().getDuration();
    }
*/
    protected void updateValues()
    {
        if (totalDuration != null && duration.greaterThan(Duration.ZERO))
        {
            Platform.runLater(new Runnable()
            {
                public void run()
                {
                    Duration currentPlayTime = mediaPlayer.getCurrentTime();
                    totalDuration.setText(formatTime(currentPlayTime, duration));
                }
            });
        }
    }

    public void listClicked(MouseEvent event)
    {
        if(songlist.getSelectionModel().getSelectedItem()!=null) {
            String item = songlist.getSelectionModel().getSelectedItem();
            String item2 = audios.get(audioNum).getName().replace(".mp3", "");

            if (!(item.equals(item2))) {
                audioNum = 0;
                item2 = audios.get(audioNum).getName().replace(".mp3", "");

                while (!(item.equals(item2))) {
                    audioNum++;
                    item2 = audios.get(audioNum).getName().replace(".mp3", "");
                }
                mediaPlayer.stop();

                if (run)
                    cancelTimer();

                media = new Media(audios.get(audioNum).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                audioLabel.setText(audios.get(audioNum).getName().replace(".mp3", ""));

                playAudio();
            }
        }
    }

    public void ErrorMessage()
    {
        String message;

        message = "Error occurred. Please try again.";

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Message");
        alert.setHeaderText("Error encountered while performing current task");
        alert.setContentText(message);

        alert.showAndWait();
    }

    public void MessageDialog()
    {
        String message;

        message = "You have selected an empty folder or folder that does not contain any mp3 or wav music files. Please check and select again.";

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeight(300.0);
        alert.setWidth(600.0);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60
                    - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }
}