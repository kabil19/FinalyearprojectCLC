package com.appli.clcapi.user.controller;
import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Objects;

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
            if(userDto.getPassword() != null){
                if(Objects.equals(userDto.getPassword(), userDto.getConfirmPw())){
                    return userService.register(userDto);
                }else{
                    return "Password isn't matching";
                }
            }else{
                return "Password can't be null";
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
    private String update(@RequestBody UserDto userDto)
    {
        return  userService.updateUser(userDto);
    }

    @GetMapping(path = "selectUsers/{existingChars}")
    private ArrayList<UserDto> selectUsers(@PathVariable String existingChars)  {
        return userService.selectUsers(existingChars);
    }


}
