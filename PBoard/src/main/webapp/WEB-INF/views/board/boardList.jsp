<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="../include/header.jsp"%>
<span id="total"></span>
<table class="table table-hover mb-0">
	<thead>
		<tr>
			<th scope="col" id="alignNo" style="cursor:pointer;">#<span id="showsortNo"></span></th>
			<th scope="col" id="alignCategory" style="cursor:pointer;">분류<span id="showsortCategory"></span></th>
			<th scope="col" id="alignTitle" style="cursor:pointer;">제목<span id="showsortTitle"></span></th>
			<th scope="col" id="alignWriter" style="cursor:pointer;">작성자<span id="showsortWriter"></span></th>
			<th scope="col" id="alignDate" style="cursor:pointer;">등록일<span id="showsortDate"></span></th>
		</tr>
	</thead>
		<c:forEach var="n" items="${notices }">
			<tr style="background-color: lightyellow;">
				<th scope="row">${n.board_no }</th>
				<td>
					<span><b>공지</b></span>
				</td>
				<td class="text-left">
					<a href="/board/boardRead?board_no=${n.board_no }&page=${empty param.page ? 1 : param.page}" style="color:red;">
						${n.board_title }
					</a>			
				</td>
				<td>${n.mem_name }</td>
				<td>${n.board_date}</td>
			</tr>
		</c:forEach>
	<tbody id="tt">
	</tbody>
</table>
<hr style="margin:0">
<nav class="bg-light navbar">
	<div><!-- nav 정렬용 div --></div>
	<!-- 페이징 -->
	<ul class="pagination nav justify-content-center" id="paging">
	</ul>
	<!-- /페이징 -->
	<sec:authorize access="isAuthenticated()">
		<a class="btn btn-primary btn-sm btn-insert justify-content-end" href="#" role="button">등록</a>
	</sec:authorize>
</nav>
<script>
var page = '${page}';
var tbody = $('#tt');
//테이블 정렬
//$(function() {
	var ctg = '${category}';
	var cri = '${criteria}';
	var kwd = '${keyword}';
	$(".btn-insert").on("click", function() {
		location.href = "/board/boardWrite";
	});
	
	$("body").find("td").css("padding",".5rem");
	$("body").find("th").css("padding",".5rem");
	
	var sort = "";
	var order = "";
	var clickcount = 0;
	var clickcountTitle = 0;
	var clickcountNo = 0;
	var clickcountCategory = 0;
	var clickcountWriter = 0;
	var clickcountDate = 0;
	
	$("#alignTitle").on("click",function(){
		clickcountTitle = sorting(clickcountTitle,'board_title',$("#showsortTitle"));
	});
	$("#alignNo").on("click",function(){
		clickcountNo = sorting(clickcountNo,'board_no',$("#showsortNo"));
	});
	$("#alignCategory").on("click",function(){
		clickcountCategory = sorting(clickcountCategory,'category',$("#showsortCategory"));
	});
	$("#alignWriter").on("click",function(){
		clickcountWriter = sorting(clickcountWriter,'mem_name',$("#showsortWriter"));
		console.log("cc" + clickcountWriter);
	});
	$("#alignDate").on("click",function(){
		clickcountDate = sorting(clickcountDate,'board_date',$("#showsortDate"));
	});
	
	function sorting(clickcount, sortby, showhow){
		console.log("clickcount : " + clickcount);
		$("thead th span").html("");
		switch(clickcount){
		case 0 : 
			sort = sortby;
			order = 'asc';
			$(showhow).html("↑");
			clickcount = settingCount(sort,1);
			pagination(page,ctg,cri,kwd,sort,order);
			console.log("clickcount0 : " + clickcount);
			break;
		case 1 : 
			sort = sortby;
			order = 'desc';
			$(showhow).html("↓");
			clickcount = settingCount(sort,2);
			pagination(page,ctg,cri,kwd,sort,order);
			break;
		case 2 : 
			sort = '';
			order = '';
			$(showhow).html("");
			clickcount = settingCount(sort,0);
			pagination(page,ctg,cri,kwd,sort,order);
			break;
		}
		return clickcount;
	}
	
	function settingCount(sort, set){
		var count = 0;
		switch (sort) {
		case 'board_title' :
			clickcountTitle = set;
			count = clickcountTitle;
			clickcountNo = 0;
			clickcountCategory = 0;
			clickcountWriter = 0;
			clickcountDate = 0;
			break;
		case 'board_no' :
			clickcountNo = set;
			count = clickcountNo;
			clickcountTitle = 0;
			clickcountCategory = 0;
			clickcountWriter = 0;
			clickcountDate = 0;
			break;
		case 'category' :
			clickcountCategory = set;
			count = clickcountCategory;
			clickcountTitle = 0;
			clickcountNo = 0;
			clickcountWriter = 0;
			clickcountDate = 0;
			break;
		case 'mem_name' :
			clickcountWriter = set;
			count = clickcountWriter;
			clickcountTitle = 0;
			clickcountNo = 0;
			clickcountCategory = 0;
			clickcountDate = 0;
			break;
		case 'board_date' :
			clickcountDate = set;
			count = clickcountDate;
			clickcountTitle = 0;
			clickcountNo = 0;
			clickcountCategory = 0;
			clickcountWriter = 0;
			break;
		}
		return count;
	}
	
	
	pagination(page,ctg,cri,kwd);
//});
</script>
</body>
</html>