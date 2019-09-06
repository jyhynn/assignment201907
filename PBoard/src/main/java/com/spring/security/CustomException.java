package com.spring.security;

import org.springframework.security.core.AuthenticationException;

public class CustomException extends AuthenticationException{

	private static final long serialVersionUID = 1L;
	public CustomException(String message) {
		super(message);
		
	}
	
	
}
