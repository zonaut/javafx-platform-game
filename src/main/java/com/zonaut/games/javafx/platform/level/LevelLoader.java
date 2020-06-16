package com.zonaut.games.javafx.platform.level;

import com.zonaut.games.javafx.platform.config.AppConfig;
import com.zonaut.games.javafx.platform.config.LevelConfig;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
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

    private LevelConfig levelConfig;

    private int levelPixelHeight;
    private int levelPixelWidth;

    private HashMap<Integer, Image> tiles = new HashMap<>();
    private List<Block> solidBlocks = new ArrayList<>();

    public LevelLoader(int levelNumber) {

        levelConfig = new LevelConfig(AppConfig.getLevelPath() + levelNumber + AppConfig.getLevelPropertiesFile(), levelNumber);
        LOG.info("Loading level {} : {} ... ", levelNumber, levelConfig.getLevelTitle());

        String filename = AppConfig.getResourcesPath() + AppConfig.getLevelPath() + levelNumber + AppConfig.getLevelFilePath();
        try {
            TMXMapReader mapReader = new TMXMapReader();

            Map map = mapReader.readMap(filename);

            int mapWidth = map.getWidth();
            int mapHeight = map.getHeight();

            levelPixelHeight = mapHeight * AppConfig.getTileSize();
            levelPixelWidth = mapWidth * AppConfig.getTileSize();

            List<Integer> SOLID_TILE_IDS = List.of(2);

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

                            if (SOLID_TILE_IDS.contains(tileId)) {
                                Block block = new Block(tileId, positionX, positionY, AppConfig.getTileSize(), AppConfig.getTileSize());
                                solidBlocks.add(block);
                                LOG.info("Solid tile at position {}:{}", positionX, positionY);
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

    public void drawLayer(Group group) {
        ImageView baseImageTile = new ImageView(tiles.get(2));
        baseImageTile.setTranslateX(0 * 32);
        baseImageTile.setTranslateY(59 * 32);
        group.getChildren().add(baseImageTile);
//        for (Block block : solidBlocks) {
//            ImageView baseImageTile = new ImageView(tiles.get(block.getId()));
//            baseImageTile.setTranslateX(block.getMinX());
//            baseImageTile.setTranslateY(block.getMinY());
//            group.getChildren().add(baseImageTile);
//        }
    }

    // TODO Just a test for now, draw correct tile on correct position
    public void drawLayer(GraphicsContext graphicsContext) {
        graphicsContext.drawImage(tiles.get(2), 0 * 32, 59 * 32);
//        for (int row = 0; row < levelPixelHeight / 32; row++) {
//            for (int col = 0; col <  levelPixelWidth / 32; col++) {
//                LOG.info("Drawing image on row {} col {}", row, col);
//                if ((row & 1) == 0) { // temp test draw on even rows
//                    // tiles.get(2) -> ID from tile
//                    graphicsContext.drawImage(tiles.get(2), col * 32, row * 32);
//                }
//            }
//        }
    }

    ///
    /// Getters
    ///

    public LevelConfig getLevelConfig() {
        return levelConfig;
    }

    public int getLevelPixelHeight() {
        return levelPixelHeight;
    }

    public int getLevelPixelWidth() {
        return levelPixelWidth;
    }
}
