@echo off
echo collect begin ...

set src_path=..
set dst_path=.\deploy

:: 脚本功能
:: 将打包的结果收集到 deploy 目录中，并将原来的 deploy 目录移动到 backup 目录
:: 也就是说，deploy 是最新的打包结果，backup 是上一次打包结果

mkdir %dst_path%
mkdir .\backup

rd /q /s .\backup
move /Y %dst_path% .\backup

rd /q /s %dst_path%
mkdir %dst_path%

for /F %%i in ('dir /B %src_path%\*') do (
    if "%%i" == "bash" (
        echo jump
    ) else (
      for /F %%j in ('dir /S /B %src_path%\%%i\*.jar') do (
          echo %%j | findstr target\archive-tmp > nul && (
                echo ignore: %%j
          ) || (
                echo copy /B /Y %%j %dst_path%\
                copy /B /Y %%j %dst_path%\
          )
      )
      for /F %%j in ('dir /S /B %src_path%\%%i\*.war') do (
          echo %%j | findstr target\archive-tmp > nul && (
                echo ignore: %%j
          ) || (
                echo copy /B /Y %%j %dst_path%\
                copy /B /Y %%j %dst_path%\
          )
      )
      for /F %%j in ('dir /S /B %src_path%\%%i\*.tar') do (
          echo %%j | findstr target\archive-tmp > nul && (
                echo ignore: %%j
            ) || (
                echo copy /B /Y %%j %dst_path%\
                copy /B /Y %%j %dst_path%\
            )
      )
      for /F %%j in ('dir /S /B %src_path%\%%i\*.gz') do (
          echo %%j | findstr target\archive-tmp > nul && (
                echo ignore: %%j
            ) || (
                echo copy /B /Y %%j %dst_path%\
                copy /B /Y %%j %dst_path%\
            )
      )
    )
)


echo collect done.
