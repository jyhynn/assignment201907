package com.spring.service;

import java.util.List;

import com.spring.domain.ReplyVO;

public interface ReplyService {
	public int reply_insert(ReplyVO reply);
	public List<ReplyVO> getAllReplies(int board_no);
	public int deleteReply(int board_no, int reply_no);
	public int updateReply(ReplyVO reply);
}
