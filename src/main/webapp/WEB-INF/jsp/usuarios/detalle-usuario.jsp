<%@ page import="org.joseAntonio.model.Usuario" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">

    <div class="row mb-4">
        <div class="col-12">
            <h1>Detalle del Usuario</h1>
        </div>
    </div>

    <%
        // Recuperar el objeto Usuario del request
        Usuario usuario = (Usuario) request.getAttribute("usuario");

        if (usuario != null) {
    %>
    <div class="card shadow-lg">
        <div class="card-header bg-success text-white">
            <h5 class="mb-0">Información del Usuario: <%= usuario.getNombre() %></h5>
        </div>
        <div class="card-body">

            <div class="row mb-3">
                <div class="col-md-4 text-md-end">
                    <label class="form-label fw-bold">ID:</label>
                </div>
                <div class="col-md-8">
                    <input type="text" class="form-control" value="<%= usuario.getId() %>" readonly disabled>
                </div>
            </div>

            <div class="row mb-3">
                <div class="col-md-4 text-md-end">
                    <label class="form-label fw-bold">Nombre de Usuario:</label>
                </div>
                <div class="col-md-8">
                    <input type="text" class="form-control" value="<%= usuario.getNombre() %>" readonly disabled>
                </div>
            </div>

            <div class="row mb-4">
                <div class="col-md-4 text-md-end">
                    <label class="form-label fw-bold">Rol del Usuario:</label>
                </div>
                <div class="col-md-8">
                    <input type="text" class="form-control" value="<%= usuario.getRol() %>" readonly disabled>
                </div>
            </div>

            <hr class="my-4">

            <div class="d-flex justify-content-end">
                <a href="${pageContext.request.contextPath}/tienda/usuarios/" class="btn btn-secondary">
                    Volver al Listado
                </a>
            </div>

        </div>
    </div>

    <% } else { %>

    <div class="alert alert-danger" role="alert">
        El usuario solicitado no fue encontrado.
    </div>

    <div class="d-flex justify-content-end">
        <a href="${pageContext.request.contextPath}/tienda/usuarios/" class="btn btn-secondary">
            Volver al Listado
        </a>
    </div>

    <% } %>

</div>

<%@ include file ="/WEB-INF/jsp/fragmentos/footer.jspf"%>