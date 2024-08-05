@echo off

:: Configuration
set DB_NAME=clc
set DB_USER=root
set DB_PASSWORD=
set BACKUP_DIR=D:\clc-backup

:: Get the current date and time
for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /value') do set CURRENT_DATE=%%I

:: Format date as DD-MM-YYYY
set DATE=%CURRENT_DATE:~6,2%-%CURRENT_DATE:~4,2%-%CURRENT_DATE:~0,4%

:: Format time as HH-MM
set TIME=%CURRENT_DATE:~8,2%-%CURRENT_DATE:~10,2%

:: Get mode argument
set MODE=%1

:: Determine the backup file path based on the mode
if "%MODE%"=="manual" (
    set BACKUP_FILE=%BACKUP_DIR%\%DB_NAME%-%DATE%_%TIME%_manual.sql
) else if "%MODE%"=="whenRestore" (
    set SNAPSHOT_DIR=%BACKUP_DIR%\SNAPSHOT
    if not exist "%SNAPSHOT_DIR%" (
        mkdir "%SNAPSHOT_DIR%"
    )
    set BACKUP_FILE=%SNAPSHOT_DIR%\%DB_NAME%-%DATE%_%TIME%.sql
) else (
    set BACKUP_FILE=%BACKUP_DIR%\%DB_NAME%-%DATE%_%TIME%.sql
)
:: Create backup directory if it doesn't exist
if not exist "%BACKUP_DIR%" (
    mkdir "%BACKUP_DIR%"
)

:: Perform the backup
if "%DB_PASSWORD%"=="" (
    mysqldump -u %DB_USER% %DB_NAME% > "%BACKUP_FILE%"
) else (
    mysqldump -u %DB_USER% -p%DB_PASSWORD% %DB_NAME% > "%BACKUP_FILE%"
)

:: Check if the backup file is empty
for %%F in ("%BACKUP_FILE%") do if %%~zF == 0 (
    echo Backup failed. The backup file is empty.
    exit /b 1
)

:: Remove old backups (e.g., older than 30 days)
forfiles /p "%BACKUP_DIR%" /s /m *.sql /d -30 /c "cmd /c del @path"

echo Backup completed: %BACKUP_FILE%
exit /b 0
