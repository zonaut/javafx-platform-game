package com.zonaut.games.javafx.platform.level;

import com.zonaut.games.javafx.platform.entities.Player;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class LevelKeyInputHandler {

    private static final Logger LOG = LogManager.getLogger(LevelKeyInputHandler.class);

    private final Set<KeyCode> activeKeys = new HashSet<>();
    private final Player player;

    public LevelKeyInputHandler(Scene scene, Player player) {
        this.player = player;

        scene.setOnKeyPressed(e -> activeKeys.add(e.getCode()));
        scene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
    }

    public void handleCurrentInput() {
        if (activeKeys.contains(KeyCode.RIGHT) && !activeKeys.contains(KeyCode.LEFT)) {
            player.moveRight();
        }
        if (activeKeys.contains(KeyCode.LEFT) && !activeKeys.contains(KeyCode.RIGHT)) {
            player.moveLeft();
        }
        if (!activeKeys.contains(KeyCode.LEFT) && !activeKeys.contains(KeyCode.RIGHT)) {
            player.stopMovingRightOrLeft();
        }

        if (activeKeys.contains(KeyCode.UP)) {
            player.jump();
        }
    }

}
