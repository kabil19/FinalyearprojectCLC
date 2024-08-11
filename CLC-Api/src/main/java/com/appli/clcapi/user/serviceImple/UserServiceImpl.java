package com.appli.clcapi.user.serviceImple;

import com.appli.clcapi.common.constants.UserConstants;
import com.appli.clcapi.common.enums.Roles;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.user.dto.GetUserReqDto;
import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.entity.UserEntity;
import com.appli.clcapi.user.repository.UserRepo;
import com.appli.clcapi.user.service.UserService;
import com.appli.clcapi.util.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CurrentUserService currentUserService;

    public NonPaginatedResponse insertNewUser(UserDto userDto) {

        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
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
                response.setErrors(List.of(UserConstants.USER_IS_ALREADY_EXIST));
                response.setStatus(HttpStatus.FORBIDDEN);
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setErrors(List.of("User creation is failed!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
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

//    public Optional<UserEntity> userFindByUsername(String name) {
//        NonPaginatedResponse response = new NonPaginatedResponse();
//        return userRepo.findByUsernameAndDeletedEquals(name, Boolean.FALSE);
//    }
    @Override
    public NonPaginatedResponse deleteUser(Long userId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        UserEntity aUser = userRepo.getReferenceById(userId);

        try {
            //            The person with USER role, can't delete the user data
            if(currentUserService.getCurrentUser().getRole().equals(Roles.USER)){
                response.setErrors(List.of("Role User has no privilege to delete!"));
                response.setStatus(HttpStatus.FORBIDDEN);
                return response;
            }
//            The user can't delete himself while he/she is logged in
            if (Objects.equals(currentUserService.getCurrentUser().getUserId(), userId)) {
                response.setErrors(List.of("Can't delete the user who is currently logged in!"));
                response.setStatus(HttpStatus.FORBIDDEN);
                return response;
            }

//            Admin can't be deleted from the DB
            if (aUser.getRole() == Roles.ADMIN) {
                response.setErrors(List.of("Admin can not be deleted!"));
                response.setStatus(HttpStatus.FORBIDDEN);
                return response;
            }
            aUser.setDeleted(true);
            userRepo.save(aUser);
            response.setSuccessMessage("The selected User's data is Deleted successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        return response;
    }

    @Override
    public NonPaginatedResponse updateUser(UserDto userDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            Optional<UserEntity> existingUserOptional = userRepo.findById(userDto.getUserId());
            UserEntity aUser = new UserEntity();
            //If the current logged in person's role is user then the privilege to update is restricted.
            if(currentUserService.getCurrentUser().getRole().equals(Roles.USER)){
                response.setErrors(List.of("Role User has no privilege to update!"));
                response.setStatus(HttpStatus.FORBIDDEN);
                return response;
            }
            if (existingUserOptional.isPresent()) {
                aUser = existingUserOptional.get();
                aUser.setFirstname(userDto.getFirstname());
                aUser.setLastname(userDto.getLastname());
                aUser.setRole(userDto.getRole());
                aUser.setGender(userDto.getGender());
                aUser.setEmail(userDto.getEmail());
                aUser.setUsername(userDto.getUsername());
            /*  if in the front-end request, both password and confirm password aren't given then the data
            is updated with the previous password. if not, the password is also updated as well.  */
                if (userDto.getConfirmPw() == null && userDto.getPassword() == null) {
                    //when updating w/d the same password
                    aUser.setPassword(existingUserOptional.get().getPassword());
                } else {
                    //when updating w/d a new password
                    aUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
                }
            }
            userRepo.save(aUser);
            response.setSuccessMessage("User is Successfully updated!");
            response.setStatus(HttpStatus.OK);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't update the user data!"));
            return response;
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