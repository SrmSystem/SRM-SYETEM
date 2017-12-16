call mvn install
if errorlevel 1 goto error
goto end
:error
echo Error Happen!!
:end
pause