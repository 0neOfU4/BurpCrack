@echo off
echo     ____                      _____       _ __          __    ____            __
echo    / __ )__  ___________     / ___/__  __(_) /____     / /   / __ \____ _____/ /__  _____
echo   / __  / / / / ___/ __ \    \__ \/ / / / / __/ _ \   / /   / / / / __  / __  / _ \/ ___/
echo  / /_/ / /_/ / /  / /_/ /   ___/ / /_/ / / /_/  __/  / /___/ /_/ / /_/ / /_/ /  __/ /
echo /_____/\____/_/  / ____/   /____/\____/_/\__/\___/  /_____/\____/\__,_/\__,_/\___/_/
echo			/_/	
echo  微信公众号：祝融安全  
echo.
start "burpsuite" /B "javaw.exe" -Xmx8G -XX:-UseParallelGC -noverify --add-opens=java.desktop/javax.swing=ALL-UNNAMED --add-opens=java.base/java.lang=ALL-UNNAMED -javaagent:BurpSuiteLoader.jar -Dfile.encoding=utf-8 -jar "burpsuite_pro.jar"
exit
pause