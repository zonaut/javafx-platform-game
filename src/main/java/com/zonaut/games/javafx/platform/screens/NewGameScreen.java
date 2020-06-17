package com.zonaut.games.javafx.platform.screens;

import com.zonaut.games.javafx.platform.config.AppConfig;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import com.zonaut.games.javafx.platform.utils.ImageUtil;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class NewGameScreen extends AbstractScreen {

    private final int menuWrapperWidth = 600;
    private final int menuWrapperHeight = 400;
    private final int menuWrapperX = (AppConfig.getWindowWidth() / 2) - (menuWrapperWidth / 2);
    private final int menuWrapperY = (AppConfig.getWindowHeight() / 2) - (menuWrapperHeight / 2);

    private Group sceneRoot = new Group();

    public NewGameScreen() {
        this.scene = new Scene(sceneRoot, AppConfig.getWindowWidth(), AppConfig.getWindowHeight());

        VBox centerWrapper = new VBox(10);
        centerWrapper.setMinWidth(menuWrapperWidth);
        centerWrapper.setMinHeight(menuWrapperHeight);
        centerWrapper.setTranslateX(menuWrapperX);
        centerWrapper.setTranslateY(menuWrapperY);
        centerWrapper.setBackground(wrapperBackground);
        centerWrapper.setBorder(wrapperBorder);
        centerWrapper.setPadding(wrapperInsets);
        centerWrapper.setAlignment(Pos.CENTER);

        Text title = new Text("New game");
        title.setFont(fontBold);
        title.setFill(textColor);
        HBox titleWrapper = new HBox();
        titleWrapper.setAlignment(Pos.CENTER);
        titleWrapper.getChildren().addAll(title);

        Text menuBack = new Text("(G)o back");
        menuBack.setFont(font);
        menuBack.setFill(textColor);

        Text menuLoadLevel = new Text("(L)oad level 1");
        menuLoadLevel.setFont(font);
        menuLoadLevel.setFill(textColor);

        VBox menuWrapper = new VBox(10);
        menuWrapper.setAlignment(Pos.CENTER);
        menuWrapper.getChildren().addAll(menuBack, menuLoadLevel);

        centerWrapper.getChildren().addAll(titleWrapper, menuWrapper);

        sceneRoot.getChildren().addAll(centerWrapper);
    }

    @Override
    void attachKeyHandler() {
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.G)) {
                ScreenType.switchScreenTo(ScreenType.MENU, stage);
            }
            if (keyEvent.getCode().equals(KeyCode.L)) {
                //drawScreenWithoutCanvas();
                drawScreenInCanvas();
            }
        });
    }

    void drawScreenInCanvas() {
        final LevelLoader levelLoader = new LevelLoader(1);
        final Canvas canvas = new Canvas(levelLoader.getLevelPixelWidth(), levelLoader.getLevelPixelHeight());
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        // TODO background color, background image, parallax, ... ?
        graphicsContext.setFill(levelLoader.getLevelConfig().getLevelBackgroundColor());
        graphicsContext.fillRect(0 , 0 , levelLoader.getLevelPixelWidth(), levelLoader.getLevelPixelHeight());

        Group group = new Group();
        group.getChildren().add(canvas);

        sceneRoot.getChildren().clear();
        sceneRoot.getChildren().add(group);

        levelLoader.drawLayer(graphicsContext);

        double minLayoutY = -1 * levelLoader.getLevelPixelHeight() + AppConfig.getWindowHeight() - AppConfig.getTileSize() + (AppConfig.getTileSize() / 4);
        group.setLayoutY(minLayoutY);

        drawPLayer(graphicsContext);
    }

    void drawScreenWithoutCanvas() {
        final LevelLoader levelLoader = new LevelLoader(1);

        Group group = new Group();

        sceneRoot.getChildren().clear();
        sceneRoot.getChildren().add(group);

        levelLoader.drawLayer(group);

        double minLayoutY = -1 * levelLoader.getLevelPixelHeight() + AppConfig.getWindowHeight() - AppConfig.getTileSize() + (AppConfig.getTileSize() / 4);
        group.setLayoutY(minLayoutY);
    }

    void drawPLayer(GraphicsContext graphicsContext) {
        Image image = new Image(NewGameScreen.class.getResourceAsStream("/sprites/player.png"));
        Image[] sprite = ImageUtil.getFrom(image, 0, 0, 32, 32, 2);
        graphicsContext.drawImage(sprite[1], 4 * 32, 59 * 32);
    }
}
