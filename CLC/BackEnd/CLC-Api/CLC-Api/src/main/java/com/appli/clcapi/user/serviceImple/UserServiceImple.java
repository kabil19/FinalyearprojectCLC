package com.appli.clcapi.user.serviceImple;

import com.appli.clcapi.user.dto.GetUserReqDto;
import com.appli.clcapi.user.dto.UserDto;
import com.appli.clcapi.user.entity.UserEntity;
import com.appli.clcapi.user.repository.UserRepo;
import com.appli.clcapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImple implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

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

                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .confirmPw(passwordEncoder.encode(userDto.getConfirmPw()))
                    .build();
            userRepo.save(user);
            return "Added";
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("User Registration Failed");
        }
    }


    public List<GetUserReqDto> getAllUsers() {
        try {
            List<GetUserReqDto> userList = new ArrayList<>();
            List<UserEntity> userEntityList = userRepo.findAllByDeletedEquals(false);

            for (UserEntity aUser : userEntityList) {
                GetUserReqDto userDto = new GetUserReqDto(aUser);
                userList.add(userDto);
            }
//            List<GetUserReqDto> userlist = userEntityList.stream()
//                    .map(GetUserReqDto::new)
//                    .collect(Collectors.toList());
            return userList;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("User Not Found");
        }

    }

    @Override
    public String deleteUser(Long userId) {
        try {
            UserEntity aUser = userRepo.getReferenceById(userId);
            aUser.setDeleted(true);
            userRepo.save(aUser);
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
            UserEntity aUser = new UserEntity();
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
                    aUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
                    aUser.setConfirmPw(passwordEncoder.encode(userDto.getConfirmPw()));
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
                if(!aUser.isDeleted()){
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