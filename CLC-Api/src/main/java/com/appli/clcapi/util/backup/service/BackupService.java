package com.appli.clcapi.util.backup.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
public class BackupService {
    public void backup() {
        try {
            Process process = Runtime.getRuntime().exec("src/main/resources/backup/backup.bat");
            process.waitFor();
            if (process.exitValue() == 0) {
                log.info("Backup completed successfully.");
            } else {
                log.error("Backup failed.");
            }
        } catch (IOException | InterruptedException e) {
            log.error("Backup process encountered an error: ", e);
        }
    }
}
