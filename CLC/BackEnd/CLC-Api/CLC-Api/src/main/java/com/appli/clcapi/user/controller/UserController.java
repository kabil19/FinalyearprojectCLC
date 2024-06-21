package com.appli.clcapi.user.controller;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.user.dto.GetUserReqDto;
import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user/")
@CrossOrigin("http://localhost:4200")
public class UserController {

private final UserService userService ;
    @PostMapping(path = "register")
    public NonPaginatedResponse registerUser(@RequestBody UserDto userDto) throws Exception {
        return userService.register(userDto);
    }
    @GetMapping(path="getAll")
    public List<GetUserReqDto> getAllUsers() throws Exception
    {
      return  userService.getAllActiveUsers();
    }
    @DeleteMapping("delete/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public NonPaginatedResponse deleteUser(@PathVariable Long userId ) {
        return userService.deleteUser(userId);
    }

    @PutMapping("update")
    private NonPaginatedResponse update(@RequestBody UserDto userDto)
    {
        return  userService.updateUser(userDto);
    }

    @GetMapping(path = "select/{existingChars}")
    private ArrayList<GetUserReqDto> selectUsers(@PathVariable String existingChars)  {
        return userService.selectUsers(existingChars);
    }


}
