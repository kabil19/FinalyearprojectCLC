package com.appli.clcapi.common.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NonPaginatedResponse {
    private boolean isPaginated ;
    private Object result;
    private List<String> errors;
    private String successMessage;
    private HttpStatus status;
    public NonPaginatedResponse(){
        isPaginated = Boolean.FALSE;
        result = null;
        errors = new ArrayList<>();
        successMessage = null;
        status = null;
    }
}
