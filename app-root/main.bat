@echo off
call build.jar
call build.bat
call del apk-release\*.apk
for /f %%i in (channels.txt) do call channel.bat %%i
for /r %%i in (apk-release\*.apk) do call sign.bat %%i