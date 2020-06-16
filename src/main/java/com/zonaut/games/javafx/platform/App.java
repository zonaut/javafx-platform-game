package com.zonaut.games.javafx.platform;

import com.zonaut.games.javafx.platform.config.AppConfig;
import com.zonaut.games.javafx.platform.screens.ScreenType;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;

public class App extends Application {

    private static final Logger LOG = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle(AppConfig.getTitle() + " - " + AppConfig.getVersion());

        primaryStage.setResizable(false);
        primaryStage.setHeight(AppConfig.getWindowHeight());
        primaryStage.setWidth(AppConfig.getWindowWidth());
        primaryStage.centerOnScreen();

        InputStream iconStream = getClass().getResourceAsStream(AppConfig.getIcon());
        Image image = new Image(iconStream);
        primaryStage.getIcons().add(image);

        primaryStage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            System.exit(0);
            primaryStage.close();
        });

        ScreenType.switchScreenTo(ScreenType.MENU, primaryStage);

        LOG.debug("App has started.");

    }

}
