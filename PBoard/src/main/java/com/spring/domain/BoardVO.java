package com.spring.domain;

import java.util.List;

import lombok.Data;

//@Data
public class BoardVO {
	private int board_no;
	private String board_title, board_content;
	private int mem_no;
	private String board_date;
	private String mem_name;
	private List<BoardAttachVO> attach;	//첨부파일
	private String category;
	private int board_depth;
	private int board_ref;
	public int getBoard_no() {
		return board_no;
	}
	public void setBoard_no(int board_no) {
		this.board_no = board_no;
	}
	public String getBoard_title() {
		return board_title;
	}
	public void setBoard_title(String board_title) {
		this.board_title = board_title;
	}
	public String getBoard_content() {
		return board_content;
	}
	public void setBoard_content(String board_content) {
		this.board_content = board_content;
	}
	public int getMem_no() {
		return mem_no;
	}
	public void setMem_no(int mem_no) {
		this.mem_no = mem_no;
	}
	public String getBoard_date() {
		return board_date;
	}
	public void setBoard_date(String board_date) {
		this.board_date = board_date;
	}
	public String getMem_name() {
		return mem_name;
	}
	public void setMem_name(String mem_name) {
		this.mem_name = mem_name;
	}
	public List<BoardAttachVO> getAttach() {
		return attach;
	}
	public void setAttach(List<BoardAttachVO> attach) {
		this.attach = attach;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public int getBoard_depth() {
		return board_depth;
	}
	public void setBoard_depth(int board_depth) {
		this.board_depth = board_depth;
	}
	public int getBoard_ref() {
		return board_ref;
	}
	public void setBoard_ref(int board_ref) {
		this.board_ref = board_ref;
	}
}
