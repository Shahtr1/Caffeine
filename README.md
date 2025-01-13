# Caffeine Program - README

## Overview

The `Caffeine` program simulates an automated mouse movement tool that works when the Caps Lock key is ON. The program uses Java's `Robot` class to move the mouse to a random position on the screen at regular intervals. The Caps Lock key acts as a switch to enable or disable the mouse movement. 

When Caps Lock is ON, the program will randomly move the mouse within the screen bounds. If Caps Lock is OFF, the program will simply wait for it to be turned ON.

The program listens for Caps Lock state changes and detects it using a hidden, invisible frame.

## Requirements

- Java 8 or higher must be installed on your system.
- A screen with at least a 1920x1080 resolution is recommended, as the program uses these values for random mouse movement within the screen bounds.

## Features

- **Random Mouse Movement**: When Caps Lock is ON, the mouse will be moved to a random position on the screen every 10 seconds.
- **Caps Lock Detection**: The program uses an invisible frame that listens for Caps Lock key presses to detect when the Caps Lock state changes.
- **Low Resource Usage**: The mouse movement check happens at a fixed interval (every 10 seconds), so it does not use excessive system resources.

## How It Works

1. **Caps Lock Detection**: 
   - The program listens for key events on an invisible frame to detect when Caps Lock is toggled. This requires the frame to be **in focus** for the key listener to detect Caps Lock changes.
   
2. **Mouse Movement**:
   - When Caps Lock is ON, the mouse will be moved to a random location on the screen within the screen's width (1920px) and height (1080px).
   
3. **Scheduled Task**:
   - A scheduled task is run every 10 seconds that checks the Caps Lock state and moves the mouse if necessary.

## Requirements

- Java 8 or higher.
- A compatible IDE or text editor to compile and run the program.
- The frame window must be in focus for the Caps Lock state changes to be detected.

## Usage Instructions

1. **Compile the Program**:
   - Save the program as `Caffeine.java`.
   - Open your terminal or command prompt and navigate to the directory where `Caffeine.java` is located.
   - Compile the program using the following command:
     ```bash
     javac Caffeine.java
     ```

2. **Run the Program**:
   - Once compiled, run the program using the following command:
     ```bash
     java Caffeine
     ```

3. **How to Enable/Disable Mouse Movement**:
   - The program will only move the mouse when Caps Lock is ON.
   - Press Caps Lock to toggle the state. The mouse will start moving randomly when Caps Lock is ON and stop when Caps Lock is OFF.

4. **Important Note**:
   - The invisible frame that listens for Caps Lock state changes must be in focus for the Caps Lock key events to be detected.

## Limitations

- The program requires the frame to be in focus in order to detect Caps Lock key events. If the frame loses focus, Caps Lock state changes won't be detected.
- Currently, the program is designed to work with a screen resolution of at least 1920x1080.

## License

This project is licensed under the MIT License - see the LICENSE file for details.
