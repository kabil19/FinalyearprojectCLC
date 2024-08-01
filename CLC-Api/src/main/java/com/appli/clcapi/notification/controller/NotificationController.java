package com.appli.clcapi.notification.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/api/notifications")
@CrossOrigin(origins = "http://localhost:4200")
public class NotificationController {


    private final NotificationService notificationService;
    @GetMapping("fetchAllNotifications")
    public NonPaginatedResponse fetchAllNotifications(){
        return notificationService.fetchAllNotifications();
    }
}
