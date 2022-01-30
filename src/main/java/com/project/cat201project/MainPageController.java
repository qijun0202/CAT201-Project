package com.project.cat201project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
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

            if (files.length != 0)
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
                        names.add(fil.getName());
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
    private boolean run ,init;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) // play list initialization
    {
        audios = new ArrayList<File>();
        fileDirectory = new File("music");
        files = fileDirectory.listFiles();

        ObservableList<String> names = FXCollections.observableArrayList();;
        if (files.length != 0)
        {
            for (File file: files)
            {
                audios.add(file);
                names.add(file.getName());
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

            audioProgressBar.setStyle("-fx-accent: #00FF00;");
        }
        else {
            ErrorMessage(1);
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

    public void listClicked(MouseEvent event)
    {
        if(songlist.getSelectionModel().getSelectedItem()!=null) {
            String item = songlist.getSelectionModel().getSelectedItem();
            String item2 = audios.get(audioNum).getName();

            if (!(item.equals(item2))) {
                audioNum = 0;
                item2 = audios.get(audioNum).getName();

                while (!(item.equals(item2))) {
                    audioNum++;
                    item2 = audios.get(audioNum).getName();
                }
                mediaPlayer.stop();

                if (run)
                    cancelTimer();

                media = new Media(audios.get(audioNum).toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                audioLabel.setText(audios.get(audioNum).getName());

                playAudio();
            }
        }
    }

    public void ErrorMessage(int code)
    {
        String message;
        switch (code)
        {
            case 1:
            {
                message = "No any file found in the directory.";
                break;
            }
            case 2:
            {
                message = "Er ";
                break;
            }
            case 3:
            {
                message = "Err ";
                break;
            }
            case 4:
            {
                message = " ";
                break;
            }
            default:
                message = "There is an error occur!";
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText("Look, an Error Dialog");
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
}