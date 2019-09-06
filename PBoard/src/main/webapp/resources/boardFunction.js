/**
 * 게시판 function 스크립트
 */

//게시글 수정시 뷰
function modifyView(isAdmin) {
	$("#category-select").prop("disabled",false);
	$("#board_title").prop("readonly",false);
	$("#board_content").prop("readonly",false);
	$("#category-select").css("background","none");
	$("#board_title").css("background","none");
	$("#board_content").css("background","none");
	$(".btn-modify").hide();
	$(".btn-delete").hide();
	$("#option").show();
	$("#FileDlAll").hide();
	$("#dlIcon").hide();
	$(".btn-list").hide();
	$(".btn-confirm").show();
	$(".btn-cancel").show();
	$(".btn-attach").show();
	$(".pageInfo").show();
	$("ul#fileUlImg li span.close").show();
	$("ul#fileUlFile li span.close").show();
	$("#optionAttach").show();
	if(isAdmin == 'false'){
		$("#category-select option").each(function(i,obj){
			if(obj.value == "notice"){
				$(obj).css("display","none");
			};
		})
	}
}

//게시글 읽기모드일 때 수정적용, 수정취소, 파일첨부, 파일삭제 버튼 숨기기
function notModifying(clone_selected){
	$(".btn-attach").hide();
	$(".btn-confirm").hide();
	$(".btn-cancel").hide();
	$(".pageInfo").hide();
	$("#option").hide();
	$(".btn-modify").show();
	$(".btn-delete").show();
	$(".btn-list").show();
	$("#option").show();
	$("#FileDlAll").show();
	$("#dlIcon").show();
	$("#fileUlImg li span.deleteBtn").hide();
	$("#fileUlFile li span.deleteBtn").hide();
}

//readonly일 경우 컬러링
function readonly(){
	$("#category-select").prop("disabled",true);
	$("#board_content").prop("readonly",true);
	$("#board_title").prop("readonly",true);
	$("#category-select").css("background","#F7F7F7");
	$("#board_content").css("background","#F7F7F7");
	$("#board_title").css("background","#F7F7F7");
}

//댓글 삭제
function deleteReply(targetLi, board_no, reply_no){
	$.ajax({
		url : "/board/delete_reply",
		data : {board_no: board_no,
				reply_no : reply_no
		},
			type : "post",
		beforeSend : function(xhr){   //데이터 전송 전에 헤더에 csrf값을 설정
			xhr.setRequestHeader($('#csrf').attr('name'), $('#csrf').attr('content'));
        },
		success : function(result){
			console.log(result);
			targetLi.remove();
		}
	});
}

//게시글에 해당하는 댓글목록 얻어오기
function getReplies(re,logedMem_no,isAdmin,board_no){ 
	$.getJSON({
		url : 'boardGetReplyList',
		data : {board_no: board_no},
		success:function(data){
			$(data).each(function(i,obj){
				var str = "";
				if((obj.mem_no == logedMem_no) || isAdmin){
					str = "<small><span id='modifyReply' style='cursor:pointer;'>수정</span>"
						+ " <span id='modifyReplyConfirm' style='display:none; cursor:pointer;'>수정확인</span>"
						+ " <span id='modifyReplyCancel' style='display:none; cursor:pointer;'>수정취소</span>"
						+ " <span id='deleteReply' class='close' style='cursor:pointer;'>&times;</span></small>"
				}
				re.append("<li"
						+ " data-bno='" + obj.board_no 
						+ "' data-rno='" + obj.reply_no 
						+ "' class='list-group-item'>"
						+ "<div class='d-flex w-100 justify-content-between'>"
						+ "<span class='mb-1'><b>" + obj.mem_name + "</b> "
						+ "<small style='color:gray;'>" + obj.reply_date + "</small></span>"
					    + str 
					    + "</div>"
					    + "<textarea class='form-control'"
					    + " disabled='true'"
					    + " maxlength='330'"
					    + " style='background-color:white; border:none; padding:0px;'>"
					    + obj.reply 
					    + "</textarea>"
					    + "</li>");
			});
			
			//textarea 내용의 길이에 맞게 높이 조절
			var h = $("#replyList li").find('textarea');
			h.each(function(i,obj){
				h[i].rows = h[i].scrollHeight/21;
			})
		}
	});
}
//페이징
function pagination(nowpage, ctg, cri, kwd, sort, order){
	var table = $("#tt");
	if(ctg == "null"){
		ctg == null;
	}
	if(cri == "null"){
		cri == null;
	}
	if(kwd == "null"){
		kwd == null;
	}
	$.getJSON({
		url : 'pagination',
		data : {page : nowpage,
			category : ctg,
			criteria : cri,
			keyword : kwd,
			align : sort,
			order : order
		},
		success : function(result){
			table.empty();
			if(result.list.length==0){
				table.append("<tr><th colspan='5' scope='row'>데이터가 없습니다.</th></tr>");
			}
			$(result.list).each(function(i,obj){
				var str = ""
				var atag = "<a href='/board/boardRead?board_no="+obj.board_no+"&page=" + result.nowPage + "'>"
				var depth = obj.board_depth;
				var ctg = "";
				switch(obj.category){
				case "notice" : ctg = "공지"; break;
				case "ask" : ctg = "질문"; break;
				case "board" : ctg = "자유"; break;
				case "answer" : ctg = "답글"; break;
				}
				if(depth == 1){
					str = "&nbsp;&nbsp;&nbsp;&nbsp;ㄴ ";
				}
				if(result.isSearched == true){
					atag = "<a href='/board/boardRead?board_no="+obj.board_no+"&page=" + result.nowPage
						+ "&category=" + obj.category + "&criteria=" + result.criteria + "&keyword=" + result.keyword + "&isSearched=true'>"
				}
				table.append("<tr>"
							+ "<th scope='row' style='padding:.5rem;'>" + obj.board_no + "</th>"
							+ "<td style='padding:.5rem;'>" + ctg + "</td>"
							+ "<td class='text-left' style='padding:.5rem;'>"
							+ str
							+ atag
							+ obj.board_title
							+ "</a></td>"
							+ "<td style='padding:.5rem;'>" + obj.mem_name + "</td>"
							+ "<td style='padding:.5rem;'>" + obj.board_date + "</td>"
							+ "</tr>"
				);
				
			});
			$("#paging").html(result.pageMenu);
			$("#total").html("총 <b>" + result.total_row + "</b>개의 게시물");
		},
		error : function(result){
			console.log("ㅠㅠ");
		}
	});//리스트json
}