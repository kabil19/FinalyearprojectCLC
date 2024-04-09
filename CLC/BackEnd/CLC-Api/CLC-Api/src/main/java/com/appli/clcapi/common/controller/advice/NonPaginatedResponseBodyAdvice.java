package com.appli.clcapi.common.controller.advice;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(annotations = RestControllerAdvice.class)
public class NonPaginatedResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response)
    {
        if(body instanceof NonPaginatedResponse){
            NonPaginatedResponse nonPaginatedResponse = (NonPaginatedResponse) body;
            if(!nonPaginatedResponse.getErrors().isEmpty()) {
                response.setStatusCode(HttpStatus.BAD_REQUEST);
            }
        }
        return null;
    }
}
