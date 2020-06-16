package com.zonaut.games.javafx.platform;

/**
 * This is needed to start a JavaFx when running it packaged as a jar and on Java 11+
 * TODO Research this further on how we can avoid this
 */
public class Main {

    public static void main(String[] args) {
        App.main(args);
    }

}

