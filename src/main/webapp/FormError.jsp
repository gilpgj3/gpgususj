<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="titulo" scope="request" value="Error" />
<html>
<head>
<meta charset="UTF-8">
<title>${titulo}</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<link rel="stylesheet" href="css/colores.css">
<link rel="stylesheet" href="css/estilos.css">
</head>
<body class="vista">
<header>
	<h1>${titulo}</h1>
	</header>
	<div class="principal">
		<p><c:out value="${requestScope.error}"/></p>
	<p>
		<button type="button" onclick="history.back()">Aceptar</button>
	</p>
	</div>
	<%@include file="/WEB-INF/jspf/mi-footer.jspf"%>
</body>
</html>