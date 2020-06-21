package com.zonaut.games.javafx.platform.entities;

import com.zonaut.games.javafx.platform.Config;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import com.zonaut.games.javafx.platform.utils.ImageUtil;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Crawler extends ImageView {

    private static final Logger LOG = LogManager.getLogger(Crawler.class);

    private static final double DELAY = 1.0 / Config.INSTANCE.app.fps;
    private static final double SPEED = 100;

    private final LevelLoader levelLoader;

    private boolean isMovingLeft;
    private boolean isMovingRight;

    private final Image[] crawlerSprite;
    private final int crawlerSpriteDuration;

    private Animation animation;

    public Crawler(LevelLoader levelLoader, double x, double y) {
        this.levelLoader = levelLoader;

        setX(x);
        setY(y);

        Image image = Config.getImage(Config.INSTANCE.images.crawler);

        crawlerSprite = ImageUtil.getFrom(image, 0, 0, 45, 25, 4);
        crawlerSpriteDuration = 2000;

        setFitWidth(crawlerSprite[0].getWidth());
        setFitHeight(crawlerSprite[0].getHeight());

        isMovingRight = true;

        animation = new Animation(crawlerSpriteDuration, crawlerSprite);
        animation.play();
    }

    public void tick() {
        updateAnimation();

        if (getX() <= 0) {
            isMovingRight = true;
            isMovingLeft = false;
        }
        if (getX() >= levelLoader.getLevelPixelWidth()) {
            isMovingLeft = true;
            isMovingRight = false;
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
            isMovingLeft = !isMovingLeft;
            isMovingRight = !isMovingRight;
        }

        ////////////////////////////////////////////////////////////////////////

        // Move
        if (isMovingRight) {
            setX(getX() + SPEED * DELAY);
        } else {
            setX(getX() - SPEED * DELAY);
        }

    }

    public void updateAnimation() {
        setImage(animation.getImage());
    }

}
