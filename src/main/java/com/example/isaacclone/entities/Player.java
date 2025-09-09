package com.example.isaacclone.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import com.example.isaacclone.input.InputHandler;
import com.example.isaacclone.rooms.Room;

public class Player extends Entity {
    private int health;
    private int maxHealth;
    private double damage;
    private double fireRate; // 射击频率（越低越快）
    private double fireCooldown; // 射击冷却时间
    private double fireRange;//射程
    //TODO:实现道具、炸弹、钥匙逻辑（可选）

    public Player(double x, double y) {
        super(x, y, 30, 30);
        this.maxHealth = 3;
        this.health = maxHealth;
        this.damage = 3.5;
        this.speed = 2.5;
        this.fireRate = 20;
        this.fireCooldown = 0;
        //this.fireRange = 10;
    }

    @Override
    public void update(double delta, InputHandler input, Room room) {
        // 处理移动
        handleMovement(input, delta);

        // 处理射击
        handleShooting(input, delta);

        // 确保玩家在房间范围内
        constrainToRoom(room);

        // 更新冷却时间
        if (fireCooldown > 0) {
            fireCooldown -= delta;
        }
    }

    private void handleMovement(InputHandler input, double delta) {
        double moveX = 0;
        double moveY = 0;

        // 根据输入计算移动方向
        if (input.isKeyPressed(InputHandler.Key.UP)) {
            moveY -= speed * delta;
        }
        if (input.isKeyPressed(InputHandler.Key.DOWN)) {
            moveY += speed * delta;
        }
        if (input.isKeyPressed(InputHandler.Key.LEFT)) {
            moveX -= speed * delta;
        }
        if (input.isKeyPressed(InputHandler.Key.RIGHT)) {
            moveX += speed * delta;
        }

        // 归一化对角线移动速度
        if (moveX != 0 && moveY != 0) {
            double normalized = 1 / Math.sqrt(2);
            moveX *= normalized;
            moveY *= normalized;
        }

        // 应用移动
        x += moveX;
        y += moveY;
    }

    private void handleShooting(InputHandler input, double delta) {
        // 检查是否可以射击（冷却时间结束且按下射击键）
        if (fireCooldown <= 0 && input.isKeyPressed(InputHandler.Key.SHOOT)) {
            // 创建子弹（暂时向右侧发射击）TODO:修改发射子弹逻辑
            Bullet bullet = new Bullet(x + width, y + height/2 - 2, 10, 4, damage, 5, 0);
            EntityManager.getInstance().addEntity(bullet);

            // 重置冷却时间
            fireCooldown = fireRate;
        }
    }

    private void constrainToRoom(Room room) {
        // 限制玩家在房间边界内
        x = Math.max(room.getLeftWall(), Math.min(x, room.getRightWall() - width));
        y = Math.max(room.getTopWall(), Math.min(y, room.getBottomWall() - height));
    }

    @Override
    public void render(GraphicsContext gc) {
        // 绘制玩家（红色圆形） TODO:添加素材
        gc.setFill(Color.RED);
        gc.fillOval(x, y, width, height);
    }

    // 受伤方法
    public void takeDamage(int amount) {
        health = Math.max(0, health - amount);
        if (health <= 0) {
            setAlive(false);
            // TODO:添加死亡逻辑
            System.out.println("GameOver");
        }
    }

    // 恢复生命值
    public void heal(int amount) {
        health = Math.min(maxHealth, health + amount);
    }

    // getter和setter方法
    public int getHealth() { return health; }
    public double getDamage() { return damage; }
    public double getSpeed() { return speed; }

    public void setDamage(double damage) { this.damage = damage; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setFireRate(double fireRate) { this.fireRate = fireRate; }
}
