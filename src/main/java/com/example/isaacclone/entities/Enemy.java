package com.example.isaacclone.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.example.isaacclone.input.InputHandler;
import com.example.isaacclone.rooms.Room;

import java.util.List;
import java.util.Random;

public class Enemy extends Entity {
    private double damage;
    private int health;
    private EnemyType type;
    private Random random;
    private double moveTimer;
    private double moveInterval;
    private double targetX;
    private double targetY;

    // 敌人类型枚举
    public enum EnemyType {
        NORMAL(20, 1, 1.0),
        FAST(10, 1, 1.8),
        STRONG(30, 2, 0.7);

        public final int health;
        public final double damage;
        public final double speed;

        EnemyType(int health, double damage, double speed) {
            this.health = health;
            this.damage = damage;
            this.speed = speed;
        }
    }

    public Enemy(double x, double y, EnemyType type) {
        super(x, y, 25, 25);
        this.type = type;
        this.health = type.health;
        this.damage = type.damage;
        this.speed = type.speed;
        this.random = new Random();
        this.moveTimer = 0;
        this.moveInterval = 100; // 敌人改变移动方向的时间间隔
    }

    @Override
    public void update(double delta, InputHandler input, Room room) {
        // 寻找玩家并移动
        moveTowardsPlayer(delta, room);

        // 检查与玩家的碰撞
        checkPlayerCollision();

        // 确保敌人在房间内
        constrainToRoom(room);
    }

    private void moveTowardsPlayer(double delta, Room room) {
        // 获取玩家引用
        Player player = EntityManager.getInstance().getPlayer();

        if (player != null && player.isAlive()) {
            // 计算向玩家移动的方向
            double dx = player.getX() - x;
            double dy = player.getY() - y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            // 如果离玩家足够远，则向玩家移动
            if (distance > 50) {
                dx /= distance;
                dy /= distance;

                x += dx * speed * delta;
                y += dy * speed * delta;
            } else {
                // 近距离时随机移动
                moveRandomly(delta, room);
            }
        } else {
            // 如果没有玩家，随机移动
            moveRandomly(delta, room);
        }
    }

    private void moveRandomly(double delta, Room room) {
        moveTimer += delta;

        // 定期改变移动目标位置
        if (moveTimer >= moveInterval) {
            moveTimer = 0;
            // 在房间内随机选择一个目标位置
            targetX = room.getLeftWall() + random.nextDouble() * (room.getRightWall() - width - room.getLeftWall());
            targetY = room.getTopWall() + random.nextDouble() * (room.getBottomWall() - height - room.getTopWall());
        }

        // 向目标位置移动
        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 5) {
            dx /= distance;
            dy /= distance;

            x += dx * speed * delta;
            y += dy * speed * delta;
        }
    }

    private void checkPlayerCollision() {
        Player player = EntityManager.getInstance().getPlayer();
        if (player != null && collidesWith(player) && player.isAlive()) {
            // 敌人接触到玩家，造成伤害
            player.takeDamage((int)damage);
        }
    }

    private void constrainToRoom(Room room) {
        x = Math.max(room.getLeftWall(), Math.min(x, room.getRightWall() - width));
        y = Math.max(room.getTopWall(), Math.min(y, room.getBottomWall() - height));
    }

    @Override
    public void render(GraphicsContext gc) {
        // 根据敌人类型绘制不同颜色
        switch (type) {
            case NORMAL:
                gc.setFill(Color.GREEN);
                break;
            case FAST:
                gc.setFill(Color.YELLOW);
                break;
            case STRONG:
                gc.setFill(Color.DARKGREEN);
                break;
        }

        gc.fillOval(x, y, width, height);

        // 绘制生命值条
        gc.setFill(Color.RED);
        double healthBarWidth = (health / (double)type.health) * width;
        gc.fillRect(x, y - 5, healthBarWidth, 3);
    }

    // 受伤方法
    public void takeDamage(double damage) {
        health -= damage;
        if (health <= 0) {
            setAlive(false);
            // 可以在这里添加敌人死亡时的逻辑，如掉落道具等
        }
    }

    public EnemyType getType() {
        return type;
    }
}
