package com.zonaut.games.javafx.platform;

import com.zonaut.games.javafx.platform.screens.ScreenType;
import javafx.application.Application;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class App extends Application {

    private static final Logger LOG = LogManager.getLogger(App.class);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle(Config.INSTANCE.app.title + " - " + Config.INSTANCE.app.version);

        primaryStage.setResizable(false);
        primaryStage.setWidth(Config.INSTANCE.app.windowWidth);
        primaryStage.setHeight(Config.INSTANCE.app.windowHeight);
        primaryStage.centerOnScreen();

        primaryStage.getIcons().add(Config.getImage(Config.INSTANCE.images.icon));

        primaryStage.setOnCloseRequest(windowEvent -> {
            windowEvent.consume();
            System.exit(0);
            primaryStage.close();
        });

        ScreenType.switchScreenTo(ScreenType.MENU, primaryStage);

        LOG.debug("App has started.");

    }

}
