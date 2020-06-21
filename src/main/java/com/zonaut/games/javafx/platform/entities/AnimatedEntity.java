package com.zonaut.games.javafx.platform.entities;

import com.zonaut.games.javafx.platform.Config;
import com.zonaut.games.javafx.platform.common.Direction;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AnimatedEntity extends ImageView {

    protected static final Logger LOG = LogManager.getLogger(AnimatedEntity.class);

    protected static final double DELAY = 1.0 / Config.INSTANCE.app.fps;
    protected static final double GRAVITY_ACCELERATION = 2300; // Tweak value to desired speed of acceleration

    protected final double speedX;

    protected Direction direction = Direction.RIGHT;

    protected Animation animation;
    protected LevelLoader levelLoader;

    public AnimatedEntity(double x, double y, double speed, LevelLoader levelLoader) {
        setX(x);
        setY(y);
        speedX = speed;
        this.levelLoader = levelLoader;
    }

    abstract void tick();

    protected void updateAnimation() {
        setImage(animation.getImage());
    }

    public void moveRight() {
        direction = Direction.RIGHT;
    }

    public void moveLeft() {
        direction = Direction.LEFT;
    }

}
