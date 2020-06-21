package com.zonaut.games.javafx.platform.entities;

import com.zonaut.games.javafx.platform.Config;
import com.zonaut.games.javafx.platform.common.Direction;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import com.zonaut.games.javafx.platform.utils.ImageUtil;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;

public class Crawler extends AnimatedEntity {

    public Crawler(double x, double y, LevelLoader levelLoader) {
        super (x, y, 100, levelLoader);

        Image image = Config.getImage(Config.INSTANCE.images.crawler);
        Image[] crawlerSprite = ImageUtil.getFrom(image, 0, 0, 45, 25, 4);
        int crawlerSpriteDuration = 2000;
        setFitWidth(crawlerSprite[0].getWidth());
        setFitHeight(crawlerSprite[0].getHeight());

        animation = new Animation(crawlerSpriteDuration, crawlerSprite);
        animation.play();
    }

    @Override
    public void tick() {
        updateAnimation();

        if (getX() <= 0) {
            direction = Direction.RIGHT;
        }
        if (getX() >= levelLoader.getLevelPixelWidth()) {
            direction = Direction.LEFT;
        }

        ////////////////////////////////////////////////////////////////////////

        // TODO Use object layers to create these blocking objects so we don't need to do calculations
        //      We can also create object layers to position these crawlers and other starting positions
        //      We can use the type property in the object to indicate it's purpose and map it easily to an enum if needed
        // E.g. for bound to check if the crawler needs to change positions
        Bounds bound1 = new BoundingBox(416, 1216, 64, 32);
        Bounds bound2 = new BoundingBox(1248, 1216, 64, 32);

        if (intersects(bound1) || intersects(bound2)) {
            LOG.info("Crawler needs to change direction");
            if (direction.equals(Direction.RIGHT)) {
                direction = Direction.LEFT;
            } else {
                direction = Direction.RIGHT;
            }
        }

        ////////////////////////////////////////////////////////////////////////

        // Move
        if (direction.equals(Direction.RIGHT)) {
            setX(getX() + speedX * DELAY);
        } else {
            setX(getX() - speedX * DELAY);
        }

    }

}
