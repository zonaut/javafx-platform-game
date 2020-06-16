package com.zonaut.games.javafx.platform.level;

import javafx.geometry.BoundingBox;

public class Block extends BoundingBox {

    private int id;

    public Block(int id, double minX, double minY, double width, double height) {
        super(minX, minY, width, height);
    }

    public int getId() {
        return id;
    }
}
