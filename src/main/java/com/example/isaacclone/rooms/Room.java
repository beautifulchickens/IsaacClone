package com.example.isaacclone.rooms;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.example.isaacclone.entities.Enemy;
import com.example.isaacclone.entities.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {
    private int roomNumber;
    private RoomType type;
    private int leftWall;
    private int rightWall;
    private int topWall;
    private int bottomWall;
    private List<Door> doors;
    private int totalEnemies;

    // 房间类型
    public enum RoomType {
        NORMAL,
        TREASURE,
        BOSS,
        SHOP,
        SECRET
    }

    // 门的位置
    public enum DoorPosition {
        TOP, BOTTOM, LEFT, RIGHT
    }

    // 门类
    public static class Door {

        private DoorPosition position;
        private boolean isOpen;
        private int lastRoom;
        private int nextRoom; //记录门两侧的房间



        public Door(DoorPosition position) {
            this.position = position;
            this.isOpen = false; // 初始关闭，清理房间后打开
        }

        public DoorPosition getPosition() { return position; }
        public boolean isOpen() { return isOpen; }
        public void open() { isOpen = true; }
    }

    public Room(int roomNumber, RoomType type, int left, int right, int top, int bottom) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.leftWall = left;
        this.rightWall = right;
        this.topWall = top;
        this.bottomWall = bottom;
        this.doors = new ArrayList<>();
        this.totalEnemies = 0;

        // 根据房间类型设置门
        setupDoors();
    }

    private void setupDoors() {
        //TODO:需要添加返回前一房间的门的逻辑,

        // 随机添加门（至少有一个出口）
        Random random = new Random();

        // 普通房间至少有两个门
        if (type == RoomType.NORMAL) {
            doors.add(new Door(DoorPosition.TOP));
            doors.add(new Door(DoorPosition.BOTTOM));

            if (random.nextBoolean()) doors.add(new Door(DoorPosition.LEFT));
            if (random.nextBoolean()) doors.add(new Door(DoorPosition.RIGHT));
        }
        // BOSS房间通常只有一个入口
        else if (type == RoomType.BOSS) {
            doors.add(new Door(DoorPosition.LEFT));
        }
        // 其他类型房间
        else {
            doors.add(new Door(DoorPosition.TOP));
            if (random.nextBoolean()) doors.add(new Door(DoorPosition.BOTTOM));
            if (random.nextBoolean()) doors.add(new Door(DoorPosition.LEFT));
            if (random.nextBoolean()) doors.add(new Door(DoorPosition.RIGHT));

            // 确保至少有一个门
            if (doors.isEmpty()) {
                doors.add(new Door(DoorPosition.RIGHT));
            }
        }
    }

    // 在房间中生成敌人
    public void spawnEnemies(EntityManager entityManager) {
        // 根据房间类型生成不同数量和类型的敌人
        Random random = new Random();

        if (type == RoomType.NORMAL) {
            totalEnemies = 2 + random.nextInt(3); // 2-4个敌人
        } else if (type == RoomType.BOSS) {
            totalEnemies = 1; // BOSS房间只有一个BOSS
        } else {
            totalEnemies = random.nextInt(2); // 其他房间0-1个敌人
        }

        // 生成敌人
        for (int i = 0; i < totalEnemies; i++) {
            Enemy.EnemyType enemyType;

            // 随机选择敌人类型
            if (type == RoomType.BOSS) {
                // BOSS房间生成特殊BOSS（暂时用STRONG类型代替）
                //TODO
                enemyType = Enemy.EnemyType.STRONG;
            } else {
                int rand = random.nextInt(10);
                if (rand < 6) {
                    enemyType = Enemy.EnemyType.NORMAL;
                } else if (rand < 9) {
                    enemyType = Enemy.EnemyType.FAST;
                } else {
                    enemyType = Enemy.EnemyType.STRONG;
                }
            }

            // 随机位置，但远离中心和墙壁
            double x = leftWall + 50 + random.nextDouble() * (rightWall - leftWall - 100 - 25);
            double y = topWall + 50 + random.nextDouble() * (bottomWall - topWall - 100 - 25);

            // 创建敌人并添加到实体管理器
            entityManager.addEntity(new Enemy(x, y, enemyType));
        }
    }

    // 检查房间是否已清空（所有敌人被消灭）
    public boolean isCleared(EntityManager entityManager) {
        // 如果是战斗房间，检查敌人是否全部消灭
        if (type == RoomType.NORMAL || type == RoomType.BOSS) {
            boolean cleared = entityManager.getEnemies().isEmpty();

            // 如果房间已清空，打开所有门
            if (cleared) {
                for (Door door : doors) {
                    door.open();
                }
            }

            return cleared;
        }

        // 非战斗房间默认视为已清空
        return true;
    }

    // 获取剩余敌人数量
    public int getRemainingEnemies(EntityManager entityManager) {
        return entityManager.getEnemies().size();
    }

    // 渲染房间
    public void render(GraphicsContext gc) {

        //TODO:绘制背景

        // 绘制墙壁
        //TODO:更改为图片(可选)
        gc.setFill(Color.DARKGRAY);
        gc.fillRect(leftWall, topWall, rightWall - leftWall, topWall); // 上墙
        gc.fillRect(leftWall, bottomWall, rightWall - leftWall, topWall); // 下墙
        gc.fillRect(leftWall, topWall, leftWall, bottomWall - topWall); // 左墙
        gc.fillRect(rightWall, topWall, leftWall, bottomWall - topWall); // 右墙

        // 绘制地板
        gc.setFill(Color.rgb(30, 30, 30));
        gc.fillRect(leftWall, topWall, rightWall - leftWall, bottomWall - topWall);

        // 绘制门
        gc.setFill(Color.BROWN);
        for (Door door : doors) {
            int doorWidth = 40;
            int doorHeight = 10;

            switch (door.position) {
                case TOP:
                    int topDoorX = (leftWall + rightWall - doorWidth) / 2;
                    gc.fillRect(topDoorX, topWall - doorHeight/2, doorWidth, doorHeight);
                    break;
                case BOTTOM:
                    int bottomDoorX = (leftWall + rightWall - doorWidth) / 2;
                    gc.fillRect(bottomDoorX, bottomWall - doorHeight/2, doorWidth, doorHeight);
                    break;
                case LEFT:
                    int leftDoorY = (topWall + bottomWall - doorWidth) / 2;
                    gc.fillRect(leftWall - doorHeight/2, leftDoorY, doorHeight, doorWidth);
                    break;
                case RIGHT:
                    int rightDoorY = (topWall + bottomWall - doorWidth) / 2;
                    gc.fillRect(rightWall - doorHeight/2, rightDoorY, doorHeight, doorWidth);
                    break;
            }

            // 如果门是关闭的，绘制门板
            if (!door.isOpen()) {
                gc.setFill(Color.rgb(100, 60, 30));
                switch (door.position) {
                    case TOP:
                    case BOTTOM:
                        gc.fillRect(
                                (leftWall + rightWall - doorWidth/2) / 2,
                                door.position == DoorPosition.TOP ? topWall - doorHeight/2 : bottomWall - doorHeight/2,
                                doorWidth/2,
                                doorHeight
                        );
                        break;
                    case LEFT:
                    case RIGHT:
                        gc.fillRect(
                                door.position == DoorPosition.LEFT ? leftWall - doorHeight/2 : rightWall - doorHeight/2,
                                (topWall + bottomWall - doorWidth/2) / 2,
                                doorHeight,
                                doorWidth/2
                        );
                        break;
                }
                gc.setFill(Color.BROWN);
            }
        }
    }

    // getter方法
    public int getLeftWall() { return leftWall; }
    public int getRightWall() { return rightWall; }
    public int getTopWall() { return topWall; }
    public int getBottomWall() { return bottomWall; }
    public RoomType getType() { return type; }
    public int getRoomNumber() { return roomNumber; }
    public int getTotalEnemies() { return totalEnemies; }
    public List<Door> getDoors() { return doors; }
}
