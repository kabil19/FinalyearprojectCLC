package com.appli.clcapi.userlogin.service;

import com.appli.clcapi.userlogin.dto.request.LoginDto;
import com.appli.clcapi.userlogin.dto.response.LoginSuccessful;

public interface UserLoginService {
LoginSuccessful userLogin(LoginDto loginRequest);

}
