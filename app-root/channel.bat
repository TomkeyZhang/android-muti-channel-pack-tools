echo %1
call channel.jar %1
call ant release
call copy-apk.jar %1