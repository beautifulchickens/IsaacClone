package com.example.isaacclone.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import com.example.isaacclone.input.InputHandler;
import com.example.isaacclone.rooms.Room;

public class Bullet extends Entity {
    private double damage;
    private double directionX;
    private double directionY;
    private int lifetime; // 子弹生命周期（帧数）
    private int maxLifetime; // 最大生命周期

    public Bullet(double x, double y, double width, double height,
                  double damage, double speed, double angle) {
        super(x, y, width, height);
        this.damage = damage;
        this.speed = speed;

        // 根据角度计算方向向量
        this.directionX = Math.cos(Math.toRadians(angle));
        this.directionY = Math.sin(Math.toRadians(angle));

        this.maxLifetime = 120; // 子弹存在的最大帧数
        this.lifetime = maxLifetime;
    }

    // 重载构造函数，直接指定方向
    public Bullet(double x, double y, double width, double height,
                  double damage, double speed, double dirX, double dirY) {
        super(x, y, width, height);
        this.damage = damage;
        this.speed = speed;
        this.directionX = dirX;
        this.directionY = dirY;
        this.maxLifetime = 120;
        this.lifetime = maxLifetime;
    }

    @Override
    public void update(double delta, InputHandler input, Room room) {
        // 移动子弹
        x += directionX * speed * delta;
        y += directionY * speed * delta;

        // 减少生命周期
        lifetime -= delta;
        if (lifetime <= 0) {
            setAlive(false);
            return;
        }

        // 检查是否击中敌人
        checkEnemyCollision();

        // 检查是否超出房间边界
        if (x < room.getLeftWall() || x + width > room.getRightWall() ||
                y < room.getTopWall() || y + height > room.getBottomWall()) {
            setAlive(false);
        }
    }

    private void checkEnemyCollision() {
        // 检查与敌人的碰撞
        for (Enemy enemy : EntityManager.getInstance().getEnemies()) {
            if (enemy.isAlive() && collidesWith(enemy)) {
                enemy.takeDamage(damage);
                setAlive(false); // 击中敌人后子弹消失
                break;
            }
        }
    }

    @Override
    public void render(GraphicsContext gc) {
        // 绘制子弹（黄色矩形）
        gc.setFill(Color.YELLOW);
        gc.fillRect(x, y, width, height);
    }
}
