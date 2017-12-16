@echo off
echo [Pre-Requirement] Makesure install JDK 6.0+ and set the JAVA_HOME.
echo [Pre-Requirement] Makesure install Maven 3.0.3+ and set the PATH.

set MVN=mvn
set MAVEN_OPTS=%MAVEN_OPTS% -XX:MaxPermSize=128m

call %MVN% clean install
if errorlevel 1 goto error
cd scms\subprojects\basemodule
call %MVN% antrun:run -Prefresh-db
if errorlevel 1 goto error
cd ..\..\..\
cd scms\subprojects\baseweb

start "qeweb" %MVN% clean -Djetty.port=80 jetty:run
if errorlevel 1 goto error
cd ..\..\

echo [INFO] Please wait a moment. When you see "[INFO] Started Jetty Server" in both 2 popup consoles, you can access below demo sites:
echo [INFO] http://localhost:80/qeweb


goto end
:error
echo Error Happen!!
:end
pause