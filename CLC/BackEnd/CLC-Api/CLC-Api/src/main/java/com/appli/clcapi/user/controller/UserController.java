package com.appli.clcapi.user.controller;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.user.dto.GetUserReqDto;
import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/user/")
@CrossOrigin(origins = "*")
public class UserController {

private final UserService userService ;
    @PostMapping(path = "register")
    public NonPaginatedResponse registerUser(@RequestBody UserDto userDto) throws Exception {
        return userService.register(userDto);
    }
    @GetMapping(path="getAll")
    public List<GetUserReqDto> getAllUsers() throws Exception
    {
      return  userService.getAllUsers();
    }
    @DeleteMapping("delete/{userId}")
    public String deleteUser(@PathVariable Long userId ) {
        return userService.deleteUser(userId);
    }

    @PutMapping("update")
    private String update(@RequestBody UserDto userDto)
    {
        return  userService.updateUser(userDto);
    }

    @GetMapping(path = "select/{existingChars}")
    private ArrayList<GetUserReqDto> selectUsers(@PathVariable String existingChars)  {
        return userService.selectUsers(existingChars);
    }


}
