@echo off
setlocal enabledelayedexpansion
set libs=libs
set staticFileDir=resource
set %mainClass=com.huahudie.TestCase
set totalStr= %staticFileDir%
for /r %libs% %%i in (*.jar) do (
    set totalStr=!totalStr!;%%i
    set pa=1
    echo !pa!
)
echo %totalStr%
java -classpath %totalStr% %mainClass%
a
pause

