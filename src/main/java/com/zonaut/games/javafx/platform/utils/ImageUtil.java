package com.zonaut.games.javafx.platform.utils;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

public final class ImageUtil {

    public static Image getFrom(Image image, int x, int y, int width, int height) {
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }

    /**
     * TODO If the sprite is on multiple rows we need to do a calculation based on images width and height
     * @param image
     * @param x the X position from the upper left corner of the image
     * @param y the Y position from the upper left corner of the image
     * @param width
     * @param height
     * @param count
     * @return
     */
    public static Image[] getFrom(Image image, int x, int y, int width, int height, int count) {
        Image[] images = new Image[count];
        for (int index = 0 ; index < count; index++) {
            images[index] = getFrom(image, x + (width * index), y, width, height);
        }
        return images;
    }

}
