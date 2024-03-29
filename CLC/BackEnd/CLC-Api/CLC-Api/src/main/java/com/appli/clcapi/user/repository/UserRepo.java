package com.appli.clcapi.user.repository;



import com.appli.clcapi.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface UserRepo extends JpaRepository<UserEntity,Long> {

    Iterable<UserEntity> findByUsernameIsContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrGenderContainingIgnoreCase
            (String username,
             String firstname,
             String email,
             String gender);

    List<UserEntity> findAllByDeletedEquals(boolean state);
}
