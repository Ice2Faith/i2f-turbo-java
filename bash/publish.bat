@echo off
echo release begin ...

set src_path=.\deploy
set dst_path=.\release



mkdir %dst_path%


for /F %%i in ('dir /B %src_path%\*') do (
    echo %%i | findstr /B "^test-" > nul 2>&1 && (
        echo ignore test file %%i
    ) || (
        echo copy /B /Y %src_path%\%%i %dst_path%\
        copy /B /Y %src_path%\%%i %dst_path%\
    )
)


echo release done.
