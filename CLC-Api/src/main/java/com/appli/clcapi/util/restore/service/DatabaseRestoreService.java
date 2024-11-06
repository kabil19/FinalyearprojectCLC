package com.appli.clcapi.util.restore.service;

import com.appli.clcapi.util.backup.service.BackupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
@Service
@Slf4j
public class DatabaseRestoreService {

    @Value("${app.restore-script-path}")
    private String restoreScriptPath;
    private final BackupService backupService;

    public DatabaseRestoreService(BackupService backupService) {
        this.backupService = backupService;
    }

    public void restoreDatabase(File backupFile) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", restoreScriptPath, backupFile.getAbsolutePath());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();

        if (process.exitValue() == 0) {
            System.out.println("Restore completed successfully.");
        } else {
            System.err.println("Restore is failed.");
        }
    }
}

