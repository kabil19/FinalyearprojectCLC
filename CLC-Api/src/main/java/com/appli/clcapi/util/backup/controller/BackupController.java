package com.appli.clcapi.util.backup.controller;

import com.appli.clcapi.util.backup.service.BackupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/backup")
@RequiredArgsConstructor
public class BackupController {

    private final BackupService backupService;

    @PostMapping
    public ResponseEntity<String> triggerBackup() {
        backupService.backup(true);
        return ResponseEntity.ok("Backup is initiated successfully!");
    }
}
