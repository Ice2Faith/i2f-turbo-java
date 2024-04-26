@echo off
echo release begin ...

set src_path=.\deploy
set dst_path=.\release

:: 脚本功能
:: 将 deploy 中的内容按照同名覆盖原则覆盖到 release 目录中
:: 如果 release 中没有同名文件，则不会复制

mkdir %dst_path%


for /F %%i in ('dir /B %src_path%\*') do (
    if exist %dst_path%\%%i (
        echo copy /B /Y %src_path%\%%i %dst_path%\
        copy /B /Y %src_path%\%%i %dst_path%\
    )
)


echo release done.
