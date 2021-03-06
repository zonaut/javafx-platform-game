package com.zonaut.games.javafx.platform.entities;

import com.zonaut.games.javafx.platform.Config;
import com.zonaut.games.javafx.platform.common.Direction;
import com.zonaut.games.javafx.platform.level.Block;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import com.zonaut.games.javafx.platform.utils.ImageUtil;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Player extends AnimatedEntity {

    private static final double JUMP_HEIGHT = 750; // Tweak value to desired height
    private static final int BULLET_DELAY = 250;

    private final Image[] idleSprite;
    private final int idleSpriteDuration;
    private final Image[] walkRightSprite;
    private final int walkRightSpriteDuration;
    private final Image[] walkLeftSprite;
    private final int walkLeftSpriteDuration;

    private final double width;
    private final double height;

    private double yMotionPosition;

    private boolean isJumping;

    private final List<Bullet> bullets = new ArrayList<>();
    private long lastBulletFiredOn = System.currentTimeMillis();

    public Player(double x, double y, Direction direction, LevelLoader levelLoader) {
        super (x, y, 225, levelLoader);
        this.direction = direction;

        // TODO Use sprites for different player movement action like idle, walking, ...
        Image image = Config.getImage(Config.INSTANCE.images.player);
        // Define all available sprites for our player
        // TODO Get this from app config ???
        idleSprite = ImageUtil.getFrom(image, 0, 0, 32, 32, 3);
        idleSpriteDuration = 1500;
        walkRightSprite = ImageUtil.getFrom(image, 0, 32, 32, 32, 2);
        walkRightSpriteDuration = 300;
        walkLeftSprite = ImageUtil.getFrom(image, 0, 64, 32, 32, 2);
        walkLeftSpriteDuration = 300;
        // TODO Will the player always have the same constraints or should this be placed under updateImage?
        this.width = idleSprite[0].getWidth();
        this.height = idleSprite[0].getHeight();
        setFitWidth(width);
        setFitHeight(height);

        // Initial animation state of player
        animation = new Animation(idleSpriteDuration, idleSprite);
        animation.play();

        updateAnimation();
    }

    /**
     * Change player position based on it's environment and actions performed by the user.
     * The order of the checks and adjustments done here matter and can't be changed.
     * TODO This needs to be tweaked a bit + additions for future actions
     */
    @Override
    public void tick() {
        // Update the entities animation
        updateAnimation();

        // Move player on the X axis
        if (direction.equals(Direction.RIGHT)) {
            setX(getX() + speedX * DELAY);
        }
        if (direction.equals(Direction.LEFT)) {
            setX(getX() - speedX * DELAY);
        }
        // Don't let the player go out of our level on the X axis
        if (getX() < 0) {
            setX(0);
        }
        if (getX() > levelLoader.getLevelPixelWidth() - Config.INSTANCE.app.tileSize) {
            setX(levelLoader.getLevelPixelWidth() - Config.INSTANCE.app.tileSize);
        }

        // An offset is needed to reposition the player on collision otherwise the position is in the block
        double OFFSET = 0.01;

        // Check collisions with solid tiles on the X axis and reposition the player if it collides
        for (Block block : levelLoader.getBlocks()) {
            if (intersects(block)) {
                if (direction.equals(Direction.RIGHT) && !block.isCanPassWhenJumping()) {
                    setX(block.getMinX() - width - OFFSET);
                }
                if (direction.equals(Direction.LEFT) && !block.isCanPassWhenJumping()) {
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
                    setY(block.getMinY() + Config.INSTANCE.app.tileSize + OFFSET);
                    yMotionPosition = 0;
                } else if (falling) {
                    setY(block.getMinY() - height - OFFSET);
                    isJumping = false;
                    yMotionPosition = 0;
                }
            }
        }

    }

    // TODO Maybe we put this in their respective method like jump, stopMoving,  ... ?
    @Override
    protected void updateAnimation() {
        super.updateAnimation();
        if (direction.equals(Direction.RIGHT)) {
            animation.setFrames(walkRightSpriteDuration, walkRightSprite);
        } else if (direction.equals(Direction.LEFT)) {
            animation.setFrames(walkLeftSpriteDuration, walkLeftSprite);
        } else {
            animation.setFrames(idleSpriteDuration, idleSprite);
        }
    }

    public void stopMovingRightOrLeft() {
        direction = Direction.IDLE; // TODO Is this really idle? We could still be in the air
    }

    public void jump() {
        if (!isJumping) {
            yMotionPosition -= JUMP_HEIGHT;
        }
        isJumping = true;
    }

    public void shoot() {
        if (System.currentTimeMillis() < lastBulletFiredOn + BULLET_DELAY) {
            return;
        }
        LOG.info("Shooting bullet ...");
        Bullet bullet = new Bullet(getX(), getY(), direction, levelLoader);
        bullets.add(bullet);
        levelLoader.getCurrentLevel().getChildren().add(bullet);
        lastBulletFiredOn = System.currentTimeMillis();
    }

    ///
    /// Getters
    ///

    public List<Bullet> getBullets() {
        return bullets;
    }
}
