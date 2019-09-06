<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/header.jsp"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> --%>

<div class="container mt-5 col-md-8 order-md-1 mb-3">
	<h6 class="pageInfo">게시물 수정</h6>
	<form action="board_update" id="board_update">
		<select name="category" class="custom-select" id="category-select">
			<option value="none">게시글 분류를 선택하세요</option> 
			<option value="notice">--공지--</option>
			<option value="ask">--질문--</option>
			<option value="board">--자유--</option>
			<option value="answer">--답글--</option>
		</select>
		<input type="hidden" name="board_no" id="board_no" value="${board.board_no }" readonly="readonly"/>
		<input type="hidden" name="member_id" value="${member_id}"/>
		<input type="text" class="form-control" id="board_title" name="board_title" value="${board.board_title }"/>
		<p class="mt-3">${board.mem_name } &nbsp; &nbsp; ${board.board_date}</p>
		<hr>
		<textarea id="board_content" name="board_content" class="form-control" rows="10" readonly="readonly">${board.board_content }</textarea>
		<input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
		<!-- 첨부파일 -->
		<ul class="list-group pull-right" id="lst">
			<li class="btn-attach">
				<p style="color:gray; text-align:left; margin-bottom:0px; list-style:none;" id="option">
					각 파일 사이즈 10MB 이하, exe/sh/alz/java/jsp/php/egg 파일은 첨부 불가</p>
				<input type="file" name="uploadFile" class="form-control" id="uploadFile" multiple="multiple"/>
			</li>
			<li style="list-style-type : none;" class="uploadevent uploadResult text-left">
				<p style="color:gray; display:inline;" id="optionAttach">첨부파일 <span id="fileCount"></span></p>
				&nbsp;
				<img alt="download" id="dlIcon" src="/resources/icons/icon-download.png" width="15" height="15">
				<a id="FileDlAll" href="#">전체 다운로드</a>
					<div style="border:1px solid #ced4da">
						<div id="fileListImg">
							<span>이미지 첨부 파일 </span>
							<ul id="fileUlImg" class="list-group" style="flex-direction : row; border:1px solid rgba(0,0,0,.125); border-left:none;border-right:none; overflow-y: hidden">
							</ul>
						</div>
						<div id="fileListEct">
							<span>일반 첨부 파일 </span> 
							<ul id="fileUlFile" class="list-group">
							</ul>
						</div>
					</div>
			</li>
		</ul>
		
		<!-- 이미지 크게보기 모달 -->
		<div id="myModal" class="modal" style="background-color: rgba(0, 0, 0, 0.3);">
			<span class="close">&times;</span> 
				<div class="modal-dialog">
					<img class="modal-content" >
				</div>
		</div>

	</form>
</div>
<div class="mb-2">
	<c:if test="${member_no == board.mem_no || admin}">
			<a class="btn btn-primary btn-sm btn-modify" href="#" role="button">수정</a>
			<a class="btn btn-primary btn-sm btn-confirm" href="#" role="button">수정적용</a>
			<a class="btn btn-primary btn-sm btn-delete" href="#" role="button">삭제</a>
			<a class="btn btn-primary btn-sm btn-cancel" href="#" role="button">수정취소</a>
	</c:if>
	<a class="btn btn-primary btn-sm btn-list" href="#" role="button">목록으로</a>
	<c:if test="${board.category ne 'answer' }">
		<a class="btn btn-primary btn-sm btn-answer" href="#" role="button">답글</a>
	</c:if>
	<c:if test="${isSearched==true }">
		<a class="btn btn-primary btn-sm btn-search-list" href="#" role="button">검색목록으로</a>
	</c:if>
</div>

<!-- 댓글입력 -->
<div class="input-group mb-3 container mt-3 col-md-8 order-md-1">
	<div class="input-group-prepend">
		<span class="input-group-text" id="inputGroupFileAddon01">${member_name }</span>
	</div>
	<input type="text" class="form-control" id="reply" name="reply" placeholder="댓글 입력(최대 330자)" maxlength="330">
	<div class="input-group-append">
		<button class="btn btn-outline-secondary" id="btn-insertRe" type="button" id="button-addon2">댓글 등록</button>
	</div>
</div>

<!-- 댓글목록 -->
<div id="replies" class="mb-5 container mt-3 col-md-8 order-md-1">
	<ul id="replyList" class="list-group"></ul>
