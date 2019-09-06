package com.spring.domain;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Getter
public class CustomUserDetails implements UserDetails{

	private static final long serialVersionUID = -4086869747130410600L;
	private MemberVO vo;
	private String mem_name;
	private int mem_no;

	public CustomUserDetails(MemberVO vo) {
		super();
		this.vo = vo;
		this.mem_name = vo.getMem_name();
		this.mem_no = vo.getMem_no();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		ArrayList<GrantedAuthority> auth = new ArrayList<GrantedAuthority>();
        auth.add(new SimpleGrantedAuthority(vo.getMem_auth()));
        return auth;
	}

	@Override
	public String getPassword() {
		return vo.getMem_pwd();
	}

	@Override
	public String getUsername() {
		return vo.getMem_id();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public MemberVO getVo() {
		return vo;
	}

	public String getMem_name() {
		return mem_name;
	}

	public int getMem_no() {
		return mem_no;
	}
	
	@Override

	public boolean equals(Object otherUser) {
		System.out.println("otherUser.hashCode() " + otherUser.hashCode() );
		if (otherUser.hashCode() == hashCode()) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
	    return vo.getMem_id().hashCode() ;
	}
	
}
