package com.spring.mapper;

import java.util.HashMap;
import java.util.List;

import com.spring.domain.BoardVO;

public interface BoardMapper {
	
	public List<BoardVO> allList();
	public BoardVO reading(int board_no);
	public int updating(HashMap<String, Object> hash);
	public int delete(int board_no);
	public int insert(BoardVO board);
	public int total();
	public int searchListTotal(HashMap<String, Object> hash);
	public int getAlignTotal(HashMap<String, Object> hash);
	public List<BoardVO> getNotices();
	public List<BoardVO> getOnePage(HashMap<String, Object> hash);
	public List<BoardVO> getSearchPage(HashMap<String, Object> hash);
	public List<BoardVO> getAlign(HashMap<String, Object> hash);
}
