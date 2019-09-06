<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
<title>Login</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
<script src="/resources/jquery-3.3.1.js"></script>
</head>
<body class="text-center" style="display: flex;">
	<sec:authorize access="isAnonymous()"> 	
		<form class="form-signin" action="/login" method="post" >
			<h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>
			<label for="inputId" class="sr-only">ID</label> 
			<input type="text" id="inputId" name="mem_id" class="form-control" placeholder="ID" required autofocus> 
			<label for="inputPassword" class="sr-only">Password</label>
			<input type="password" id="inputPassword" name="mem_pwd"class="form-control" placeholder="Password" required>
			<input name="${_csrf.parameterName}" type="hidden" value="${_csrf.token}"/>
			<div class="checkbox mb-3"></div>
			<button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
		</form>
	</sec:authorize>
	<sec:authorize access="isAuthenticated()"> 	
		로그인 상태입니다. <a href="/board/boardList"> 게시판목록으로</a>
	</sec:authorize>
<script>
	$(function(){
		var error = '${error}';
		console.log(error);
		if(error == 'loginfailed'){
			alert("아이디 혹은 비밀번호 오류");
			return;
		}else if(error == 'duplicated'){
			alert("다른 브라우저에서 접속 중인 계정입니다.\n해당 브라우저에서 먼저 로그아웃 해주시기 바랍니다");
			return;
		}
	});
</script>
</body>

</html>
