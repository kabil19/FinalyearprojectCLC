package com.appli.clcapi.util;

import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.serviceImple.UserServiceImpl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.appli.clcapi.common.enums.Roles.ADMIN;


@Component
@Slf4j
@RequiredArgsConstructor
public class DefaultDataLoader {

    private final UserServiceImpl userServiceImpl;
    @Value("${app.default-admin-password}")
    private String password;


    @PostConstruct
    public void createDefaultUser() {

        if (userServiceImpl.getAllUsers().isEmpty()) {
            UserDto aUser = new UserDto();
            aUser.setUsername("Admin");
            aUser.setRole(ADMIN);
            aUser.setPassword(password);
            userServiceImpl.insertNewUser(aUser);
            log.info("Default User created!");
        }

        backup();

    }

    private void backup() {
        try {
            Process process = Runtime.getRuntime().exec("src/main/resources/backup/backup.bat");
            process.waitFor();
            if (process.exitValue() == 0) {
                System.out.println("Backup completed successfully.");
            } else {
                System.err.println("Backup failed.");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
