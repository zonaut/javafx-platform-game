# JavaFX platform game

An orthogonal platform game example using JavaFX.

**This is a work in progress!**

This is developed and tested on a Linux-based system.  
I try to keep Win users into consideration when it comes to file separators but let me know if I missed something.

## Build & run

    // Run
    mvn clean javafx:run

    // Build
    mvn clean package
    
    // Run the build package
    java -jar target/javafx-platform-game-0.0.1-SNAPSHOT.jar 

## Map editor

This example uses [Tiled](https://www.mapeditor.org/) as a map editor which delivers a file format that is well-known by a large community.  
An added benefit is that there is a Java artifact libtiled available which seems well maintained for reading these maps.

We could write a custom map editor and file format but this seems pointless to me.  
Let's keep it fun and spend our time on creating the actual game itself.

## Graphics

The`_design-files` folder contains all source files which are created with [Gimp](https://www.gimp.org/). 
