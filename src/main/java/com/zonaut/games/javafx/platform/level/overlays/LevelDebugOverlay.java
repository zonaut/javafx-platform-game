package com.zonaut.games.javafx.platform.level.overlays;

import com.zonaut.games.javafx.platform.Config;
import com.zonaut.games.javafx.platform.entities.Player;
import com.zonaut.games.javafx.platform.level.LevelLoader;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.time.Duration;
import java.time.Instant;

public class LevelDebugOverlay extends Pane {

    public static Background RED = new Background(new BackgroundFill(Color.web("d34251"), CornerRadii.EMPTY, Insets.EMPTY));
    public static Background GREEN = new Background(new BackgroundFill(Color.web("87ba4e"), CornerRadii.EMPTY, Insets.EMPTY));
    public static Background ORANGE = new Background(new BackgroundFill(Color.web("eeb540"), CornerRadii.EMPTY, Insets.EMPTY));
    public static Background PURPLE = new Background(new BackgroundFill(Color.web("9177d4"), CornerRadii.EMPTY, Insets.EMPTY));

    private final Font font = Font.font("Monospace", FontWeight.NORMAL, 14);
    private final Font fontBold = Font.font("Monospace", FontWeight.BOLD, 14);

    private final VBox debugInformation;

    private Text levelTitle;
    private Text levelDimensions;
    private Text playerPosition;

    private VBox messageWrapper;
    private Text message;

    private Instant lastMessage = Instant.now();

    public LevelDebugOverlay() {

        debugInformation = new VBox(10);
        debugInformation.setTranslateX(20);
        debugInformation.setTranslateY(20);
        debugInformation.setMinWidth(600);
        debugInformation.setPadding(new Insets(10));
        debugInformation.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.4), CornerRadii.EMPTY, Insets.EMPTY)));

        levelTitle = new Text();
        levelTitle.setFont(font);
        levelTitle.setFill(Color.WHITESMOKE);

        levelDimensions = new Text();
        levelDimensions.setFont(font);
        levelDimensions.setFill(Color.WHITESMOKE);

        playerPosition = new Text();
        playerPosition.setFont(font);
        playerPosition.setFill(Color.WHITESMOKE);

        messageWrapper = new VBox(10);
        messageWrapper.setPadding(new Insets(10));
        message = new Text();
        message.setFont(fontBold);
        message.setFill(Color.WHITESMOKE);
        messageWrapper.getChildren().add(message);

        debugInformation.getChildren().addAll(
                levelTitle,
                levelDimensions,
                playerPosition,
                messageWrapper
        );

        getChildren().addAll(debugInformation);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastMessage.isBefore(Instant.now().minus(Duration.ofMillis(3000)))) {
                    message.setText("");
                    debugInformation.getChildren().remove(messageWrapper);
                }
            }
        };
        timer.start();
    }

    public void updateLevelName(LevelLoader levelLoader) {
        this.levelTitle.setText("Level " + Config.LEVEL.id + " : " + Config.LEVEL.title);
        this.levelDimensions.setText("Level dimensions (PX) : W " + levelLoader.getLevelPixelWidth() + ", H " + levelLoader.getLevelPixelHeight());
    }

    public void updatePlayerPosition(Player player) {
        this.playerPosition.setText("Player position : X " + player.getX() + ", Y " + player.getY());
    }

    public void showMessage(String message, Background background) {
        if (!debugInformation.getChildren().contains(messageWrapper)) {
            debugInformation.getChildren().add(messageWrapper);
        }
        this.message.setText(message);
        this.messageWrapper.setBackground(background);
        lastMessage = Instant.now();
    }

}
