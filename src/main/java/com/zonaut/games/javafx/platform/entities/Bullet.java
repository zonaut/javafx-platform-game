package com.zonaut.games.javafx.platform.entities;

import com.zonaut.games.javafx.platform.Config;
import com.zonaut.games.javafx.platform.level.Block;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bullet extends ImageView {

    private static final Logger LOG = LogManager.getLogger(Bullet.class);

    private static final double DELAY = 1.0 / Config.INSTANCE.app.fps;
    private static final int BULLET_SPEED = 400;

    private LevelLoader levelLoader;
    private Player player;
    private boolean isFacingRight;

    public Bullet(double x, double y, boolean isFacingRight, LevelLoader levelLoader, Player player) {
        Image image = Config.getImage(Config.INSTANCE.images.bullet);
        setImage(image);

        setFitWidth(image.getWidth());
        setFitHeight(image.getHeight());

        this.isFacingRight = isFacingRight;
        this.levelLoader = levelLoader;
        this.player = player;

        setX(x);

        // TODO Tweak the starting position of the bullet a bit
        setY(y + (player.getHeight() / 2) - (image.getHeight() / 2));

        tick();
    }

    public void tick() {
        if (isFacingRight) {
            setX(getX() + BULLET_SPEED * DELAY);
        } else {
            setX(getX() - BULLET_SPEED * DELAY);
        }
    }

    public boolean isOutOfLevelBounds() {
        if (getX() <= 0 || getX() >= levelLoader.getLevelPixelWidth()) {
            return true;
        }
        // TODO maybe this could be moved to the engine and update it from there, we are now looping in different places
        for (Block block : levelLoader.getBlocks()) {
            if (intersects(block)) {
                return true;
            }
        }
        return false;
    }

}
