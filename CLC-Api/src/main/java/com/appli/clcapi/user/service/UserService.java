package com.appli.clcapi.user.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.user.dto.GetUserReqDto;
import com.appli.clcapi.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public interface UserService{


    NonPaginatedResponse insertNewUser(UserDto userDto)throws Exception;

    List<GetUserReqDto> getAllActiveUsers() throws Exception;

    NonPaginatedResponse deleteUser(Long userId);

    NonPaginatedResponse updateUser(UserDto userDto);

    ArrayList<GetUserReqDto> selectUsers(String existingChars);
}
