package com.spring.service;

import java.util.List;

import com.spring.domain.BoardAttachVO;
import com.spring.domain.BoardVO;

public interface BoardService {

	public BoardVO reading(int board_no);
	public BoardVO updating(BoardVO board);
	public int delete(int board_no);
	public int insert(BoardVO board);
	public int total();
	public int searchListTotal(String category, String criteria, String keyword);
	public List<BoardVO> getNotices();
	public List<BoardVO> getOnePage(int nowPage, String align, String order);
	public List<BoardVO> getSearchPage(int nowPage, String category, String criteria, String keyword, String align, String order);
	public List<BoardAttachVO> attachList(int board_no);
}
