package com.zonaut.games.javafx.platform.screens.level;

import com.zonaut.games.javafx.platform.config.AppConfig;
import com.zonaut.games.javafx.platform.entities.Player;
import com.zonaut.games.javafx.platform.level.LevelKeyInputHandler;
import com.zonaut.games.javafx.platform.level.LevelLoader;
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

        // TODO create a game state manager that we inject and use as a global way of accessing things and states
        levelLoader = new LevelLoader(levelNumber);

        currentLevel = new Group();
        sceneRoot.getChildren().clear();
        sceneRoot.getChildren().add(currentLevel);

        levelLoader.drawLayersOn(currentLevel);

        // TODO Temp player start position, get this from level information
        double x = 4 * 32;
        double y = 59 * 32 - 32; // bottom row - one row

        player = new Player(x, y);
        currentLevel.getChildren().add(player);

        levelKeyInputHandler = new LevelKeyInputHandler(scene, player);

        // TODO Simple loop at 60 frames a second as set in the application.properties
        double targetFpsOffset = 1000 / AppConfig.getFps();
        KeyFrame frame = new KeyFrame(Duration.millis(targetFpsOffset), e -> loop());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();

        // TODO use background color for now, maybe background or parallax in future
        scene.setFill(levelLoader.getLevelConfig().getLevelBackgroundColor());

        stage.show();
    }

    /**
     * Game loop
     */
    private void loop() {

        // Check active input and call appropriate methods for it.
        levelKeyInputHandler.update();

        // Update the players position based on changes that are made by input or other factors
        player.tick();

        panPlayer();

    }

    // TODO Better pan implementation
    private void panPlayer() {

        int sceneHeight = AppConfig.getWindowHeight();
        int sceneWidth = AppConfig.getWindowWidth();

        // Y position
        double offsetY = -1 * levelLoader.getLevelPixelHeight() + sceneHeight - AppConfig.getTileSize();
        currentLevel.setLayoutY(offsetY);

    }

}
