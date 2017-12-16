mvn clean
if errorlevel 1 goto error


echo [INFO] error
echo [INFO] http://localhost:80/qeweb


goto end
:error
echo Error Happen!!
:end
pause