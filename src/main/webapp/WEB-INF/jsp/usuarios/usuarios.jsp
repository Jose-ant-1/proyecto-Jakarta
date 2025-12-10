<%@ page import="java.util.List" %>
<%@ page import="org.joseAntonio.model.Usuario" %><%--
  Created by IntelliJ IDEA.
  User: josea
  Date: 22/11/2025
  Time: 17:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Usuarios</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/tienda/usuarios/crear" method="get">
    <input type="submit" value="crear">
</form>
<%
    if (request.getAttribute("listaUsuarios") != null) {
        List<Usuario> listaUsuarios = (List<Usuario>)request.getAttribute("listaUsuarios");

        for (Usuario usuario: listaUsuarios) {

%>
<div><%=usuario.getId()%></div>
<div><%=usuario.getNombre()%></div>
<div><%=usuario.getRol()%></div>
<%
        }
    } else {

%>
<p>No se encontró el usuario solicidato</p>
<%}%>
</body>
</html>
