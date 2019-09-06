package com.spring.security;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.domain.CustomUserDetails;
import com.spring.domain.MemberVO;
import com.spring.mapper.MemberMapper;

import lombok.extern.slf4j.Slf4j;

//DB에서 유저 정보를 직접 가져오는 인터페이스 구현
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	//ConcurrentHashMap<String, HttpSession> sessionMap = CheckSession.sessionMap;
	@Autowired
	private MemberMapper mapper;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, CustomException {

		log.warn("Load User by username : " + username);
		MemberVO cud = mapper.read(username);
		if(cud==null) {
			throw new UsernameNotFoundException(username);
		}
		
		/*
		 * if(cud!=null && sessionMap.get(username)!=null) { throw new
		 * CustomException("duplicated"); }
		 */
		
		//return cud==null || (cud!=null && sessionMap.get(username)!=null)? null : new CustomUserDetails(cud);
		return new CustomUserDetails(cud);
	}
	
}
