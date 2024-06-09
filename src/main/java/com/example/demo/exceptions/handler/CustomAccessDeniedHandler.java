package com.example.demo.exceptions.handler;

import com.example.demo.dtos.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    // cài lại access denied trả về dạng error response
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper
                .writeValueAsString(new ErrorResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        accessDeniedException.getMessage(),
                        HttpStatus.UNAUTHORIZED.name()
                ));
        response.getWriter().write(json);
    }
}
