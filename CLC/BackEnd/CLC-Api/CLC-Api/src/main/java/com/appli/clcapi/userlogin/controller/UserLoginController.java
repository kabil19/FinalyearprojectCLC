package com.appli.clcapi.userlogin.controller;

import com.appli.clcapi.userlogin.dto.request.LoginDto;
import com.appli.clcapi.userlogin.dto.response.LoginSuccessful;
import com.appli.clcapi.userlogin.service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user/")
@CrossOrigin(origins = "http://localhost:4200")
public class UserLoginController {
public final UserLoginService userLoginService;

    @PostMapping(path = "login")
    public LoginSuccessful login(@RequestBody LoginDto loginDto){
//            return ResponseEntity.ok(userLoginService.userLogin(loginDto));
        return (userLoginService.userLogin(loginDto));
    }
}
