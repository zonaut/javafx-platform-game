package com.zonaut.games.javafx.platform.entities.player;

import com.zonaut.games.javafx.platform.config.AppConfig;
import com.zonaut.games.javafx.platform.level.Block;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import com.zonaut.games.javafx.platform.screens.NewGameScreen;
import com.zonaut.games.javafx.platform.utils.ImageUtil;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Player extends ImageView {

    private static final Logger LOG = LogManager.getLogger(Player.class);

    private static final double DELAY = 1.0 / AppConfig.getFps();

    private static final double SPEED_X = 280; // Tweak value to desired speed
    private static final double JUMP_HEIGHT = 750; // Tweak value to desired height
    private static final double GRAVITY_ACCELERATION = 2300; // Tweak value to desired speed of acceleration

    private LevelLoader levelLoader;

    private Image[] playerIdleSprite;
    private Image[] playerWalkRightSprite;
    private Image[] playerWalkLeftSprite;
    private int width = 32;
    private int height = 32;

    private double yMotionPosition;

    private boolean isMovingLeft;
    private boolean isMovingRight;
    private boolean isJumping;

    private boolean isFacingRight;

    private List<Bullet> bullets = new ArrayList<>();
    private long bulletDelay = 200;
    private long lastBulletFiredOn = System.currentTimeMillis();

    public Player(LevelLoader levelLoader, double x, double y, boolean isFacingRight) {
        this.levelLoader = levelLoader;

        setX(x);
        setY(y);

        this.isFacingRight = isFacingRight;

        // TODO Use sprites for different player movement action like idle, walking, ...
        Image image = new Image(NewGameScreen.class.getResourceAsStream(AppConfig.getPlayerImage()));

        playerIdleSprite = ImageUtil.getFrom(image, 0, 0, 32, 32, 2);
        playerWalkRightSprite = ImageUtil.getFrom(image, 0, 32, 32, 32, 1);
        playerWalkLeftSprite = ImageUtil.getFrom(image, 0, 64, 32, 32, 1);

        setFitWidth(width);
        setFitHeight(height);

        updateImage();
    }

    /**
     * Change player position based on it's environment and actions performed by the user.
     * The order of the checks and adjustments done here matter and can't be changed.
     * TODO This needs to be tweaked a bit + additions for future actions
     */
    public void tick() {
        // Set correct image
        updateImage();
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
        for (Block block : levelLoader.getBlocks()) {
            if (intersects(block)) {
                if (isMovingRight && !block.isCanPassWhenJumping()) {
                    setX(block.getMinX() - width - OFFSET);
                }
                if (isMovingLeft && !block.isCanPassWhenJumping()) {
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
        for (Block block : levelLoader.getBlocks()) {
            if (intersects(block)) {
                if (jumping && !block.isCanPassWhenJumping()) {
                    setY(block.getMinY() + AppConfig.getTileSize() + OFFSET);
                    yMotionPosition = 0;
                } else if (falling) {
                    setY(block.getMinY() - height - OFFSET);
                    isJumping = false;
                    yMotionPosition = 0;
                }
            }
        }

    }

    // TODO Do more things here like movement based on the sprite.
    //      Also other states like idle, jumping, hurt, resetting player, ...
    private void updateImage() {
        if (isFacingRight) {
            setImage(playerWalkRightSprite[0]);
        } else {
            setImage(playerWalkLeftSprite[0]);
        }
    }

    public void moveRight() {
        isMovingRight = true;
        isMovingLeft = false;
        isFacingRight = true;
    }

    public void moveLeft() {
        isMovingLeft = true;
        isMovingRight = false;
        isFacingRight = false;
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

    public void shoot() {
        if (System.currentTimeMillis() < lastBulletFiredOn + bulletDelay) {
            return;
        }
        LOG.info("Shooting bullet ...");
        Bullet bullet = new Bullet(getX(), getY(), isFacingRight, levelLoader, this);
        bullets.add(bullet);
        levelLoader.getCurrentLevel().getChildren().add(bullet);
        lastBulletFiredOn = System.currentTimeMillis();
    }

    ///
    /// Getters
    ///

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public List<Bullet> getBullets() {
        return bullets;
    }
}
