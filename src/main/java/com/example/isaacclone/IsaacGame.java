package com.example.isaacclone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class IsaacGame extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // 加载游戏主界面
        FXMLLoader fxmlLoader = new FXMLLoader(IsaacGame.class.getResource("game-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);

        // 配置舞台
        stage.setTitle("以撒的结合 复刻版");
        stage.setScene(scene);
        stage.setResizable(false); // 游戏窗口不可调整大小
        stage.show();
    }

    public static void main(String[] args) {
        launch(); // 启动JavaFX应用
    }
}
