package com.appli.clcapi.user.serviceImple;

import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.entity.UserEntity;
import com.appli.clcapi.user.repository.UserRepo;
import com.appli.clcapi.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Optional;
@Service
@AllArgsConstructor
public class UserServiceImple implements UserService {
    private final UserRepo userRepo;

    public String register(UserDto userDto) {
        try {
            var user = UserEntity.builder()
                    .userId(userDto.getUserId())
                    .username(userDto.getUsername())
                    .firstname(userDto.getFirstname())
                    .lastname(userDto.getLastname())
                    .gender(userDto.getGender())
                    .role(userDto.getRole())
                    .email(userDto.getEmail())
                    .password(userDto.getPassword())
                    .confirmPw(userDto.getConfirmPw())
                    .build();
            userRepo.save(user);
            return "Added";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("User Registration Failed");
        }
    }


    public ArrayList<UserDto> getAllUsers() {
        try {
            ArrayList<UserDto> userList = new ArrayList<>();
            Iterable<UserEntity> userEntityList = userRepo.findAll();

            for (UserEntity aUser : userEntityList) {
                UserDto userDto = new UserDto(aUser);
                userList.add(userDto);
            }
            return userList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("User Not Found");
        }

    }

    @Override
    public String deleteUser(Long userId) {
        try {
            Optional<UserEntity> existingUserOptional = userRepo.findById(userId);
            UserEntity aUser = existingUserOptional.get();
            userRepo.delete(aUser);
            return "UserDeleted";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    @Override
    public String updateUser(UserDto userDto) {
        try {
            Optional<UserEntity> existingUserOptional = userRepo.findById(userDto.getUserId());
            UserEntity aUser = null;
            if (existingUserOptional.isPresent()) {
                aUser = existingUserOptional.get();
                aUser.setFirstname(userDto.getFirstname());
                aUser.setLastname(userDto.getLastname());
                aUser.setUsername(userDto.getUsername());
                aUser.setRole(userDto.getRole());
                aUser.setGender(userDto.getGender());
                aUser.setEmail(userDto.getEmail());
                if(userDto.getConfirmPw()==null && userDto.getPassword()==null){
                    aUser.setPassword(existingUserOptional.get().getPassword());
                    aUser.setConfirmPw(existingUserOptional.get().getConfirmPw());
                }else{
                    aUser.setPassword(userDto.getPassword());
                    aUser.setConfirmPw(userDto.getConfirmPw());
                }
            }
            userRepo.save(aUser);
            return "Updated";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }


    public ArrayList<UserDto> selectUsers(String existingChars) {
        try {
//            Optional<UserEntity> foundUsers =
            ArrayList<UserDto> userList = new ArrayList<>();
            Iterable<UserEntity> searchedUser = userRepo.findByUsernameIsContainingIgnoreCase(existingChars);
            for (UserEntity aUser : searchedUser) {
                UserDto userDto = new UserDto(aUser);
                userList.add(userDto);
            }
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


    }
}