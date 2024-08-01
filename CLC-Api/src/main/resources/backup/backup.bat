@echo off

:: Configuration
set DB_NAME=clc
set DB_USER=root
set DB_PASSWORD=password
set BACKUP_DIR=D:\backup
for /f "tokens=2 delims==" %%I in ('wmic os get localdatetime /value') do set DATE=%%I
set DATE=%DATE:~0,8%%DATE:~8,4%
set BACKUP_FILE=%BACKUP_DIR%\%DB_NAME%-%DATE%.sql

:: Create backup directory if it doesn't exist
if not exist "%BACKUP_DIR%" (
    mkdir "%BACKUP_DIR%"
)

:: Perform the backup
mysqldump -u %DB_USER% -p%DB_PASSWORD% %DB_NAME% > "%BACKUP_FILE%"

:: Check if the backup file is empty
for %%F in ("%BACKUP_FILE%") do if %%~zF == 0 (
    echo Backup failed. The backup file is empty.
    exit /b 1
)

:: Optional: Remove old backups (e.g., older than 30 days)
forfiles /p "%BACKUP_DIR%" /s /m *.sql /d -30 /c "cmd /c del @path"

echo Backup completed: %BACKUP_FILE%
exit /b 0
