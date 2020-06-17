package com.zonaut.games.javafx.platform.level;

import javafx.geometry.BoundingBox;

public class Block extends BoundingBox {

    private int id;

    public Block(double minX, double minY, double width, double height, int id) {
        super(minX, minY, width, height);
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