</div>
<script>
	$(function(){
		
		var jsp = "read";
		var changing = false;
		var isAdmin = ${admin};
		var logedMem_no = ${member_no};
		var writeMem_no = ${board.mem_no};
		var board_no = ${board.board_no};
		var re = $("#replyList");	
		
		console.log();
		
		readonly();
		notModifying();
		getReplies(re,logedMem_no,isAdmin, board_no);	//댓글목록
		
		var selected = "${board.category}";
		$("#category-select").val(selected);
		
		//수정전 내용 복사
		var clone_title = $('#board_title').val();
		var clone_content = $('#board_content').val();
		var cloneObj = $(".btn-attach").clone();
		var cloneList = $("#lst").clone();
		var clone_selected = $("#category-select").clone();
		
		//수정적용
		$(".btn-confirm").on("click", function(e) {
			e.preventDefault();
			
			//BoardVO 안의 attach객체에 담길 값
			var lis = $(".uploadResult ul li");
			var str = addAttach(lis);
			
			if($("#board_title").val() != null 
					&& $("#board_content").val() != null 
					&& $("#category-select").val() != "none"){
				var conf = confirm("수정하시겠습니까?");
				var form = $("#board_update").append(str).serialize();
				if(conf){
					$.ajax({
						url : 'updating',
						type : "post",
						data : form,
						success : function(data) {
							if(data!=null){
								alert("수정되었습니다.");
								$('#board_title').val(data.board_title);
								$('#board_content').val(data.board_content);
								$("#category-select").val(data.category);
								readonly();
								notModifying();
							}else{
								alert("수정 실패");
								return;
							}
						}
					});//ajax
				}//conf
			}else{
				alert("제목과 내용은 비워둘 수 없습니다.");
				return;
			}
		});
		
		// 수정취소
		$(".btn-cancel").on("click",function(){
			var con = confirm("수정하던 내용이 사라집니다. 수정을 취소하시겠습니까?");
			if(con){
				$('#board_title').val(clone_title);
				$('#board_content').val(clone_content);
				$("#category-select").val(selected);
				$("#fileUlImg li").remove();
				$("#fileUlFile li").remove();
				$.getJSON({
					url : 'boardGetAttachList',
					data : {board_no:'${board.board_no }'},
					success:function(data){
						showUploadedFile(data, jsp);
					}
				});
				readonly();
				notModifying();
				changing = false;
			}
		});
		
		// 바로 삭제버튼 눌렀을 때
		$(".btn-delete").on("click",function(){
			var conf = confirm("삭제하시겠습니까?");
			if(conf){
				$.ajax({
					url : 'delete',
					data : {board_no : $("#board_no").val(),
							member_id : '${member_id}'},
					success : function(data) {
						alert("삭제되었습니다. 리스트로 이동합니다.");
						location.replace("boardList");
					},
					error : function(data){
						alert("삭제 실패");
						return;
					}
				});
			}
		});
		
		//목록으로
		$(".btn-list").on("click",function(){
			location.replace("boardList?page=${param.page}");
		});
		
		//검색목록으로
		$(".btn-search-list").on("click",function(){
			location.replace("boardList?page=${param.page}&category=${ctg}&criteria=${cri}&keyword=${kwd}");
		});
		
		//답글
		$(".btn-answer").on("click",function(){
			var inputs = '';
			inputs+='<input type="hidden" name="_csrf" value="'+ $('#csrf').attr('content') +'" />'; 
			inputs+='<input type="hidden" name="board_no" value="'+ board_no +'" />'; 
			inputs+='<input type="hidden" name="mem_no" value="'+ logedMem_no +'" />'; 
			inputs+='<input type="hidden" name="category" value="answer" />'; 
			inputs+='<input type="hidden" name="page" value="'+ ${param.page} +'" />'; 
			inputs+='<input type="hidden" name="title" value="'+ $("#board_title").val() +'" />'; 
			inputs+='<input type="hidden" name="content" value="'+ $("#board_content").val() +'" />'; 
			$('<form id="goAnswer" action="boardWrite" method="post">' + inputs + '</form>').appendTo('body');
			$("#goAnswer").submit().remove();
		});
		
		//업로드 영역에 변화 감지 시 uploadFile에 있는 정보 가져오기
		$(".btn-attach").on("change",function(){ detect(cloneObj, jsp, changing); });
		
		// 수정
		$(".btn-modify").on("click", function(){ 
			changing = true;
			modifyView(isAdmin, changing); });

		//첨부파일 불러오기 function>> fileControl.js
		
		//게시글에 해당하는 첨부파일 얻어오기
		$.getJSON({
			url : 'boardGetAttachList',
			data : {board_no:'${board.board_no }'},
			success:function(data){
				showUploadedFile(data, jsp);
			}
		});
		
		//댓글 입력
		$("#btn-insertRe").on("click",function(){
			console.log("댓글클릭");
			$.ajax({
				url : "/board/insert_reply",
				data : {board_no:'${board.board_no }',
						reply : $("#reply").val(),
						mem_no : logedMem_no,
				},
 				type : "post",
				beforeSend : function(xhr){   //데이터 전송 전에 헤더에 csrf값을 설정
					xhr.setRequestHeader($('#csrf').attr('name'), $('#csrf').attr('content'));
		        },
				success : function(result){
					console.log(result);
					$("#reply").val("");
					re.empty();
					getReplies(re,logedMem_no,isAdmin, board_no);
				}
			});
		});
		
		//댓글 삭제
		re.on("click", "#deleteReply", function(){
			if(confirm("댓글을 삭제하시겠습니까?")){
				deleteReply($(this).closest("li"), 
							$(this).closest("li").data("bno"),
							$(this).closest("li").data("rno"));
			}
		});
		
		var origin;
		//댓글 수정하기 버튼
		re.on("click", "#modifyReply", function(){
			var own = $(this).closest("li");
			var thisInput = own.find("textarea");
			origin = own.find("textarea").val();
			own.find("#modifyReply").hide();
			own.find("#modifyReplyCancel").show();
			own.find("#modifyReplyConfirm").show();
			thisInput.css("background-color","rgb(247, 247, 247)");
			thisInput.css("border","show");
			thisInput.css("padding-left","6px");
			thisInput.attr("disabled",false);
			
			
		});
		
		//댓글 수정 적용
		re.on("click","#modifyReplyConfirm", function(){
			var own = $(this).closest("li");
			if(confirm("댓글을 수정하시겠습니까?")){
				$.ajax({
					url : "/board/update_reply",
					data : {board_no: own.data("bno"),
							reply_no : own.data("rno"),
							reply : own.find("textarea").val()
					},
	 				type : "post",
					beforeSend : function(xhr){   //데이터 전송 전에 헤더에 csrf값을 설정
						xhr.setRequestHeader($('#csrf').attr('name'), $('#csrf').attr('content'));
			        },
					success : function(result){
						re.empty();
						getReplies(re,logedMem_no,isAdmin,'${board.board_no }');
					}
				});
			}
		});
		
		//댓글 수정 취소
		re.on("click", "#modifyReplyCancel",function(){
			var own = $(this).closest("li");
			var thisInput = own.find("textarea");
			if(confirm("댓글수정을 취소하시겠습니까?")){
				thisInput.val(origin);
				thisInput.css("background-color","white");
				thisInput.attr("disabled",true);
				thisInput.css("border","none");
				thisInput.css("padding","0px");
				own.find("#modifyReplyConfirm").hide();
				own.find("#modifyReplyCancel").hide();
				own.find("#modifyReply").show();
			}
		});			
		
		// X를 클릭하면 첨부된 파일 삭제하기
		$(".uploadResult").on("click", ".deleteBtn", function(){
			if(confirm("파일을 삭제하시겠습니까?")){				
				deleteFile($(this).closest("li"), '${board.board_no }');
			}
		});
		
		// 모달 팝업 열기 (이미지 클릭 시 크게 보기)
		$(".uploadResult ul").on("click","img",function(){
			console.log("이미지 보기");
			var targetImg = $(this).data("ori");	//원본이미지경로
			console.log(targetImg);
			$("#showImages").fadeIn();
			$(".modal").find(".modal-content").attr("src", '/display?fileName=' + targetImg);
			$(".modal").fadeIn();
		});
		
		// 모달 팝업 닫기
		$("span").on("click", function () {
			$(".modal").fadeOut(); //팝업 닫기
		});
		
		// 파일 전체 다운로드
		$("#FileDlAll").on("click",function(){
			var fileList = new Array();
			$("ul#fileUlImg li").each(function(i, obj){
				var dlFile = dlFileAllList(obj);
				console.log(dlFile);
				fileList.push(dlFile);
			});
			$("ul#fileUlFile li").each(function(i, obj){
				var dlFile = dlFileAllList(obj);
				console.log(dlFile);
				fileList.push(dlFile);
			});
			console.log(fileList);
			downloadZip(fileList);
		});
		
	});
</script>
</body>
</html>