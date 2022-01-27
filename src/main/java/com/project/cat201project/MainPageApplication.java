package com.project.cat201project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageApplication extends Application
{
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(MainPageApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }

    public void playAudio()
    {
        mediaPlayer.play();
    }

    public void stopAudio()
    {
        mediaPlayer.pause();
    }

    public void previousAudio()
    {
    }

    public void nextAudio()
    {
    }

    public void resetAudio()
    {
    }

    public void changeAudioSpeed(ActionEvent event)
    {
    }

    public void startTimer()
    {
    }

    public void cancelTimer()
    {
    }
}