package com.appli.clcapi.userlogin.serviceImple;

import com.appli.clcapi.userlogin.dto.request.LoginDto;
import com.appli.clcapi.userlogin.dto.response.LoginSuccessful;
import com.appli.clcapi.userlogin.repository.UserLoginRepo;
import com.appli.clcapi.userlogin.service.UserLoginService;
import com.appli.clcapi.userlogin.service.jwtService.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserloginServiceImple implements UserLoginService {
    private final AuthenticationManager authenticationManager;
    private final UserLoginRepo userLoginRepo;
    private final JwtService jwtService;

    @Override
    public LoginSuccessful userLogin(LoginDto loginRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            var existingUserInDB = userLoginRepo.findByUsername(loginRequest.getUsername()).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
            String jwtToken = jwtService.generateToken(existingUserInDB);
            return LoginSuccessful.builder()
                    .token(jwtToken)
                    .build();
        }catch (Exception e){
            e.printStackTrace();
        }
    return null;

    }


}
