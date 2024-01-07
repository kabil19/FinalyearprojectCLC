package com.appli.clcapi.userlogin.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;
}
