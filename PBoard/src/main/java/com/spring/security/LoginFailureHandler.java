package com.spring.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		log.warn("Login failure");
		String ex = exception.getMessage();
		log.warn("------------------ : " + ex);
		String error = "";
		
		if(ex.equals("Bad credentials")) {
			error = "loginfailed";
		}
		if(ex.equals("duplicated")) {
			error = "duplicated";
		}
		response.sendRedirect("/home?error=" + error);
	}
}
