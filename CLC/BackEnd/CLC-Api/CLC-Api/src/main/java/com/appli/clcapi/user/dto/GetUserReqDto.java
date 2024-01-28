package com.appli.clcapi.user.dto;

import com.appli.clcapi.user.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetUserReqDto {
    @JsonProperty("userId")
    private Long userId;

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstname")
    private String firstname;

    @JsonProperty("lastname")
    private String lastname;

    @JsonProperty("gender")
    private String gender;

    @JsonProperty("role")
    private String role;

    @JsonProperty("email")
    private String email;
    @JsonProperty(value = "deleted")
    @JsonIgnore
    private boolean deleted;

    public GetUserReqDto(UserEntity userEntity){
        this.userId = userEntity.getUserId();
        this.firstname = userEntity.getFirstname();
        this.lastname = userEntity.getLastname();
        this.username = userEntity.getUsername();
        this.gender = userEntity.getGender();
        this.role = userEntity.getRole();
        this.email = userEntity.getEmail();
        this.deleted =userEntity.isDeleted();
    }
}
