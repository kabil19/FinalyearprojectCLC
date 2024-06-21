package com.appli.clcapi.util;

import com.appli.clcapi.authentication.repository.UserLoginRepo;
import com.appli.clcapi.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentUserService {
    private final UserLoginRepo userLoginRepo;

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        return userLoginRepo.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

    }
}
