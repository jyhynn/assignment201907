package com.spring.mapper;

import java.util.List;

import com.spring.domain.BoardAttachVO;

public interface AttachMapper {
	public int insertAttach(BoardAttachVO attach);
	public List<BoardAttachVO> getAttachs(int board_no);
	public int deleteAttach(int board_no);
}
