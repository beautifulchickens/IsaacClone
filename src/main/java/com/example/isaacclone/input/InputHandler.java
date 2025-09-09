package com.example.isaacclone.input;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.util.HashMap;
import java.util.Map;

public class InputHandler {
    // 定义游戏中使用的按键
    //TODO:修改发射为通过小键盘方向键控制
    public enum Key {
        UP, DOWN, LEFT, RIGHT, SHOOT, NEXT_ROOM, PAUSE
    }

    // 按键状态映射（是否按下）
    private Map<Key, Boolean> keyStates;

    // 按键与KeyCode的映射（可自定义键位）
    private Map<Key, KeyCode> keyMap;

    public InputHandler() {
        // 初始化按键状态
        keyStates = new HashMap<>();
        for (Key key : Key.values()) {
            keyStates.put(key, false);
        }

        // 初始化默认键位映射
        keyMap = new HashMap<>();
        keyMap.put(Key.UP, KeyCode.W);
        keyMap.put(Key.DOWN, KeyCode.S);
        keyMap.put(Key.LEFT, KeyCode.A);
        keyMap.put(Key.RIGHT, KeyCode.D);
        keyMap.put(Key.SHOOT, KeyCode.SPACE);
        keyMap.put(Key.NEXT_ROOM, KeyCode.E);
        keyMap.put(Key.PAUSE, KeyCode.ESCAPE);

        System.out.println(keyMap);
    }

    // 处理按键按下事件
    public void handleKeyPress(KeyEvent event) {
        KeyCode code = event.getCode();

        // 检查按下的键是否在我们的映射中
        for (Map.Entry<Key, KeyCode> entry : keyMap.entrySet()) {
            if (entry.getValue() == code) {
                System.out.println("按下键: " + code); // 输出日志
                keyStates.put(entry.getKey(), true);
                break;
            }
        }
    }

    // 处理按键释放事件
    public void handleKeyRelease(KeyEvent event) {
        KeyCode code = event.getCode();

        // 检查释放的键是否在我们的映射中
        for (Map.Entry<Key, KeyCode> entry : keyMap.entrySet()) {
            if (entry.getValue() == code) {
                keyStates.put(entry.getKey(), false);
                break;
            }
        }
    }

    // 检查按键是否按下
    public boolean isKeyPressed(Key key) {
        return keyStates.getOrDefault(key, false);
    }

    // 获取键位映射（用于配置界面）
    public Map<Key, KeyCode> getKeyMap() {
        return keyMap;
    }

    // 允许重新映射键位
    public void setKeyMapping(Key key, KeyCode code) {
        keyMap.put(key, code);
    }
}
