@echo off
chcp 65001 > nul
title run jar

set MAX_JAR=

set JVM_OPTS=-Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -Dlogback.app.env=test
set JAVA_APP=java

:: search the max jar file ...
for /f "tokens=*" %%a in ('dir /b /o-s /a-d *.jar') do (
    set "MAX_JAR=%%a"
    goto :done
)

:: not found, exit
echo "not found any jar in current dir, will exit!"
pause
exit

:: found jar file, run it
:done
title %MAX_JAR%

@echo on 
%JAVA_APP%  %JVM_OPTS%  -jar %MAX_JAR%