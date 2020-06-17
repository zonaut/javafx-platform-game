package com.zonaut.games.javafx.platform.level;

import com.zonaut.games.javafx.platform.config.AppConfig;
import com.zonaut.games.javafx.platform.entities.Bullet;
import com.zonaut.games.javafx.platform.entities.Player;
import com.zonaut.games.javafx.platform.level.overlays.LevelDebugOverlay;
import com.zonaut.games.javafx.platform.screens.Screen;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LevelScreen implements Screen {

    private static final Logger LOG = LogManager.getLogger(LevelScreen.class);

    private Stage stage;
    private Scene scene;

    private int levelNumber;

    private Pane sceneRoot = new Pane();
    private Group currentLevel;

    private LevelLoader levelLoader;

    private LevelKeyInputHandler levelKeyInputHandler;

    private Player player;
    private double playerStartPositionX;
    private double playerStartPositionY;

    private LevelDebugOverlay levelDebugOverlay;

    public LevelScreen(int levelNumber) {
        this.scene = new Scene(sceneRoot, AppConfig.getWindowWidth(), AppConfig.getWindowHeight());
        this.levelNumber = levelNumber;
    }

    @Override
    public void display(Stage stage) {
        this.stage = stage;

        stage.setScene(scene);
        stage.setHeight(AppConfig.getWindowHeight());
        stage.setWidth(AppConfig.getWindowWidth());

        currentLevel = new Group();

        // TODO create a game state manager that we inject and use as a global way of accessing things and states
        levelLoader = new LevelLoader(currentLevel, levelNumber);

        sceneRoot.getChildren().clear();
        sceneRoot.getChildren().add(currentLevel);

        // TODO Temp player start position, get this from level information
        playerStartPositionX = 0;
        int playerStartPositionYOffset = 10; // An offset to make sure we don't start into a tile due to gravity
        playerStartPositionY = (levelLoader.getMapHeight() -2) * AppConfig.getTileSize() - playerStartPositionYOffset;

        player = new Player(levelLoader, playerStartPositionX, playerStartPositionY);
        currentLevel.getChildren().add(player);

        levelKeyInputHandler = new LevelKeyInputHandler(scene, player);

        // TODO Simple loop at 60 frames a second as set in the application.properties
        KeyFrame frame = new KeyFrame(Duration.millis(AppConfig.getFpsInterval()), e -> loop());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();

        // TODO use background color for now, maybe background or parallax in future
        scene.setFill(levelLoader.getLevelConfig().getLevelBackgroundColor());

        levelDebugOverlay = new LevelDebugOverlay();
        levelDebugOverlay.updateLevelName(levelLoader);
        sceneRoot.getChildren().add(levelDebugOverlay);

        stage.show();
    }

    /**
     * Game loop.
     */
    private void loop() {

        // Check active input and call appropriate methods for it.
        levelKeyInputHandler.handleCurrentInput();

        // Update the players position based on changes that are made by input or other factors
        player.tick();

        panPlayerInViewport();

        ///
        /// TODO test
        ///

        levelDebugOverlay.updatePlayerPosition(player);

        // Player fell in hole
        if (player.getY() > levelLoader.getLevelPixelHeight()) {
            player.setX(playerStartPositionX);
            player.setY(playerStartPositionY);
            currentLevel.setLayoutX(playerStartPositionX);
            levelDebugOverlay.showMessage("You just died ! Resetting player to start position.", false);
        }

        // Collect coins if we intersect them
        // TODO Move this to player or keep in loop ?
        levelLoader.getCollectibles().removeIf(block -> {
            if (player.intersects(block.getBoundsInParent())) {
                currentLevel.getChildren().remove(block);
                String message = String.format("Picked up a coin on X %f - Y %f", block.getBoundsInParent().getMinX(), block.getBoundsInParent().getMinY());
                levelDebugOverlay.showMessage(message, true);
                return true;
            }
            return false;
        });

        // Loop over each bullet and remove it if it is out of the level
        player.getBullets().removeIf(bullet -> {
            if (bullet.isOutOfLevelBounds()) {
                currentLevel.getChildren().remove(bullet);
                return true;
            }
            return false;
        });
        // Update bullets
        for (Bullet bullet : player.getBullets()) {
            bullet.tick();
        }
    }

    /**
     * Pan the player in the viewport of the application window.
     * Keep the order of the positions set as one depends on the other for correct panning.
     * TODO
     *      - Can we calculate this offset correction?
     *      - Is 1/3rd better on the X axis, if yes make it work correctly for both side
     */
    private void panPlayerInViewport() {

        int sceneHeight = AppConfig.getWindowHeight();
        int sceneWidth = AppConfig.getWindowWidth();
        int levelHeight = levelLoader.getLevelPixelHeight();
        int levelWidth = levelLoader.getLevelPixelWidth();
        int offsetCorrection = 24;

        //
        // Y position
        //

        // Set the player in the middle of the viewport
        double offsetY = sceneHeight * 1.0 / 2;
        currentLevel.setLayoutY(-1 * player.getY() + offsetY);

        // keep the player on the bottom of our viewport when we are located at the bottom of the level.
        double minLayoutY = -1 * levelHeight + sceneHeight - offsetCorrection;
        if (currentLevel.getLayoutY() < minLayoutY) {
            currentLevel.setLayoutY(minLayoutY);
        }

        //
        // X position
        //

        // Keep the player in the middle of the viewport
        double offsetX = sceneWidth * 1.0 / 2;

        // Keep the levels left side end within the viewport
        if (player.getX() < offsetX) {
            return;
        }

        // Keep the player on the position in the viewport based on the offset
        currentLevel.setLayoutX(-1 * player.getX() + offsetX);

        // Keep the levels right side end within the viewport
        double minLayoutX = -1 * levelWidth + sceneWidth;
        if (currentLevel.getLayoutX() < minLayoutX) {
            currentLevel.setLayoutX(minLayoutX);
        }

    }

}
