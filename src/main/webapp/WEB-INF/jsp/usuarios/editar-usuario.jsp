<%@ page import="org.joseAntonio.model.Usuario" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">

    <%
        // Recuperar el objeto Usuario del request
        Usuario usuario = (Usuario) request.getAttribute("usuario");

        if (usuario != null) {
    %>
    <div class="row mb-4">
        <div class="col-12">
            <h1>Editar Usuario: <%= usuario.getNombre() %></h1>
        </div>
    </div>

    <div class="card shadow-lg">
        <div class="card-header bg-success text-white">
            <h5 class="mb-0">Modificar datos del usuario con ID: <%= usuario.getId() %></h5>
        </div>
        <div class="card-body">

            <form action="${pageContext.request.contextPath}/tienda/usuarios/editar/<%= usuario.getId() %>" method="post" class="needs-validation" novalidate>

                <input type="hidden" name="id" value="<%= usuario.getId() %>">

                <input type="hidden" name="__method__" value="put">

                <div class="mb-3">
                    <label for="nombre" class="form-label fw-bold">Nombre de Usuario</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" value="<%= usuario.getNombre() %>" required>
                    <div class="invalid-feedback">
                        Por favor, ingrese un nombre de usuario.
                    </div>
                </div>

                <div class="mb-3">
                    <label for="rol" class="form-label fw-bold">Rol</label>
                    <select class="form-select" id="rol" name="rol" required>
                        <option value="Administrador" <%= "Administrador".equals(usuario.getRol()) ? "selected" : "" %>>Administrador</option>
                        <option value="Usuario" <%= "Usuario".equals(usuario.getRol()) ? "selected" : "" %>>Usuario</option>
                    </select>
                    <div class="invalid-feedback">
                        Por favor, seleccione un rol.
                    </div>
                </div>

                <hr class="my-4">

                <div class="mb-3">
                    <label for="contrasenia" class="form-label fw-bold">Nueva Contraseña (Dejar vacío para no cambiar)</label>
                    <input type="password" class="form-control" id="contrasenia" name="contrasenia" placeholder="Ingrese una nueva contraseña si desea cambiarla">
                    <div class="form-text">Si deja este campo vacío, la contraseña actual no se modificará.</div>
                </div>

                <hr class="my-4">

                <div class="d-flex justify-content-between">
                    <button type="submit" class="btn btn-warning">Guardar Cambios</button>

                    <a href="${pageContext.request.contextPath}/tienda/usuarios/" class="btn btn-secondary">Cancelar</a>
                </div>

            </form>

        </div>
    </div>

    <% } else { %>

    <div class="alert alert-danger" role="alert">
        El usuario solicitado para editar no fue encontrado.
    </div>

    <div class="d-flex justify-content-end">
        <a href="${pageContext.request.contextPath}/tienda/usuarios/" class="btn btn-secondary">
            Volver al Listado
        </a>
    </div>

    <% } %>

</div>

<%@ include file ="/WEB-INF/jsp/fragmentos/footer.jspf"%>