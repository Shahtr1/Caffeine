@echo off
REM Go to the parent folder where Caffeine.java is
cd ..

REM Compile the Java file
javac Caffeine.java

REM If compilation fails, exit
if %errorlevel% neq 0 (
    echo Compilation failed!
    exit /b %errorlevel%
)

REM Run the compiled program
java Caffeine
