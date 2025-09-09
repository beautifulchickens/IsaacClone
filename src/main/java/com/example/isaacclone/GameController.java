package com.example.isaacclone;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import com.example.isaacclone.entities.EntityManager;
import com.example.isaacclone.entities.Player;
import com.example.isaacclone.input.InputHandler;
import com.example.isaacclone.rooms.Room;
import com.example.isaacclone.rooms.RoomGenerator;

public class GameController {
    @FXML private AnchorPane gamePane;
    @FXML private Canvas gameCanvas;
    @FXML private Label healthLabel;
    @FXML private Label damageLabel;
    @FXML private Label speedLabel;
    @FXML private Label roomLabel;
    @FXML private Label enemiesLabel;

    private GraphicsContext gc;
    private EntityManager entityManager;
    private InputHandler inputHandler;
    private Room currentRoom;
    private boolean isRunning;
    private Player player;

    @FXML
    public void initialize() {
        // 初始化画布
        gc = gameCanvas.getGraphicsContext2D();

        // 初始化游戏组件
        inputHandler = new InputHandler();
        entityManager = new EntityManager();
        currentRoom = RoomGenerator.generateRandomRoom();

        // 创建玩家
        player = new Player(300, 250); // 初始位置在画布中心
        entityManager.addEntity(player);

        // 生成房间内的敌人
        currentRoom.spawnEnemies(entityManager);

        // 注册输入监听
        setupInputHandling();

        // 启动游戏循环
        startGameLoop();

        // 点击面板时重新获取焦点
        gamePane.setOnMouseClicked(event -> {
            gamePane.requestFocus();
            System.out.println("面板已获取焦点");
        });
    }

    private void setupInputHandling() {
        gamePane.setOnKeyPressed(this::handleKeyPress);
        gamePane.setOnKeyReleased(this::handleKeyRelease);
        gamePane.setFocusTraversable(true); // 允许面板获取焦点
        gamePane.requestFocus(); // 确保面板能接收键盘输入
    }

    private void startGameLoop() {
        isRunning = true;

        // 游戏循环线程
        new Thread(() -> {
            long lastTime = System.nanoTime();
            double nsPerUpdate = 1_000_000_000.0 / 60.0; // 60 FPS

            while (isRunning) {
                long now = System.nanoTime();
                double delta = (now - lastTime) / nsPerUpdate;

                // 更新游戏逻辑
                update(delta);
                // 渲染画面
                render();

                lastTime = now;

                // 控制循环速度
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void update(double delta) {
        // 更新所有实体
        entityManager.update(delta, inputHandler, currentRoom);

        // 检查房间是否清空
        if (currentRoom.isCleared(entityManager)) {
            // 可以切换到下一个房间
            //TODO
        }

        // 更新UI信息
        updateHUD();
    }

    private void render() {
        // 清空画布
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());

        // 渲染房间
        currentRoom.render(gc);

        // 渲染所有实体
        entityManager.render(gc);
    }

    private void updateHUD() {
        // 更新玩家状态显示
        healthLabel.setText("生命值: " + player.getHealth());
        damageLabel.setText("伤害: " + player.getDamage());
        speedLabel.setText("速度: " + player.getSpeed());

        // 更新房间信息
        roomLabel.setText("房间: " + currentRoom.getRoomNumber());
        enemiesLabel.setText("敌人: " + currentRoom.getRemainingEnemies(entityManager) +
                "/" + currentRoom.getTotalEnemies());
    }

    private void handleKeyPress(KeyEvent event) {
        inputHandler.handleKeyPress(event);

        // 处理特殊按键，如切换房间
        if (event.getCode().equals(inputHandler.getKeyMap().get(InputHandler.Key.NEXT_ROOM))) {
            System.out.println("尝试进入下一房间");
            if (currentRoom.isCleared(entityManager)) {
                currentRoom = RoomGenerator.generateRandomRoom();
                currentRoom.spawnEnemies(entityManager);
            }
        }
    }

    private void handleKeyRelease(KeyEvent event) {
        inputHandler.handleKeyRelease(event);
    }
}
