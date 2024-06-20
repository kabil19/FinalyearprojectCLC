package com.appli.clcapi.user.serviceImple;

import com.appli.clcapi.common.constants.UserConstants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.user.dto.GetUserReqDto;
import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.entity.UserEntity;
import com.appli.clcapi.user.repository.UserRepo;
import com.appli.clcapi.user.service.UserService;
import com.appli.clcapi.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    public NonPaginatedResponse register(UserDto userDto) {

        NonPaginatedResponse response = new NonPaginatedResponse();
        if (userRepo.findByUsernameAndDeletedEquals(userDto.getUsername(), false).isEmpty()) {
            var user = UserEntity.builder()
                    .userId(userDto.getUserId())
                    .username(userDto.getUsername().toLowerCase())
                    .firstname(userDto.getFirstname())
                    .lastname(userDto.getLastname())
                    .gender(userDto.getGender())
                    .role(userDto.getRole())
                    .email(userDto.getEmail())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .build();
            UserEntity aUser = userRepo.save(user);
            GetUserReqDto getUserDTO = new GetUserReqDto(aUser);
            response.setResult(getUserDTO);
            response.setSuccessMessage(UserConstants.USER_CREATED_SUCCESSFULLY);
        } else {
            response.setErrors(Arrays.asList(UserConstants.USER_IS_ALREADY_EXIST));
        }
        return response;
    }

    @Override
    public List<GetUserReqDto> getAllActiveUsers() {
        try {
            List<GetUserReqDto> userList = new ArrayList<>();
            List<UserEntity> userEntityList = userRepo.findAllByDeletedEquals(false);

            for (UserEntity aUser : userEntityList) {
                GetUserReqDto userDto = new GetUserReqDto(aUser);
                userList.add(userDto);
            }
            return userList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("User Not Found");
        }

    }
    public List<GetUserReqDto> getAllUsers() {
        try {
            List<GetUserReqDto> userList = new ArrayList<>();
            List<UserEntity> userEntityList = userRepo.findAll();

            for (UserEntity aUser : userEntityList) {
                GetUserReqDto userDto = new GetUserReqDto(aUser);
                userList.add(userDto);
            }
            return userList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("User Not Found");
        }

    }

    public Optional<UserEntity> userFindByUsername(String name) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        return userRepo.findByUsernameAndDeletedEquals(name, Boolean.FALSE);
    }

    @Override
    public NonPaginatedResponse deleteUser(Long userId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            if(currentUserService.getCurrentUser().getUserId() == userId){
                response.setErrors(List.of("Can't delete"));
                return response;
            }
            UserEntity aUser = userRepo.getReferenceById(userId);
            aUser.setDeleted(true);
            userRepo.save(aUser);
            response.setSuccessMessage("User Deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
return response;
    }

    @Override
    public String updateUser(UserDto userDto) {
        try {
            Optional<UserEntity> existingUserOptional = userRepo.findById(userDto.getUserId());
            UserEntity aUser = new UserEntity();
            if (existingUserOptional.isPresent()) {
                aUser = existingUserOptional.get();
                aUser.setFirstname(userDto.getFirstname());
                aUser.setLastname(userDto.getLastname());
//                aUser.setUsername(userDto.getUsername().toLowerCase());
                aUser.setRole(userDto.getRole());
                aUser.setGender(userDto.getGender());
                aUser.setEmail(userDto.getEmail());
                if (userDto.getConfirmPw() == null && userDto.getPassword() == null) {
                    throw new RuntimeException("Invalid Password");
                } else {
                    aUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
//                    aUser.setConfirmPw(passwordEncoder.encode(userDto.getConfirmPw()));
                }
            }
            userRepo.save(aUser);
            return "Updated";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }

    }


    @Override
    public ArrayList<GetUserReqDto> selectUsers(String existingChars) {
        try {
            ArrayList<GetUserReqDto> userList = new ArrayList<>();
            Iterable<UserEntity> searchedUser = userRepo.findByUsernameIsContainingIgnoreCaseOrFirstnameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrGenderContainingIgnoreCase
                    (existingChars,
                            existingChars,
                            existingChars,
                            existingChars);
            for (UserEntity aUser : searchedUser) {
                if (!aUser.isDeleted()) {
                    GetUserReqDto userDto = new GetUserReqDto(aUser);
                    userList.add(userDto);
                }
            }
            return userList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }


    }
}