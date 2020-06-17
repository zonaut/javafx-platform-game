package com.zonaut.games.javafx.platform.screens;

import com.zonaut.games.javafx.platform.config.AppConfig;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
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
                ScreenType.loadLevel(stage, 1);
            }
        });
    }


}
