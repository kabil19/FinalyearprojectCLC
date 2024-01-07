package com.appli.clcapi.user.service;

import com.appli.clcapi.user.dto.GetUserReqDto;
import com.appli.clcapi.user.dto.UserDto;

import java.util.ArrayList;

public interface UserService{


    String register(UserDto userDto)throws Exception;

    ArrayList<GetUserReqDto> getAllUsers() throws Exception;

    String deleteUser(Long userId);

    String updateUser(UserDto userDto);

    ArrayList<UserDto> selectUsers(String existingChars);
}
