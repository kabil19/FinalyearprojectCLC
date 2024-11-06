@echo off
setlocal

REM Set database connection details
set DB_NAME=clc
set DB_USER=root
set DB_PASSWORD=CLC2019
:: Path to the backup file provided by the application
set BACKUP_FILE=%1

:: Check if the backup file exists
if not exist "%BACKUP_FILE%" (
    echo Backup file not found: %BACKUP_FILE%
    exit /b 1
)

:: Perform the restore
echo Restoring database from %BACKUP_FILE%
if "%DB_PASSWORD%"=="" (
    mysql -u %DB_USER% %DB_NAME% < "%BACKUP_FILE%"
) else (
    mysql -u %DB_USER% -p%DB_PASSWORD% %DB_NAME% < "%BACKUP_FILE%"
)

:: Check if the restore was successful
if ERRORLEVEL 1 (
    echo Restore failed with error code %ERRORLEVEL%.
    exit /b 1
)

echo Restore completed successfully.
exit /b 0
