#!/bin/bash

# Path to your JavaFX SDK lib folder
JFX_LIB=~/openjfx-17.0.16_linux-x64_bin-sdk/javafx-sdk-17.0.16/lib

# Create build directory if it doesn't exist
mkdir -p build

# Compile all Java files
javac --module-path "$JFX_LIB" --add-modules javafx.controls,javafx.fxml *.java -d build

# Run the application
java --module-path "$JFX_LIB" --add-modules javafx.controls,javafx.fxml -cp build App
