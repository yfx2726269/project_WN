package com;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GoBangMainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        //创建场景
        Scene scene = new Scene(GenerateMainScene.getChessboard(), GenerateMainScene.getStageHeight(), GenerateMainScene.getStageWeight());
        //放置场景
        stage.setResizable(false);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        //展示舞台
    }

    public static void main(String[] args) {
        //启动
        launch(args);
    }
}
