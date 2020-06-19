package com.zonaut.games.javafx.platform.entities;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Animation extends Transition {

    private static final Logger LOG = LogManager.getLogger(Animation.class);

    private Image[] frames;
    private int index;
    private int lastIndex;

    private int duration;

    public Animation(int duration, Image[] frames) {
        this.setInterpolator(Interpolator.LINEAR);
        this.setCycleCount(INDEFINITE);
        this.setFrames(duration, frames);
    }

    public void setFrames(int duration, Image[] frames) {
        if (this.duration != duration) {
            this.setCycleDuration(Duration.millis(duration));
            this.playFromStart();
        }
        this.duration = duration;
        this.frames = frames;
    }

    public Image getImage() {
        if (frames.length <= index) {
            index = 0;
        }
        return this.frames[index];
    }

    @Override
    protected void interpolate(double frac) {
        int index = Math.min((int) Math.floor(frac * frames.length), frames.length - 1);
        if (index != lastIndex) {
            this.index = index;
            this.lastIndex = index;
        }
    }
}
