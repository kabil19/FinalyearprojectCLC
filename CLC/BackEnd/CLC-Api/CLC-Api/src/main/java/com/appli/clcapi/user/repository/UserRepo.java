package com.appli.clcapi.user.repository;


import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;


@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {

    Iterable<UserEntity> findByUsernameIsContainingIgnoreCase(String existingChars);


}
