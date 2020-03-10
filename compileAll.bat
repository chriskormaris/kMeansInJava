:: This file is for Windows only.

if not exist bin mkdir bin
javac -cp lib\gral-core-0.11.jar; -d bin src\kMeans\*.java src\plot\*.java
pause
