package com.spring.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.spring.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		//로그인 성공하면 기본값들이 Autentication에 담기게 됨
		//다른 브라우저 중복 로그인 방지를 위해 로그인 상태값 등록
		List<String> roleNames = new ArrayList<String>();
		authentication.getAuthorities().forEach(author -> {
			roleNames.add(author.getAuthority());
		});
		
		if (roleNames.contains("ROLE_USER") || roleNames.contains("ROLE_ADMIN")) {
			response.sendRedirect("/board/boardList");
			return;
		}
		response.sendRedirect("/board/boardList");
	}
}
