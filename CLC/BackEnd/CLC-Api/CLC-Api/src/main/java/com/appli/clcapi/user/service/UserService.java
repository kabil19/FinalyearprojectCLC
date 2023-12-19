package com.appli.clcapi.user.service;

import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.entity.UserEntity;

import java.util.ArrayList;
import java.util.Optional;

public interface UserService{


    String register(UserDto userDto)throws Exception;

    ArrayList<UserDto> getAllUsers() throws Exception;

    String deleteUser(Long userId);

    Optional<UserEntity> updateUser(UserDto userDto);


    ArrayList<UserDto> selectUsers(String existingChars);
}
