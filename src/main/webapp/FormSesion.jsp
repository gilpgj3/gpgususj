<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="título" scope="request" value="Sesion" />
<c:url var="logoutURL" value="${requestScope.logoutURL}" />
<html>
<head>
<meta charset="UTF-8">
<title><c:out value="${título}" /></title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="https://fonts.googleapis.com/icon?family=Material+Icons"
	rel="stylesheet">
<link rel="stylesheet" href="css/colores.css">
<link rel="stylesheet" href="css/estilos.css">
</head>
<body class="vista">
	<header>
		<div class="herramientas">
			<span class="divisor"></span> <a href="${fn:escapeXml(logoutURL)}">Terminar
				Sesión</a>
		</div>
		<h1>
			<c:out value="${título}" />
		</h1>
	</header>
	<div class="principal">
		<fieldset>
			<legend>Cue</legend>
			<c:out value="${pageContext.request.userPrincipal.name}" />
		</fieldset>
		<figure>
			<img src="${fn:escapeXml(requestScope.imagen) }"
				alt="${fn:escapeXml(pageContext.request.userPrincipal.name) }" />
		</figure>
	</div>
	<%@include file="/WEB-INF/jspf/mi-footer.jspf"%>
	<%@include file="/WEB-INF/jspf/mi-navegacion.jspf"%>
</body>
</html>