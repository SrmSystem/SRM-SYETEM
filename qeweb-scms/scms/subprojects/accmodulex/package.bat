@echo off
echo [Pre-Requirement] Makesure package JDK 6.0+ and set the JAVA_HOME.
echo [Pre-Requirement] Makesure package Maven 3.0.3+ and set the PATH.

set MVN=mvn
set MAVEN_OPTS=%MAVEN_OPTS% -XX:MaxPermSize=128m

cd ../../../
call %MVN% clean package

if errorlevel 1 goto error
goto end
:error
echo Error Happen!!
:end
pause
