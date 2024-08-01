package com.appli.clcapi.authentication.service;

import com.appli.clcapi.authentication.dto.request.LoginDto;
import com.appli.clcapi.authentication.dto.response.AuthResponse;

public interface UserLoginService {
AuthResponse userLogin(LoginDto loginRequest);

}
