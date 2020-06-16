package com.zonaut.games.javafx.platform.config;

import javafx.scene.paint.Color;

import java.io.InputStream;
import java.util.Properties;

public class LevelConfig {

    private static final Properties PROPERTIES = new Properties();

    private int levelNumber;

    private String levelTitle;
    private String levelStory;
    private Color levelBackgroundColor;

    public LevelConfig(String file, int levelNumber) {
        this.levelNumber = levelNumber;

        try (InputStream inputStream = AppConfig.class.getResourceAsStream(file)) {
            if (inputStream == null) {
                throw new RuntimeException();
            }

            PROPERTIES.load(inputStream);

            levelTitle = parseString("level.title");
            levelStory = parseString("level.story");

            String hexValue = parseString("level.background.color");
            levelBackgroundColor = Color.web(hexValue,1.0);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static String parseString(String property) {
        return PROPERTIES.getProperty(property);
    }

    static int parseInt(String property) {
        final String value = PROPERTIES.getProperty(property);
        return Integer.parseInt(value);
    }

    static boolean parseBoolean(String property) {
        final String value = PROPERTIES.getProperty(property);
        return Boolean.parseBoolean(value);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getLevelTitle() {
        return levelTitle;
    }

    public String getLevelStory() {
        return levelStory;
    }

    public Color getLevelBackgroundColor() {
        return levelBackgroundColor;
    }
}
