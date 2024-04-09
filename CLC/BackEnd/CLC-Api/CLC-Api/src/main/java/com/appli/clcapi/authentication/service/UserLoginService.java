package com.appli.clcapi.authentication.service;

import com.appli.clcapi.authentication.dto.request.LoginDto;
import com.appli.clcapi.authentication.dto.response.LoginSuccessful;

public interface UserLoginService {
LoginSuccessful userLogin(LoginDto loginRequest);

}
