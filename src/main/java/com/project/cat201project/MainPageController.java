package com.project.cat201project;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
<<<<<<< Updated upstream
import javafx.scene.layout.AnchorPane;
=======
import javafx.scene.input.MouseEvent;
>>>>>>> Stashed changes
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
    @FXML private Label audioLabel;
    @FXML private ListView<String> songlist;
    @FXML private Button playBttn, pauseBttn, resetBttn, previousBttn, nextBttn, browseBttn;
    @FXML private ComboBox<String> speedBox;
    @FXML private Slider volSlider;
    @FXML private ProgressBar audioProgressBar;

    @FXML
    private void openDirectoryChooser(ActionEvent event)
    {
        final DirectoryChooser dirChooser = new DirectoryChooser();
        Stage stage = (Stage) browseBttn.getScene().getWindow();
        File file = dirChooser.showDialog(stage);

        if(file != null)
        {
<<<<<<< Updated upstream
            System.out.println("Path: " + file.getAbsolutePath());
=======
            mediaPlayer.stop();
            if(run)
                cancelTimer();
            audios.clear();
            for (File fil : files)
            {
                audios.add(fil);
            }
            media = new Media(audios.get(audioNum).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            audioLabel.setText(audios.get(audioNum).getName());
            playAudio();
>>>>>>> Stashed changes
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

        ObservableList<String> names = FXCollections.observableArrayList();;
        if (files != null)
        {
            for (File file: files)
            {
                audios.add(file);
                names.add(file.getName());
//                songlist.getItems().add(file.getName());
            }
            songlist.setItems(names);
        }

        media = new Media(audios.get(audioNum).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        audioLabel.setText(audios.get(audioNum).getName());

        for(int i = 0; i < audioSpeeds.length; i++)
        {
            speedBox.getItems().add(Double.toString(audioSpeeds[i]));
        }

        speedBox.setOnAction(this::changeAudioSpeed);
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
        changeAudioSpeed(null);
        mediaPlayer.setVolume(volSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    public void stopAudio()
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
        }
        else
        {
            audioNum = 0;
            mediaPlayer.stop();

            media = new Media(audios.get(audioNum).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            audioLabel.setText(audios.get(audioNum).getName());
        }
    }

    public void resetAudio()
    {
        audioProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    public void changeAudioSpeed(ActionEvent event)
    {
        if(speedBox.getValue() == null)
        {
            mediaPlayer.setRate(1);
        }
        mediaPlayer.setRate(Integer.parseInt(speedBox.getValue()));
    }

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

    public void listClicked(MouseEvent event)
    {
        String item = songlist.getSelectionModel().getSelectedItem();
        String item2 =audioLabel.getText();
        if(!item.equals(item2))
        {
            audioNum = 0;
            item2 = audios.get(audioNum).getName();

            while (!(item.equals(item2))) {
                audioNum++;
                item2 = audios.get(audioNum).getName();
            }
            ;
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