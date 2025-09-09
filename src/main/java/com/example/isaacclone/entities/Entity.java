package com.example.isaacclone.entities;

import javafx.scene.canvas.GraphicsContext;
import com.example.isaacclone.input.InputHandler;
import com.example.isaacclone.rooms.Room;

public abstract class Entity {
    protected double x; // x坐标
    protected double y; // y坐标
    protected double width; // 宽度
    protected double height; // 高度
    protected double speed; // 移动速度
    protected boolean alive; // 是否存活

    public Entity(double x, double y, double width, double height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = 1.0;
        this.alive = true;
    }

    // 更新实体状态
    public abstract void update(double delta, InputHandler input, Room room);

    // 渲染实体
    public abstract void render(GraphicsContext gc);

    // 检查与其他实体的碰撞
    public boolean collidesWith(Entity other) {
        return x < other.x + other.width &&
                x + width > other.x &&
                y < other.y + other.height &&
                y + height > other.y;
    }

    // getter和setter方法
    public double getX() { return x; }
    public void setX(double x) { this.x = x; }

    public double getY() { return y; }
    public void setY(double y) { this.y = y; }

    public double getWidth() { return width; }
    public double getHeight() { return height; }

    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }
}
