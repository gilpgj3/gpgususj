<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="título" scope="request" value="Usuarios" />
<c:url var="urlAgregar" value="CtrlUsuarioBusca" />
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
<body>
	<form class="vista" action="CtrlUsuarios">
		<%@include file="/WEB-INF/jspf/herramientas-maestras.jspf"%>
		<ol class="principal">
			<c:forEach var="it" items="${requestScope.lista}">
				<li><c:url var="url" value="CtrlUsuarioBusca">
						<c:param name="id" value="${it.id}" />
					</c:url>
					<figure>
						<img src="${it.imagen}=s300" alt="${it.cue}">
					</figure>
					<div>
						<a href="${url}"><c:out value="${it.cue}" /></a> <br>
						<c:out value="${it.pasatiempo}" />
						<br>
						<c:out value="${it.roles}" />
					</div></li>
			</c:forEach>
		</ol>
		<%@include file="/WEB-INF/jspf/mi-footer.jspf"%>
		<%@include file="/WEB-INF/jspf/mi-navegacion.jspf"%>
	</form>
</body>
</html>
