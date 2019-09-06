<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="org.springframework.security.core.Authentication"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta id="csrf" name="${_csrf.headerName}" content="${_csrf.token}">
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<script src="/resources/jquery-3.3.1.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
<script src="/resources/fileControl.js"></script>
<script src="/resources/boardFunction.js"></script>
<title>Board Assignment</title>
</head>
<body class="text-center" >
	<nav class="navbar navbar-light bg-light">
		<a class="navbar-brand" href="/board/boardList">게시판 리스트</a>
		<c:if test="${isList == true }">
			<form class="form-inline my-2 my-lg-0" action="/board/boardList" id="searching">
				<select name="category" class="custom-select" id="category-select-search">
					<option value="all">전체</option>
					<option value="notice">공지</option>
					<option value="ask">질문</option>
					<option value="board">자유</option>
					<option value="answer">답글</option>
				</select>
				<select name="criteria" class="custom-select" id="criteria-select">
					<option value="title">제목</option>
					<option value="content">내용</option>
					<option value="writer">작성자</option>
					<option value="tc">제목+내용</option>
					<option value="all">제목+내용+작성자</option>
				</select>
				<input class="form-control mr-sm-2" name="keyword" id="keyword" type="search" placeholder="Search" aria-label="Search">
				<%-- <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" /> --%>
				<button class="btn btn-outline-success my-2 my-sm-0" id="searchBtn" type="submit">Search</button>
		    </form>
	    </c:if>
	    <form action="/logout" method="POST">
	        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	        <sec:authorize access="hasRole('ROLE_USER')" var="user">[일반]</sec:authorize>
	        <sec:authorize access="hasRole('ROLE_ADMIN')" var="admin">[관리자]</sec:authorize>
			<sec:authentication property="principal.mem_name" var="member_name"/>${member_name } 님 
			<sec:authentication property='principal.mem_no' var="member_no"/>
			<sec:authentication property='principal.username' var="member_id"/>
	        <button class="btn btn-outline-success my-2 my-sm-0" type="submit">LOGOUT</button>
	    </form>
	</nav>
	<script>
		$(function(){
			// 검색바
			var searchResult = '${searchResult}';
			var ctg = '${category}';
			var cri = '${criteria}';
			var kwd = '${keyword}';
			
			if(ctg!="" && cri!=""){
				$("#category-select-search").val(ctg);
				$("#criteria-select").val(cri);
			}
			$("#keyword").val(kwd);
			
			$("#searchBtn").on("click",function(e){
				e.preventDefault;
				$("#searching").append("<input type='hidden' name='page' value='${page}'/>");
				$("#searching").submit();
			});
			
		});
	</script>
