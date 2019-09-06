package com.spring.mapper;

import java.util.HashMap;
import java.util.List;

import com.spring.domain.ReplyVO;

public interface ReplyMapper {
	public int reply_insert(ReplyVO reply);
	public List<ReplyVO> getAllReplies(int board_no);
	public int deleteReply(HashMap<String, Integer> hash);
	public int updateReply(ReplyVO reply);
}
