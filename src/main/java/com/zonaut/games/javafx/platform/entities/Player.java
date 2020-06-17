package com.zonaut.games.javafx.platform.entities;

import com.zonaut.games.javafx.platform.config.AppConfig;
import com.zonaut.games.javafx.platform.level.Block;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import com.zonaut.games.javafx.platform.screens.NewGameScreen;
import com.zonaut.games.javafx.platform.utils.ImageUtil;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Player extends ImageView {

    private static final Logger LOG = LogManager.getLogger(Player.class);

    private static final double DELAY = 1.0 / AppConfig.getFps();

    private static final double SPEED_X = 300; // Tweak value to desired speed
    private static final double JUMP_HEIGHT = 750; // Tweak value to desired height
    private static final double GRAVITY_ACCELERATION = 2200; // Tweak value to desired speed of acceleration

    private LevelLoader levelLoader;

    private Image[] playerSprite;
    private int playerWidth = 32;
    private int playerHeight = 32;

    private double yMotionPosition;

    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isJumping;

    public Player(LevelLoader levelLoader, double x, double y) {
        this.levelLoader = levelLoader;

        setX(x);
        setY(y);

        // TODO Use sprites for different player movement action like idle, walking, ...
        Image image = new Image(NewGameScreen.class.getResourceAsStream("/sprites/player.png"));
        playerSprite = ImageUtil.getFrom(image, 0, 0, 32, 32, 2);
        setImage(playerSprite[1]);
        setFitWidth(playerWidth);
        setFitHeight(playerHeight);
    }

    /**
     * Change player position based on it's environment and actions performed by the user.
     * The order of the checks and adjustments done here matter and can't be changed.
     * TODO This needs to be tweaked a bit + additions for future actions
     */
    public void tick() {
        // Move player on the X axis
        if (isMovingRight) {
            setX(getX() + SPEED_X * DELAY);
        }
        if (isMovingLeft) {
            setX(getX() - SPEED_X * DELAY);
        }
        // Don't let the player go out of our level on the X axis
        if (getX() < 0) {
            setX(0);
        }
        if (getX() > levelLoader.getLevelPixelWidth() - AppConfig.getTileSize()) {
            setX(levelLoader.getLevelPixelWidth() - AppConfig.getTileSize());
        }

        // An offset is needed to reposition the player on collision otherwise the position is in the block
        double OFFSET = 0.01;

        // Check collisions with solid tiles on the X axis and reposition the player if it collides
        for (Block block : levelLoader.getSolidBlocks()) {
            if (intersects(block)) {
                if (isMovingRight) {
                    setX(block.getMinX() - playerWidth - OFFSET);
                }
                if (isMovingLeft) {
                    setX(block.getMaxX() + OFFSET);
                }
            }
        }

        // Calculate and set the Y axis of the player based on current position and gravity settings
        yMotionPosition += GRAVITY_ACCELERATION * DELAY;
        setY(getY() + yMotionPosition * DELAY);

        // Check collisions with solid tiles on the Y axis and reposition the player if it collides
        boolean jumping = yMotionPosition < 0;
        boolean falling = yMotionPosition > 0;
        for (Block block : levelLoader.getSolidBlocks()) {
            if (intersects(block)) {
                if (jumping) {
                    setY(block.getMinY() + AppConfig.getTileSize() + OFFSET);
                    yMotionPosition = 0;
                } else if (falling) {
                    setY(block.getMinY() - playerHeight - OFFSET);
                    isJumping = false;
                    yMotionPosition = 0;
                }
            }
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

    public void jump() {
        if (!isJumping) {
            yMotionPosition -= JUMP_HEIGHT;
        }
        isJumping = true;
    }

    ///
    /// Getters
    ///

    public boolean isMovingLeft() {
        return isMovingLeft;
    }

    public boolean isMovingRight() {
        return isMovingRight;
    }
}
