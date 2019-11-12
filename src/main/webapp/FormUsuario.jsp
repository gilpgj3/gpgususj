<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%><!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="modelo" scope="request" value="${requestScope.modelo}" />
<c:set var="título" scope="request"
	value="${empty param.id ? 'Usuario Nuevo' : modelo.cue}" />
<c:url var="urlCancelar" value="CtrlUsuarios" />
<c:url var="urlEliminar" value="CtrlUsuarioElimina" />
<c:url var="urlImagen" value="${modelo.imagen}" />
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
	<form class="vista" action="CtrlUsuarioGuarda" method="post"
		enctype="multipart/form-data">
		<%@include file="/WEB-INF/jspf/herramientas-detalle.jspf"%>
		<div class="principal">
			<c:if test="${!empty param.id}">
				<figure>
					<img src="${fn:escapeXml(urlImagen)}"
						alt="${fn:escapeXml(modelo.cue)}">
				</figure>
			</c:if>
			<fieldset>
				<legend>Cue (email)</legend>
				<input name="cue" type="email" required
					value="${fn:escapeXml(modelo.cue)}">
			</fieldset>
			<fieldset>
				<legend>Imagen</legend>
				<input name="imagen" type="file" ${empty param.id ? 'required' : '' }
					accept=".png,.jpg,.jpeg,.gif">
			</fieldset>
			<fieldset>
				<legend>Pasatiempo</legend>
				<select name="pasatiempo">
					<c:forEach var="it" items="${requestScope.pasatiempos}">
						<%@include file="/WEB-INF/jspf/option.jspf"%>
					</c:forEach>
				</select>
			</fieldset>
			<fieldset>
				<legend>Roles</legend>
				<select name="roles" multiple>
					<c:forEach var="it" items="${requestScope.roles}">
						<%@include file="/WEB-INF/jspf/option.jspf"%>
					</c:forEach>
				</select>
			</fieldset>
		</div>
		<%@include file="/WEB-INF/jspf/mi-footer.jspf"%>
		<%@include file="/WEB-INF/jspf/mi-navegacion.jspf"%>
	</form>
</body>
</html>
