package com.spring.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.common.Common;
import com.spring.domain.BoardAttachVO;
import com.spring.domain.BoardVO;
import com.spring.mapper.AttachMapper;
import com.spring.mapper.BoardMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("board")
public class BoardServiceImpl implements BoardService {
	
	@Autowired
	private BoardMapper mapper;
	@Autowired
	private AttachMapper attach;
	
	//리스트
	@Override
	public List<BoardVO> getOnePage(int nowPage, String align, String order) {
		log.info(align + "/" + order);
		//한 페이지에 표시되는 게시물의 시작과 끝번호를 계산
		int start = (nowPage -1) * Common.board.BLOCKLIST + 1;	//nowpage(쪽)가 1이면 0 * 10 + 1 = 1
		int end = start + Common.board.BLOCKLIST -1;	//1 + 10 -1 = 10 
		HashMap<String, Object> hash = new HashMap<>();
		hash.put("start", start);
		hash.put("end", end);
		hash.put("align", align);
		hash.put("order", order);
		List<BoardVO> list = mapper.getOnePage(hash);
		return list;
	}
	
	//조회
	@Override
	public BoardVO reading(int board_no) {
		BoardVO board = mapper.reading(board_no);
		return board;
	}
	
	//수정
	@Transactional
	@Override
	public BoardVO updating(BoardVO board) {
		HashMap<String, Object> hash = new HashMap<String, Object>();
		hash.put("board_no", board.getBoard_no());
		hash.put("board_title", board.getBoard_title());
		hash.put("board_content", board.getBoard_content());
		hash.put("category", board.getCategory());
		int result = mapper.updating(hash);
		//해당 게시글에 첨부된 파일 삭제
		attach.deleteAttach(board.getBoard_no());
		if(board.getAttach()!=null && board.getAttach().size()>0) {	//첨부파일 재등록
				board.getAttach().forEach(board_attach -> {
					board_attach.setBoard_no(board.getBoard_no());
					attach.insertAttach(board_attach);
				});
		}
		if(result>0) {
			BoardVO updatedBoard = reading(board.getBoard_no());
			return updatedBoard;
		}else {
			return null;
		}
	}
	
	//삭제
	@Override
	@Transactional
	public int delete(int board_no) {
		attach.deleteAttach(board_no);
		return mapper.delete(board_no);
	}
	
	@Transactional
	@Override
	public int insert(BoardVO board) {
		int result = mapper.insert(board);
		if(board.getAttach()!=null && board.getAttach().size() > 0) {
			board.getAttach().forEach(board_attach -> {
				board_attach.setBoard_no(board.getBoard_no());
				attach.insertAttach(board_attach);
			});
		}
		return result;
	}
	
	@Override
	public List<BoardAttachVO> attachList(int board_no) {
		return attach.getAttachs(board_no);
	}
	
	//전체게시물수
	@Override
	public int total() {
		HashMap<String, Object> hash = new HashMap<>();
		return mapper.total();
	}
	
	//검색 결과 게시물수
	@Override
	public int searchListTotal(String category, String criteria, String keyword) {
		HashMap<String, Object> hash = new HashMap<>();
		hash.put("criteria", criteria);
		hash.put("keyword", keyword);
		hash.put("category", category);
		return mapper.searchListTotal(hash);
	}

	//검색
	@Override
	public List<BoardVO> getSearchPage(int nowPage, String category, String criteria, String keyword, String align, String order) {
		int start = (nowPage -1) * Common.board.BLOCKLIST + 1;
		int end = start + Common.board.BLOCKLIST -1;
		HashMap<String, Object> hash = new HashMap<>();
		hash.put("start", start);
		hash.put("end", end);
		hash.put("criteria", criteria);
		hash.put("keyword", keyword);
		hash.put("category", category);
		hash.put("align", align);
		hash.put("order", order);
		return mapper.getSearchPage(hash);
	}
	
	//공지리스트
	@Override
	public List<BoardVO> getNotices(){
		return mapper.getNotices();
	}

}
