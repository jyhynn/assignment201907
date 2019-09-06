package com.spring.domain;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

//@Data
public class MemberVO implements Serializable{
	private static final long serialVersionUID = 1L;
	private int mem_no;
	private String mem_id, mem_name, mem_pwd;
	private String mem_auth;
	private boolean enabled;
	public int getMem_no() {
		return mem_no;
	}
	public void setMem_no(int mem_no) {
		this.mem_no = mem_no;
	}
	public String getMem_id() {
		return mem_id;
	}
	public void setMem_id(String mem_id) {
		this.mem_id = mem_id;
	}
	public String getMem_name() {
		return mem_name;
	}
	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}
	public String getMem_pwd() {
		return mem_pwd;
	}
	public void setMem_pwd(String mem_pwd) {
		this.mem_pwd = mem_pwd;
	}
	public String getMem_auth() {
		return mem_auth;
	}
	public void setMem_auth(String mem_auth) {
		this.mem_auth = mem_auth;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
