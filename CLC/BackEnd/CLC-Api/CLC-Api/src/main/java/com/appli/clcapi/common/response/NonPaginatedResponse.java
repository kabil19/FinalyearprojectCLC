package com.appli.clcapi.common.response;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class NonPaginatedResponse {
    private boolean isPaginated ;
    private Object result;
    private List<String> errors;
    private String successMessage;
    public NonPaginatedResponse(){
        isPaginated = Boolean.FALSE;
        result = null;
        errors = new ArrayList<String>();
        successMessage = null;
    }
}
