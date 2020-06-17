package com.zonaut.games.javafx.platform.config;

import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static final Properties PROPERTIES = new Properties();
    private static final String PROPERTIES_FILE = "application.properties";

    private static final String RESOURCES_PATH = "src/main/resources";

    private static String version;
    private static String title;

    private static boolean debugEnabled;

    private static int windowHeight;
    private static int windowWidth;

    private static String basePath;

    private static String icon;

    private static String levelPath;
    private static String levelFilePath;
    private static String levelPropertiesFile;

    private static int tileSize;

    private static double fps;
    private static double fpsInterval;

    static {
        try (InputStream inputStream = AppConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (inputStream == null) {
                throw new RuntimeException();
            }

            PROPERTIES.load(inputStream);

            version = parseString("app.version");
            title = parseString("app.title");

            debugEnabled = parseBoolean("app.debug.enabled");

            windowHeight = parseInt("app.window.height");
            windowWidth = parseInt("app.window.width");

            basePath = parseString("app.paths.base");

            icon = basePath + parseString("app.icon");

            levelPath = basePath + parseString("app.paths.level.path");
            levelFilePath = "/" + parseString("app.paths.level.file");
            levelPropertiesFile = "/" + parseString("app.paths.level.properties");

            tileSize = parseInt("app.tile.size");

            fps = parseInt("app.fps");
            fpsInterval = 1000 / fps;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    ///
    /// Helpers
    ///

    public static String parseString(String property) {
        return PROPERTIES.getProperty(property);
    }

    public static int parseInt(String property) {
        final String value = PROPERTIES.getProperty(property);
        return Integer.parseInt(value);
    }

    public static boolean parseBoolean(String property) {
        final String value = PROPERTIES.getProperty(property);
        return Boolean.parseBoolean(value);
    }

    ///
    /// Getters
    ///


    public static String getResourcesPath() {
        return RESOURCES_PATH;
    }

    public static String getVersion() {
        return version;
    }

    public static String getTitle() {
        return title;
    }

    public static boolean isDebugEnabled() {
        return debugEnabled;
    }

    public static int getWindowHeight() {
        return windowHeight;
    }

    public static int getWindowWidth() {
        return windowWidth;
    }

    public static String getBasePath() {
        return basePath;
    }

    public static String getIcon() {
        return icon;
    }

    public static String getLevelPath() {
        return levelPath;
    }

    public static String getLevelFilePath() {
        return levelFilePath;
    }

    public static String getLevelPropertiesFile() {
        return levelPropertiesFile;
    }

    public static int getTileSize() {
        return tileSize;
    }

    public static double getFps() {
        return fps;
    }

    public static double getFpsInterval() {
        return fpsInterval;
    }
}