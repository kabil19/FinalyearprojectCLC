package com.appli.clcapi.user.controller;
import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.entity.UserEntity;
import com.appli.clcapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user/")
@CrossOrigin("*")
public class UserController {

private final UserService userService ;

    @PostMapping(path = "register")
    public String registerUser(@RequestBody UserDto userDto) throws Exception
    {
        try{
            if(Objects.equals(userDto.getPassword(), userDto.getConfirmPw())){
                return userService.register(userDto);
            }else{
                return "Password isn't matching";
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @GetMapping(path="getAll")
    public ArrayList<UserDto> getAllUsers() throws Exception
    {
      return  userService.getAllUsers();
    }

    @DeleteMapping("deleteUser/{userId}")
    public String deleteUser(@PathVariable Long userId ) throws Exception
    {
        return userService.deleteUser(userId);
    }

    @PutMapping("updateUser")
    private Optional<UserEntity> update(@RequestBody UserDto userDto)
    {
        return  userService.updateUser(userDto);
    }

    @GetMapping(path = "selectUsers")
    private ArrayList<UserDto> selectUsers(@RequestBody String existingChars)
    {
        return userService.selectUsers(existingChars);
    }


}
