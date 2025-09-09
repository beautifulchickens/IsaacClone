package com.example.isaacclone.entities;

import javafx.scene.canvas.GraphicsContext;
import com.example.isaacclone.input.InputHandler;
import com.example.isaacclone.rooms.Room;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityManager {
    // 单例模式
    private static EntityManager instance;

    private List<Entity> entities;
    private List<Entity> toAdd; // 待添加的实体（避免在迭代中修改列表）

    public EntityManager() {
        entities = new ArrayList<>();
        toAdd = new ArrayList<>();
    }

    // 获取单例实例
    public static EntityManager getInstance() {
        if (instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    // 添加实体
    public void addEntity(Entity entity) {
        toAdd.add(entity);

//        if (entity instanceof Bullet) {
////            System.out.println("子弹已添加到实体管理器");
//        }
    }

    // 更新所有实体
    public void update(double delta, InputHandler input, Room room) {
        // 添加待添加的实体
        entities.addAll(toAdd);
        toAdd.clear();

        // 更新所有实体
        for (Entity entity : new ArrayList<>(entities)) { // 使用副本避免ConcurrentModificationException
            if (entity.isAlive()) {
                entity.update(delta, input, room);
            } else {
                entities.remove(entity);
            }
        }
    }

    // 渲染所有实体
    public void render(GraphicsContext gc) {
        for (Entity entity : entities) {
            if (entity.isAlive()) {
                //System.out.println(entity);
                entity.render(gc);
            }
        }
    }

    // 获取玩家
    public Player getPlayer() {
        for (Entity entity : entities) {
            if (entity instanceof Player && entity.isAlive()) {
                return (Player) entity;
            }
        }
        return null;
    }

    // 获取所有敌人
    public List<Enemy> getEnemies() {
        return entities.stream()
                .filter(e -> e instanceof Enemy && e.isAlive())
                .map(e -> (Enemy) e)
                .collect(Collectors.toList());
    }

    // 清除所有实体
    public void clearAll() {
        entities.clear();
        toAdd.clear();
    }
}
