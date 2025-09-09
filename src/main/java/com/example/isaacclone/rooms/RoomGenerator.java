package com.example.isaacclone.rooms;

import java.util.Random;

public class RoomGenerator {
    private static int currentRoomNumber = 1;
    private static Random random = new Random();

    // 生成随机房间
    public static Room generateRandomRoom() {
        // 房间边界（基于600x500的游戏画布）
        int leftWall = 0;
        int rightWall = 600;
        int topWall = 0;
        int bottomWall = 500;

        // 随机选择房间类型
        Room.RoomType type = getRandomRoomType();

        // 创建并返回新房间
        Room newRoom = new Room(currentRoomNumber++, type, leftWall, rightWall, topWall, bottomWall);
        return newRoom;
    }

    // 随机选择房间类型
    private static Room.RoomType getRandomRoomType() {
        int rand = random.nextInt(100);

        // 概率分布：普通房间70%，宝藏房间10%，BOSS房间5%，商店5%，秘密房间10%
        if (rand < 70) {
            return Room.RoomType.NORMAL;
        } else if (rand < 80) {
            return Room.RoomType.TREASURE;
        } else if (rand < 85) {
            return Room.RoomType.BOSS;
        } else if (rand < 90) {
            return Room.RoomType.SHOP;
        } else {
            return Room.RoomType.SECRET;
        }
    }

    // 重置房间编号（新游戏时使用）
    public static void resetRoomNumbering() {
        currentRoomNumber = 1;
    }
}
