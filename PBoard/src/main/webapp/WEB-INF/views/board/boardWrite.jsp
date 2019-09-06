<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/header.jsp"%>

<div class="container mt-5 col-md-8 order-md-1 mb-5">
	<form id="board_insert" action="/board/board_insert" method="post">
		<select name="category" class="custom-select" id="category-select">
			<option value="none">게시글 분류를 선택하세요</option> 
			<sec:authorize access="hasRole('ROLE_ADMIN')" var="admin">
				<option value="notice">--공지--</option>
			</sec:authorize>
			<option value="ask">--질문--</option>
			<option value="board">--자유--</option>
			<option value="answer">--답글--</option>
		</select>
		<input type="text" class="form-control"  id="board_title" name="board_title" placeholder="제목(최대 40자)" required maxlength="40"/>
		<input type="hidden" name="mem_no" id="memNo" value="${member_no }" />
		<input type="hidden" name="member_id" id="memId" value="${member_id }" />
		<input type="text" name="mem_name" class="form-control" id="memName" value="${member_name }" disabled/>
		<input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
		<hr>
		<textarea id="board_content" name="board_content" rows="10" class="form-control" placeholder="내용" required></textarea>
		<!-- 첨부파일 -->
		<p style="color:gray; text-align:left; margin-bottom:0px;" id="option">
			각 파일 사이즈 10MB 이하, exe/sh/alz/java/jsp/php/egg 파일은 첨부 불가</p>
		<ul class="list-group">
			<li style="list-style-type : none" class="btn-attach" >
				<input type="file" name="uploadFile" class="form-control" id="uploadFile" multiple="multiple" />
			</li>
			<li style="list-style-type : none" class="uploadevent uploadResult text-left">
			<div style="border:1px solid #ced4da">
					<div id="fileListImg">
						<span>이미지 첨부 파일 </span>
						<ul id="fileUlImg" class="list-group fileUl overflow-auto" style="flex-direction : row; border:1px solid rgba(0,0,0,.125); border-left:none;border-right:none; ">
						</ul>
					</div>
					<div id="fileListEct">
						<span>일반 첨부 파일 </span> 
						<ul id="fileUlFile" class="list-group fileUl">
						</ul>
					</div>
			</div>
			</li>
		</ul>
		
		<div class="m-4">
			<sec:authorize access="isAuthenticated()">	
				<button class="btn btn-primary btn-sm btn-insert">등록</button>
				<button class="btn btn-primary btn-sm btn-list">등록취소</button>
			</sec:authorize>
		</div>
	</form>
</div>

<script>
	$(function(){
		var jsp = "write";
		
		if(!$(".uploadResult").has($(".attahLi"))){
			$(".uploadevent").css("display","none");
		}
		$(".uploadevent").css("display", "none");
		var cloneObj = $(".btn-attach").clone();
		
		//업로드 영역에 변화 감지 시 uploadFile에 있는 정보 가져오기
		$(".btn-attach").on("change", function() {
			detect(cloneObj, jsp);
		});

		//글 등록 버튼
		$(".btn-insert").on("click", function(e) {
			e.preventDefault();
			if($("#category-select").val() == "none"){
				alert("게시글 분류를 선택하세요.");
				return false;
			}
			
			//BoardVO 안의 attach객체에 담길 값
			var lis = $(".uploadResult ul li");
			var str = addAttach(lis);
			
			if ($("#board_title").val() != "" && $("#board_content").val() != "") {
				var form = $("#board_insert").append(str).serialize();
				$.ajax({
					url : '/board/board_insert',
					data : form,
					type : "POST",
					success : function(data) {
						alert("새 글이 등록되었습니다.");
						location.replace("boardList");
					},
					error : function(data) {
						alert("등록 실패");
						return;
					}
				});//ajax
			} else {
				alert("제목과 내용은 비워둘 수 없습니다.");
				return;
			}
		});

		// X를 클릭하면 첨부된 파일 삭제하기
		$(".uploadResult").on("click",".deleteBtn", function() {
			deleteFile($(this).closest("li"));
		});
		
		$(".btn-list").on("click",function(e){
			e.preventDefault();
			if(confirm("작성 중이던 내용이 모두 사라집니다. 등록을 취소하시겠습니까?")){
				location.href="boardList";
			}else{
				return false;
			}
		});
		
		//답글모드
		var category = '${category}';
		var content = '${content}';
		var title = '${title}';
		var board_no = '${board_no}';
		if(category=='answer'){
			$("#category-select").val(category).attr("selected","selected");
			$("#category-select").attr("disabled",true);
			$("#board_insert").append("<input type='hidden' name='board_ref' value='" + board_no + "'/>");
			$("#board_insert").append("<input type='hidden' name='category' value='" + category + "'/>");
			$("#board_content").text(content + "\n--------------------------------------------------------------------------\n");
			$("#board_title").val("[RE] : " + title);
		}
	});
</script>
</body>
</html>