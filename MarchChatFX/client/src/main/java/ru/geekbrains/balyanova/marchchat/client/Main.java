package ru.geekbrains.balyanova.marchchat.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/window.fxml"));
        root.setStyle("-fx-background-color: CADETBLUE");// цвет рамки
        //primaryStage.getIcons().add(new Image("resources/png.smile.png"));//добавила иконку
        primaryStage.setTitle("  M A R C H    C H A T");
        primaryStage.setResizable(false);//нельзя менять размер окна
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
