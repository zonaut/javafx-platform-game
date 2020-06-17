package com.zonaut.games.javafx.platform.level;

import javafx.geometry.BoundingBox;

public class Block extends BoundingBox {

    private final int id;

    private boolean canPassWhenJumping;

    public Block(double minX, double minY, double width, double height, int id) {
        super(minX, minY, width, height);
        this.id = id;
    }

    ///
    /// Getters & setters
    ///

    public int getId() {
        return id;
    }

    public boolean isCanPassWhenJumping() {
        return canPassWhenJumping;
    }

    public void setCanPassWhenJumping(boolean canPassWhenJumping) {
        this.canPassWhenJumping = canPassWhenJumping;
    }
}
