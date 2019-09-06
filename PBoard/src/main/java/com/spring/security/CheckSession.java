package com.spring.security;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextImpl;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CheckSession implements HttpSessionAttributeListener{

	public static ConcurrentHashMap<String, HttpSession> sessionMap = new ConcurrentHashMap<>();
	public void init(ServletConfig config) {}
	
	@Override
	public void attributeAdded(HttpSessionBindingEvent event) throws AuthenticationException {
		HttpSession session = event.getSession();
		SecurityContextImpl securityContextImpl = (SecurityContextImpl)event.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		if(securityContextImpl!=null) {
			Authentication authentication = securityContextImpl.getAuthentication();
			String userId = authentication.getName();
			log.info("로그인 시도. 사용자 = {} , 세션 = {} ", userId, session.getId()); 
			if(sessionMap.get(userId)!=null) {
				log.info("중복로그인. 사용자 = {}, 세션 = {} ", userId, session.getId());
			}else {
				sessionMap.put(userId,session);
				log.info("로그인 성공. 사용자 = {}, 세션 = {} ", userId, session.getId());
			}
		}
	}
	
	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		// do nothing
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		// do nothing
	}
	

}
