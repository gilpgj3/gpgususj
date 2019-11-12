<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="título" scope="request" value="Inicio" />
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
		<h1>
			<c:out value="${título}" />
		</h1>
	</header>
	<div class="principal">
		<p>Bienvenido. En esta página entran todos.</p>
	</div>
	<%@include file="/WEB-INF/jspf/mi-footer.jspf"%>
	<%@include file="/WEB-INF/jspf/mi-navegacion.jspf"%>
</body>
</html>