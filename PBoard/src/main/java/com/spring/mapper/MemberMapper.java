package com.spring.mapper;

import com.spring.domain.MemberVO;

public interface MemberMapper {
	
	public String getNickname(int mem_no);
	public MemberVO read(String username);
}
