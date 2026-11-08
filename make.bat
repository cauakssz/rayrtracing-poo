
@echo off
cd /d "%~dp0"

title ASCII Ray Tracing

"%SystemRoot%\System32\chcp.com" 65001 >nul

echo Compilando projeto...

javac -encoding UTF-8 -d . raytrace\main.java raytrace\vec3.java raytrace\ray.java

if errorlevel 1 (
    echo.
    echo Erro na compilacao!
    pause
    exit /b 1
)

echo.
echo Executando Ray Tracing...

java -Dfile.encoding=UTF-8 -cp . raytrace.Main

echo.
echo Programa finalizado!
pause
