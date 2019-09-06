package com.spring.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.common.Common;
import com.spring.common.Paging;
import com.spring.domain.BoardAttachVO;
import com.spring.domain.BoardVO;
import com.spring.domain.ReplyVO;
import com.spring.service.BoardService;
import com.spring.service.ReplyService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/board/*")
public class BoardController {
	
	@Autowired
	private BoardService service;
	@Autowired
	private ReplyService reply;
	
	@RequestMapping("/boardList")
	public void boardList(String page,String category, String criteria, String keyword, Model model) {
		log.info("-------------게시판 리스트 출력");
		//공지
		List<BoardVO> notices = getNotices();
		model.addAttribute("isList", true);
		model.addAttribute("notices", notices);	//공지
		model.addAttribute("page", page);
		model.addAttribute("category", category);
		model.addAttribute("keyword", keyword);
		model.addAttribute("criteria", criteria);
	}
	
	//ajax 페이징
	@ResponseBody
	@RequestMapping("/pagination")
	public ResponseEntity<HashMap<String, Object>> pagination(String page, String category, 
										String criteria, String keyword, String align, String order) {
		log.info(category + "/" + criteria + "/" + keyword);
		HashMap<String, Object> hash = new HashMap<String, Object>();
		int total_row = 0; 
		int nowPage = 1;
		String pageMenu = "";
		if(category != null || criteria != null || keyword != null) {
			if(category.equals("")) category = null;
			if(criteria.equals("")) criteria = null;
			if(keyword!=null && (keyword.equals("") || keyword.equals("null"))) keyword = null;
		}
		if(category==null&&criteria==null&&keyword==null) {
			total_row = service.total();
			if(total_row>0) {
				if (page != null && !page.isEmpty()) {
					nowPage = Integer.parseInt(page);
				} else {
					page = "1";
				}
				pageMenu = Paging.getPaging("javascript:pagination", nowPage, total_row,
						Common.board.BLOCKLIST, Common.board.BLOCKPAGE, false, category, criteria, keyword, align, order);
			}
			hash.put("list", service.getOnePage(nowPage,align,order));
			hash.put("isSearched", false);
		}else {
			total_row = service.searchListTotal(category, criteria, keyword);
			if(total_row>0) {
				if (page != null && !page.isEmpty()) {
					nowPage = Integer.parseInt(page);
				} else {
					page = "1";
				}
				pageMenu = Paging.getPaging("javascript:pagination", nowPage, total_row,
						Common.board.BLOCKLIST, Common.board.BLOCKPAGE, true, category, criteria, keyword, align, order);
			}
			hash.put("list", service.getSearchPage(nowPage, category, criteria, keyword, align, order));
			hash.put("isSearched", true);
		}
		hash.put("total_row",total_row);
		hash.put("nowPage",nowPage);
		hash.put("pageMenu",pageMenu);
		hash.put("criteria",criteria);
		hash.put("keyword",keyword);
		return new ResponseEntity<>(hash, HttpStatus.OK);
	}
	
	//글 조회
	@RequestMapping("/boardRead")
	public void boardRead(int board_no, Model model, boolean isSearched, String category, 
							String criteria, String keyword) {
		log.info("-------------게시판 뷰페이지 로드");
		BoardVO board = service.reading(board_no);
		model.addAttribute("board", board);
		model.addAttribute("isSearched", isSearched);
		if(category!=null || criteria != null || keyword != null) {
			model.addAttribute("ctg", category);
			model.addAttribute("cri", criteria);
			model.addAttribute("kwd", keyword);
		}
	}
	
	//글 수정
	@ResponseBody
	@RequestMapping(value="/updating", method = { RequestMethod.POST })
	public BoardVO updating(BoardVO board, String member_id, Principal principal) {
		log.info("-------------게시글 수정 처리");
		//권한체크
		if(board.getAttach()!=null) {
			for(int i = 0; i<board.getAttach().size(); i++) {
				log.debug(i + " ----- " + board.getAttach().get(i).getFileName());
				log.debug(i + " ----- " + board.getAttach().get(i).getUploadPath());
			}
		}
		if(member_id.equals(principal.getName()) || member_id.equals("admin")){
			log.info("------------------ 멤아이디" + member_id);
			return service.updating(board);
		}else {
			return null;
		} 
	}
	
	// 글 삭제
	@RequestMapping("/delete")
	@ResponseBody
	public String delete(int board_no, String member_id, Principal principal) {
		log.info("-------------게시글 삭제 처리");
		if(member_id.equals(principal.getName()) || member_id.equals("admin")){
			int result = service.delete(board_no);
			if(result>0) {
				return "deleted";
			}else {
				return "deleteFailed";
			}
		}else {
			return "deleteFailed";
		}
	}
	
	@RequestMapping("/boardWrite")
	public void insertPage(String page, String mem_no, String board_no, 
						   String category, String content, String title, Model model) {
		if(page==null || mem_no==null || board_no==null) {
			log.info("-------------게시글 등록 페이지 로드");
		}else {
			log.info("-------------답글 작성 페이지 로드");
			model.addAttribute("page", page);
			model.addAttribute("mem_no", mem_no);
			model.addAttribute("board_no", board_no);
			model.addAttribute("category", category);
			model.addAttribute("content", content);
			model.addAttribute("title", title);
		}
	}
	
	//등록
	@ResponseBody
	@RequestMapping(value = "/board_insert", method = { RequestMethod.POST })
	public String insert(BoardVO board, String member_id, Principal principal) {
		log.info("-------------게시글 등록 처리 : ");
		if(board.getAttach()!=null) {
			for(int i = 0; i<board.getAttach().size(); i++) {
				log.info(i + " ----- " + board.getAttach().get(i).getFileName());
			}
		}
		int result = service.insert(board);
		if(result > 0) {
			return "insertScs";
		}else {
			return "insertFld";
		}
	}
	
	//글첨부목록
	@RequestMapping("/boardGetAttachList")
	@ResponseBody
	public List<BoardAttachVO> getAttList(int board_no) {
		log.info("-------------------- 첨부파일 읽어오기");
		return service.attachList(board_no);
	}
	
	//댓글목록
	@RequestMapping("/boardGetReplyList")
	@ResponseBody
	public List<ReplyVO> getReplyList(int board_no) {
		log.info("-------------------- 댓글 얻어오기");
		return reply.getAllReplies(board_no);
	}
	
	//댓글 삽입
	@ResponseBody
	@RequestMapping(value="/insert_reply", method = RequestMethod.POST)
	public String insertReply(ReplyVO aReply) {
		log.info("-------------------- 댓글 입력");
		int result = reply.reply_insert(aReply);
		if(result > 0) {
			return "insertSuccess";
		}else {
			return "insertFailed";
		}
	}
	
	//댓글 삭제
	@ResponseBody
	@RequestMapping(value="/delete_reply", method = RequestMethod.POST)
	public String deleteReply(int board_no, int reply_no) {
		int result = reply.deleteReply(board_no, reply_no);
		if(result > 0) {
			return "deleteSuccess";
		}else {
			return "deleteFailed";
		}
	}
	
	//댓글 수정
	@ResponseBody
	@RequestMapping(value="/update_reply", method = RequestMethod.POST)
	public String updateReply(ReplyVO modReply) {
		int result = reply.updateReply(modReply);
		if(result > 0) {
			return "updateSuccess";
		}else {
			return "updateFailed";
		}
	}
	
	//공지 최근 3개 리스트
	private List<BoardVO> getNotices(){
		return service.getNotices();
	}
	
}
