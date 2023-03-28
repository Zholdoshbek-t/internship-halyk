package com.halyk.study.salecard.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.halyk.study.salecard.dto.Response;
import com.halyk.study.salecard.dto.enums.Status;
import com.halyk.study.salecard.mappers.ResponseMapper;
import com.halyk.study.salecard.utils.Translator;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Response error = ResponseMapper
                .response(Translator.toLocale("unauthorized_error"), Status.ERROR, false);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }
}
