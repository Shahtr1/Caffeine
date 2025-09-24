#!/bin/bash
# Navigate to parent folder (project root)
cd "$(dirname "$0")/.."

# Compile the Java file
javac Caffeine.java
if [ $? -ne 0 ]; then
  echo "Compilation failed!"
  exit 1
fi

# Run the compiled program
java Caffeine
