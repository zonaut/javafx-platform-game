package com.zonaut.games.javafx.platform.screens;

import com.zonaut.games.javafx.platform.Config;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MenuScreen extends AbstractScreen {

    private final int menuWrapperWidth = 600;
    private final int menuWrapperHeight = 400;
    private final int menuWrapperX = (Config.INSTANCE.app.windowWidth / 2) - (menuWrapperWidth / 2);
    private final int menuWrapperY = (Config.INSTANCE.app.windowHeight / 2) - (menuWrapperHeight / 2);

    public MenuScreen() {
        Pane sceneRoot = new Pane();
        this.scene = new Scene(sceneRoot, Config.INSTANCE.app.windowWidth, Config.INSTANCE.app.windowHeight);

        VBox centerWrapper = new VBox(10);
        centerWrapper.setMinWidth(menuWrapperWidth);
        centerWrapper.setMinHeight(menuWrapperHeight);
        centerWrapper.setTranslateX(menuWrapperX);
        centerWrapper.setTranslateY(menuWrapperY);
        centerWrapper.setBackground(wrapperBackground);
        centerWrapper.setBorder(wrapperBorder);
        centerWrapper.setPadding(wrapperInsets);
        centerWrapper.setAlignment(Pos.CENTER);

        Text title = new Text(Config.INSTANCE.app.title + " - " + Config.INSTANCE.app.version);
        title.setFont(fontBold);
        title.setFill(textColor);
        HBox titleWrapper = new HBox();
        titleWrapper.setAlignment(Pos.CENTER);
        titleWrapper.getChildren().addAll(title);

        Text menuNew = new Text("(N)ew game");
        menuNew.setFont(font);
        menuNew.setFill(textColor);

        Text menuOptions = new Text("(O)ptions");
        menuOptions.setFont(font);
        menuOptions.setFill(textColor);

        Text menuQuit = new Text("(Q)uit");
        menuQuit.setFont(font);
        menuQuit.setFill(textColor);

        VBox menuWrapper = new VBox(10);
        menuWrapper.setAlignment(Pos.CENTER);
        menuWrapper.getChildren().addAll(menuNew, menuOptions, menuQuit);

        centerWrapper.getChildren().addAll(titleWrapper, menuWrapper);

        sceneRoot.getChildren().addAll(centerWrapper);
    }

    @Override
    void attachKeyHandler() {
        scene.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode().equals(KeyCode.N)) {
                ScreenType.switchScreenTo(ScreenType.NEW, stage);
            }
            if (keyEvent.getCode().equals(KeyCode.O)) {
                ScreenType.switchScreenTo(ScreenType.OPTIONS, stage);
            }
            if (keyEvent.getCode().equals(KeyCode.Q)) {
                System.exit(0);
                this.stage.close();
            }
        });
    }
}
