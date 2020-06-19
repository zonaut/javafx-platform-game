package com.zonaut.games.javafx.platform.entities;

import com.zonaut.games.javafx.platform.config.AppConfig;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Crawler extends ImageView {

    private static final Logger LOG = LogManager.getLogger(Crawler.class);

    private static final double DELAY = 1.0 / AppConfig.getFps();
    private static final double SPEED = 100;

    private final LevelLoader levelLoader;

    private double width;
    private double height;

    private boolean isMovingLeft;
    private boolean isMovingRight;

    public Crawler(LevelLoader levelLoader, double x, double y) {
        this.levelLoader = levelLoader;

        setX(x);
        setY(y);

        Image image = new Image(Crawler.class.getResourceAsStream(AppConfig.getCrawlerImage()));

        this.width = image.getWidth();
        this.height = image.getHeight();
        setFitWidth(width);
        setFitHeight(height);

        setImage(image);

        isMovingRight = true;
    }

    public void tick() {

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

}
