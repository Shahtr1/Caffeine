# Caffeine Program - README

## Overview

The `Caffeine` program simulates an automated mouse movement tool that works when the Caps Lock key is ON.  
It uses Java's `Robot` class to move the mouse to random positions on the screen at fixed intervals.  
Caps Lock acts as a switch: when it's ON, the mouse moves periodically; when it's OFF, the program does nothing.

A small floating indicator window (the "flag") shows the Caps Lock state:

- **Green + "CAPS ON"** → Caps Lock is active.
- **Red + "caps off"** → Caps Lock is inactive.

Double-clicking the indicator window will **exit the program**.

## Requirements

- Java 8 or higher must be installed on your system.
- Works with any screen resolution (the program automatically detects screen size).
- A compatible IDE, terminal, or script runner to compile and run the program.

## Features

- **Visual Indicator**: A small floating window displays the Caps Lock state (green for ON, red for OFF).
- **Random Mouse Movement**: Every 10 seconds, when Caps Lock is ON, the mouse pointer is moved to a random position within your screen bounds.
- **Automatic Caps Lock Detection**: The state is polled directly from the system (no need for the frame to be in focus).
- **Exit Shortcut**: Double-clicking the indicator window cleanly shuts down the program.
- **Low Resource Usage**: Mouse movement is checked at a fixed 10-second interval.

## Project Structure

```scss
CAFFEINE/
├── run/
│ ├── run_cfn.bat # Windows batch script to compile & run
│ ├── run_cfn.vbs # Windows VBScript launcher (hides console window)
│ ├── run_cfn.sh # Linux shell script to compile & run
├── Caffeine.java # Main program source code
├── Caffeine.class # Compiled bytecode
├── LICENSE # License file
└── README.md # Project documentation
```

## How It Works

1. **Caps Lock Detection**:

   - The program queries the system (`Toolkit.getLockingKeyState`) every 200ms to detect whether Caps Lock is ON or OFF.
   - This ensures detection works even if the program window is not in focus.

2. **Mouse Movement**:

   - When Caps Lock is ON, the mouse will be moved randomly across the entire screen every 10 seconds.

3. **Indicator Window**:
   - A small, always-on-top window shows Caps Lock state with color coding and text.
   - Double-clicking the window exits the program immediately.

## Usage Instructions

### Windows

1. Go into the `run/` folder.
2. Double-click `run_cfn.bat` to compile and run in a command window.
3. Or, double-click `run_cfn.vbs` to run silently without showing a console.

### Linux / macOS

1. Make the shell script executable:

   ```bash
   chmod +x run/run_cfn.sh
   ```

2. Run it:

   ```bash
   ./run/run_cfn.sh
   ```

3. To run without terminal, create a .desktop entry with:

```ini
[Desktop Entry]
Type=Application
Name=Caffeine
Exec=/full/path/to/run/run_cfn.sh
Terminal=false
```

### Manual (any OS)

1. Compile:

```bash
javac Caffeine.java
```

2. Run:

```bash
java Caffiene
```

## Limitations

- `Toolkit.getLockingKeyState` may not be supported on some platforms (certain Linux distros or headless environments).
- On Linux/macOS, hiding the terminal requires using a .desktop launcher or running in background with output redirected.

## License

This project is licensed under the MIT License — see the [LICENSE](./LICENSE) file for details.
