package com.zonaut.games.javafx.platform.entities;

import com.zonaut.games.javafx.platform.Config;
import com.zonaut.games.javafx.platform.common.Direction;
import com.zonaut.games.javafx.platform.level.Block;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import com.zonaut.games.javafx.platform.utils.ImageUtil;
import javafx.scene.image.Image;

public class Bullet extends AnimatedEntity {

    public Bullet(double x, double y, Direction direction, LevelLoader levelLoader) {
        super (x, y, 400, levelLoader);
        this.direction = direction;
        this.levelLoader = levelLoader;

        Image image = Config.getImage(Config.INSTANCE.images.bullet);
        Image[] bulletSprite = ImageUtil.getFrom(image, 0, 0, 10, 6, 4);
        int bulletSpriteDuration = 500;
        setFitWidth(bulletSprite[0].getWidth());
        setFitHeight(bulletSprite[0].getHeight());

        // TODO Tweak the starting position of the bullet a bit
        setY(y + 16);

        animation = new Animation(bulletSpriteDuration, bulletSprite);
        animation.play();

        tick();
    }

    @Override
    public void tick() {
        updateAnimation();
        if (direction.equals(Direction.LEFT)) {
            setX(getX() - speedX * DELAY);
        } else {
            setX(getX() + speedX * DELAY);
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
