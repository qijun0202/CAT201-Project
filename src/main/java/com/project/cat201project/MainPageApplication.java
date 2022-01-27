package com.project.cat201project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPageApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
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

    public void playMusic()
    {

    }

    public void stopMusic()
    {

    }

    public void previousMusic()
    {

    }

    public void nextMusic()
    {

    }

    public void resetMusic()
    {

    }

    public void changeMusicSpeed(ActionEvent event)
    {

    }

    public void startTimer()
    {

    }

    public void cancelTimer()
    {

    }
}