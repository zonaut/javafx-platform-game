package com.zonaut.games.javafx.platform.screens;

import com.zonaut.games.javafx.platform.config.AppConfig;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public abstract class AbstractScreen implements Screen {

    protected final Font font = Font.font("Monospace", FontWeight.BOLD, 20);
    protected final Font fontBold = Font.font("Monospace", FontWeight.BOLD, 28);
    protected final Insets wrapperInsets = new Insets(5, 8, 5, 8);
    protected final Background wrapperBackground = new Background(new BackgroundFill(Color.rgb(0, 0, 0, 0.4), CornerRadii.EMPTY, Insets.EMPTY));
    protected final Border wrapperBorder = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderStroke.DEFAULT_WIDTHS));
    protected final Color textColor = Color.WHITESMOKE;

    protected Stage stage;
    protected Scene scene;

    abstract void attachKeyHandler();

    @Override
    public void display(Stage stage) {
        this.stage = stage;

        stage.setScene(scene);
        stage.setHeight(AppConfig.getWindowHeight());
        stage.setWidth(AppConfig.getWindowWidth());

        attachKeyHandler();

        stage.show();
    }

}
