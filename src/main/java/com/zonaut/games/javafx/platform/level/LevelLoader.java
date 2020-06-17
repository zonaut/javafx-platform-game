package com.zonaut.games.javafx.platform.level;

import com.zonaut.games.javafx.platform.config.AppConfig;
import com.zonaut.games.javafx.platform.config.LevelConfig;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mapeditor.core.Map;
import org.mapeditor.core.MapLayer;
import org.mapeditor.core.Tile;
import org.mapeditor.core.TileLayer;
import org.mapeditor.io.TMXMapReader;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LevelLoader {

    private static final Logger LOG = LogManager.getLogger(LevelLoader.class);

    private static final List<Integer> SOLID_TILE_IDS = List.of(2, 3);
    private static final List<Integer> PASSABLE_TILE_IDS = List.of(4);
    private static final List<Integer> COLLECTIBALE_TILE_IDS = List.of(5);

    private Group currentLevel;

    private LevelConfig levelConfig;

    private int mapWidth;
    private int mapHeight;

    private int levelPixelWidth;
    private int levelPixelHeight;

    private HashMap<Integer, Image> tiles = new HashMap<>();

    private List<Block> blocks = new ArrayList<>();

    private List<ImageView> collectibles = new ArrayList<>();

    public LevelLoader(Group currentLevel, int levelNumber) {

        levelConfig = new LevelConfig(AppConfig.getLevelPath() + levelNumber + AppConfig.getLevelPropertiesFile(), levelNumber);
        LOG.info("Loading level {} : {} ... ", levelNumber, levelConfig.getLevelTitle());

        String filename = AppConfig.getResourcesPath() + AppConfig.getLevelPath() + levelNumber + AppConfig.getLevelFilePath();
        try {
            TMXMapReader mapReader = new TMXMapReader();

            Map map = mapReader.readMap(filename);

            this.currentLevel = currentLevel;

            mapWidth = map.getWidth();
            mapHeight = map.getHeight();

            levelPixelHeight = mapHeight * AppConfig.getTileSize();
            levelPixelWidth = mapWidth * AppConfig.getTileSize();

            // TODO Fixed names for layers so we know what layer does what
            String BASE_LAYER_NAME = "base-layer";

            for (MapLayer layer : map.getLayers()) {
                LOG.info("Loading layer{} ...", layer.getName());

                // TODO Get correct layer class type
                TileLayer tileLayer = (TileLayer) layer;

                for (int y = 0; y < mapHeight; y++) {
                    for (int x = 0; x < mapWidth; x++) {

                        double positionX = x *  AppConfig.getTileSize();
                        double positionY = y * AppConfig.getTileSize();

                        Tile tile = tileLayer.getTileAt(x, y);
                        // TODO A tile is null if we don't have a tile in place, should we always set a transparent tile?
                        if (tile != null) {
                            int tileId = tile.getId() + 1;

                            // TODO Convert awt image to JavaFX image and put it in a cache
                            if (!tiles.containsKey(tileId)) {
                                Image convertedImage = createImage(tile.getImage());
                                tiles.put(tileId, convertedImage);
                            }

                            Block block = new Block(positionX, positionY, AppConfig.getTileSize(), AppConfig.getTileSize(), tileId);
                            ImageView imageView = new ImageView(tiles.get(block.getId()));
                            imageView.setTranslateX(block.getMinX());
                            imageView.setTranslateY(block.getMinY());

                            if (SOLID_TILE_IDS.contains(tileId)) {
                                blocks.add(block);
                                currentLevel.getChildren().add(imageView);
                                LOG.info("Solid tile at position {}:{}", positionX, positionY);
                            }
                            if (PASSABLE_TILE_IDS.contains(tileId)) {
                                block.setCanPassWhenJumping(true);
                                blocks.add(block);
                                currentLevel.getChildren().add(imageView);
                                LOG.info("Passable tile at position {}:{}", positionX, positionY);
                            }
                            if (COLLECTIBALE_TILE_IDS.contains(tileId)) {
                                currentLevel.getChildren().add(imageView);
                                if (COLLECTIBALE_TILE_IDS.contains(block.getId())) {
                                    collectibles.add(imageView);
                                }
                                LOG.info("Coin tile at position {}:{}", positionX, positionY);
                            }
                        }
                    }
                }

                LOG.info("Layer {} loaded.", layer.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LOG.info("Level loaded and has created and cached {} tiles.", tiles.size());
        }
    }

    // TODO SwingFXUtils.toFXImage doesn't seem to work correctly, replacement until ...
    //      We could write a custom method to read the tileset image and copy each tile into the cache
    public static Image createImage(java.awt.Image image) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write((RenderedImage) image, "png", byteArrayOutputStream);
        byteArrayOutputStream.flush();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        return new Image(byteArrayInputStream);
    }

    ///
    /// Getters
    ///

    public LevelConfig getLevelConfig() {
        return levelConfig;
    }

    public Group getCurrentLevel() {
        return currentLevel;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public int getLevelPixelWidth() {
        return levelPixelWidth;
    }

    public int getLevelPixelHeight() {
        return levelPixelHeight;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public List<ImageView> getCollectibles() {
        return collectibles;
    }
}
