package com.appli.clcapi.authentication.serviceImple;

import com.appli.clcapi.authentication.dto.request.LoginDto;
import com.appli.clcapi.authentication.dto.response.AuthResponse;
import com.appli.clcapi.authentication.repository.UserLoginRepo;
import com.appli.clcapi.authentication.service.UserLoginService;
import com.appli.clcapi.authentication.service.jwtService.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserLoginServiceImpl implements UserLoginService {
    private final AuthenticationManager authenticationManager;
    private final UserLoginRepo userLoginRepo;
    private final JwtService jwtService;

    @Override
    public AuthResponse userLogin(LoginDto loginRequest) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );
            var existingUserInDB = userLoginRepo.findByUsername(loginRequest.getUsername()).
                    orElseThrow(()->new UsernameNotFoundException("User Not Found"));
            String jwtToken = jwtService.generateToken(existingUserInDB);
            return AuthResponse.builder()
                    .token(jwtToken)
                    .userName(existingUserInDB.getUsername())
                    .build();
        }catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("Login failed!");
        }

    }
}
