package com.spring.security;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.session.SessionDestroyedEvent;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.spring.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogoutHandler extends SimpleUrlLogoutSuccessHandler implements ApplicationListener<SessionDestroyedEvent> {

	//ConcurrentHashMap<String, HttpSession> sessionMap = CheckSession.sessionMap;
	@Autowired
	MemberMapper mapper;

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		super.onLogoutSuccess(request, response, authentication);
		log.warn("로그아웃 클릭");
		//sessionMap.remove(authentication.getName());
	}

	@Override
	public void onApplicationEvent(SessionDestroyedEvent event) {
		/*
		 * List<SecurityContext> securityContexts = event.getSecurityContexts(); for
		 * (SecurityContext securityContext : securityContexts) {
		 * //sessionMap.remove(securityContext.getAuthentication().getName()); }
		 */
		log.warn("세션만료 로그아웃");
	}

}
