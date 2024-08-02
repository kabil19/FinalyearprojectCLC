package com.appli.clcapi.util.backup.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
@Slf4j
public class BackupService {
    public void backup(boolean isManual) {
        try {
            String mode = isManual ? "manual" : "automatic";
            String scriptPath= "D:/wamp64/www/FinalYearProject/CLC-Api/src/main/resources/backup/backup.bat";
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c",scriptPath, mode);
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                log.info(line);
            }

            if (process.waitFor() == 0) {
                log.info("Backup completed successfully.");
            } else {
                log.error("Backup failed.");
            }
        } catch (IOException | InterruptedException e) {
            log.error("Backup process encountered an error: ", e);
        }
    }
}