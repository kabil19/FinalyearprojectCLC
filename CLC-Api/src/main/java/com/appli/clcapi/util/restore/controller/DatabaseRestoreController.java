package com.appli.clcapi.util.restore.controller;

import com.appli.clcapi.util.restore.service.DatabaseRestoreService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api/restore")
public class DatabaseRestoreController {
    @Value("${app.restore-script-path}")
    private String restoreScriptPath;

    private final DatabaseRestoreService databaseRestoreService;

    public DatabaseRestoreController(DatabaseRestoreService databaseRestoreService) {
        this.databaseRestoreService = databaseRestoreService;
    }

    @PostMapping
    public ResponseEntity<String> restoreDatabase(@RequestParam("file") MultipartFile file) {
        try {
            // Save the uploaded file
            File backupFile = new File("D:/clc-backup/" + file.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(backupFile)) {
                fos.write(file.getBytes());
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save the file: " + e.getMessage());
            }

            // Delegate the restore operation to the service
            databaseRestoreService.restoreDatabase(backupFile);

            return ResponseEntity.ok("Restore initiated successfully.");
        } catch (IOException | InterruptedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Restore failed: " + e.getMessage());
        }
    }
}
