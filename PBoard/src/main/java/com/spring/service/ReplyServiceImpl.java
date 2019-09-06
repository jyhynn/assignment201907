package com.spring.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.domain.ReplyVO;
import com.spring.mapper.ReplyMapper;

@Service("reply")
public class ReplyServiceImpl implements ReplyService{

	@Autowired
	private ReplyMapper mapper;
	
	@Override
	public int reply_insert(ReplyVO reply) {
		return mapper.reply_insert(reply);
	}

	@Override
	public List<ReplyVO> getAllReplies(int board_no) {
		return mapper.getAllReplies(board_no);
	}

	@Override
	public int deleteReply(int board_no, int reply_no) {
		HashMap<String, Integer> hash = new HashMap<String, Integer>();
		hash.put("board_no", board_no);
		hash.put("reply_no", reply_no);
		return mapper.deleteReply(hash);
	}

	@Override
	public int updateReply(ReplyVO reply) {
		return mapper.updateReply(reply);
	}

}
