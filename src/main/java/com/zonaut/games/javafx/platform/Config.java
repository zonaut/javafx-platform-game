package com.zonaut.games.javafx.platform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

public final class Config {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper(new YAMLFactory());

    private static final String UNIX_FS = "/";
    private static final String RESOURCES_PATH = replaceFileSeparator("src/main/resources/");
    private static final String PROPERTIES_FILE = "application.yml";

    public static final Config INSTANCE;
    public static LevelConfig LEVEL;

    private Config() {}

    static {
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            INSTANCE = OBJECT_MAPPER.readValue(inputStream, Config.class);
        } catch (IOException e) {
            throw new RuntimeException("Can't process application.yml!", e);
        }
    }

    public AppConfig app;
    public ImagesConfig images;

    ///
    /// Helpers
    ///

    public static String replaceFileSeparator(String value) {
        return value.replace(UNIX_FS, File.separator);
    }

    public static Image getImage(String path) {
        final InputStream inputStream = Config.class.getResourceAsStream(replaceFileSeparator(UNIX_FS + path));
        return new Image(inputStream);
    }

    public static void loadLevelConfig(int value) {
        final String properties = MessageFormat.format(INSTANCE.app.levelConfig, value);
        try (InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(replaceFileSeparator(properties))) {
            LEVEL = OBJECT_MAPPER.readValue(inputStream, LevelConfig.class);
        } catch (IOException e) {
            throw new RuntimeException("Can't process application.yml!", e);
        }
    }

    ///
    /// Configuration Classes
    ///

    public static class AppConfig {
        public String title;
        public String version;

        @JsonProperty("debug")
        public boolean isDebugEnabled;

        public double fps;
        public int tileSize;

        public int windowWidth;
        public int windowHeight;

        public String levelConfig;
        public String levelMap;

        public double getFpsInterval() {
            return 1000 / fps;
        }
    }

    public static class ImagesConfig {
        public String icon;
        public String player;
        public String bullet;
        public String crawler;
    }

    public static class LevelConfig {
        public int id;
        public String title;
        public String story;
        public Color backgroundColor;

        public String getLevelMapPath() {
            final String path = RESOURCES_PATH + MessageFormat.format(INSTANCE.app.levelMap, LEVEL.id);
            return replaceFileSeparator(path);
        }
    }

}
