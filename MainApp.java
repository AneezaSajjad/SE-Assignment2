/*
MIT License
Copyright (c) 2018 Guru
Only for Educational purposes and for reference.
*/
package com.mycompany.atmmanagementsys;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

public class MainApp extends Application {


    @Override
    public void start(@org.jetbrains.annotations.NotNull Stage stage) throws Exception {
        System.out.println(getClass());
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainScreen.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/MainScreen.css");
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.setTitle("Main Screen");
        stage.setScene(scene);
        stage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }

}
