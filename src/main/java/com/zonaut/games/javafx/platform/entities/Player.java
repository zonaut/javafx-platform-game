package com.zonaut.games.javafx.platform.entities;

import com.zonaut.games.javafx.platform.screens.NewGameScreen;
import com.zonaut.games.javafx.platform.utils.ImageUtil;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Player extends ImageView {

    private static final double DELAY = 1.0 / 60;
    private static final double SPEED_X = 300; // Tweak value to desired speed

    Image[] tempSprite;

    private boolean isMovingLeft;
    private boolean isMovingRight;

    public Player(double x, double y) {
        Image image = new Image(NewGameScreen.class.getResourceAsStream("/sprites/player.png"));
        tempSprite = ImageUtil.getFrom(image, 0, 0, 32, 32, 2);

        setX(x);
        setY(y);

        setImage(tempSprite[1]);
        setFitHeight(32);
        setFitWidth(32);
    }

    public void tick() {
        // Move player on the X axis
        if (isMovingRight) {
            setX(getX() + SPEED_X * DELAY);
        }
        if (isMovingLeft) {
            setX(getX() - SPEED_X * DELAY);
        }
    }

    public void moveRight() {
        isMovingRight = true;
        isMovingLeft = false;
    }

    public void moveLeft() {
        isMovingLeft = true;
        isMovingRight = false;
    }

    public void stopMovingRightOrLeft() {
        isMovingLeft = false;
        isMovingRight = false;
    }

}
