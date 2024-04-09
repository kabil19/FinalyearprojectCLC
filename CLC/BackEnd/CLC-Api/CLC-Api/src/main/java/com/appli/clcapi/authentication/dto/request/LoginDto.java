package com.appli.clcapi.authentication.dto.request;

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
    @JsonProperty("userName")
    private String username;

    @JsonProperty("password")
    private String password;
}
