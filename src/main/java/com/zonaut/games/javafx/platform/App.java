package com.zonaut.games.javafx.platform;

import com.zonaut.games.javafx.platform.config.AppConfig;
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

        primaryStage.setTitle(AppConfig.getTitle());

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

        // TODO Create and set a scene as a starting screen of the game
        //primaryStage.setScene();

        primaryStage.show();

        LOG.debug("App has started.");

    }

}
