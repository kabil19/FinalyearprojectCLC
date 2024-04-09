package com.appli.clcapi.authentication.controller;

import com.appli.clcapi.authentication.dto.request.LoginDto;
import com.appli.clcapi.authentication.dto.response.LoginSuccessful;
import com.appli.clcapi.authentication.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/authentication/")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {
public final UserLoginService userLoginService;

    @PostMapping(path = "login")
    public LoginSuccessful login(@RequestBody LoginDto loginDto){
//            return ResponseEntity.ok(userLoginService.userLogin(loginDto));
        return (userLoginService.userLogin(loginDto));
    }
}
