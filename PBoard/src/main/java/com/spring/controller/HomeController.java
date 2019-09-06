package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.spring.domain.MemberVO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class HomeController {
	
	//첫화면
	@RequestMapping(value = {"/", "/home"}, method = {RequestMethod.GET, RequestMethod.POST})
	public String home(String error, Model model) {
		log.info("-------------첫화면 출력 : " + error);
		String errormsg="";
		if(error!=null) {
			errormsg = "loginfailed";
			if(error.equals("duplicated")) {
				errormsg = "duplicated";
			}
		}
		model.addAttribute("error", errormsg);
		return "/home";
	}
	
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public String login(MemberVO mem) {
		return "board/boardList";
	}

	@RequestMapping("/login_denied")
	public String login_denied() {
		log.info("-------------access 거부");
		return "login_denied";
	}
	
}
