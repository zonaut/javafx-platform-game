package com.zonaut.games.javafx.platform.screens;

import javafx.stage.Stage;

import java.util.EnumMap;

public enum  ScreenType {

    MENU,
    NEW,
    OPTIONS
    ;

    public static final EnumMap<ScreenType, Screen> SCREENS = new EnumMap<>(ScreenType.class);
    static {
        SCREENS.put(ScreenType.MENU, new MenuScreen());
        SCREENS.put(ScreenType.NEW, new NewGameScreen());
        SCREENS.put(ScreenType.OPTIONS, new OptionsScreen());
    }

    public static void switchScreenTo(ScreenType type, Stage stage) {
        SCREENS.get(type).display(stage);
    }

}
