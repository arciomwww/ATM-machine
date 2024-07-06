@echo off
setlocal

set SRC_DIR=src

if not exist bin mkdir bin

javac -encoding UTF-8 -d bin %SRC_DIR%\*.java

cd bin

java Main

cd ..

endlocal
pause
