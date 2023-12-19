package com.appli.clcapi.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_tbl")
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String firstname;
    private String lastname;
    private String username;
    private String gender;
    private String role;
    private String email;
    private String confirmPw;
    private String password;

}
