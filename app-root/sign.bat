@echo off
set ifo=%1
call keystore
call jarsigner -keystore %keystore% -storepass %storepass% -keypass %keypass% -signedjar %ifo:~0,-4%-signed.apk %1 %alias%
call zipalign -v 4 %ifo:~0,-4%-signed.apk %ifo:~0,-4%-signed-align.apk
call del %ifo:~0,-4%-signed.apk
call del %1