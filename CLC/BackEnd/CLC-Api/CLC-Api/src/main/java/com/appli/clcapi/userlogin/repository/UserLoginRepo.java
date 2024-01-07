package com.appli.clcapi.userlogin.repository;

import com.appli.clcapi.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserLoginRepo extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsername(String username);
}
