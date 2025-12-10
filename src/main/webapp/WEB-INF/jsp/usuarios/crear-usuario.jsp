<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">

    <div class="row mb-4">
        <div class="col-12">
            <h1>Crear Nuevo Usuario</h1>
        </div>
    </div>

    <div class="card shadow-sm">
        <div class="card-header bg-success text-white">
            Ingrese los datos del nuevo usuario
        </div>
        <div class="card-body">

            <form action="${pageContext.request.contextPath}/tienda/usuarios/" method="post" class="needs-validation" novalidate>

                <div class="mb-3">
                    <label for="nombre" class="form-label">Nombre de Usuario</label>
                    <input type="text" class="form-control" id="nombre" name="nombre" required>
                    <div class="invalid-feedback">
                        Por favor, ingrese un nombre de usuario.
                    </div>
                </div>

                <div class="mb-3">
                    <label for="password" class="form-label">Contraseña</label>
                    <input type="password" class="form-control" id="password" name="password" required>
                    <div class="invalid-feedback">
                        Por favor, ingrese una contraseña.
                    </div>
                </div>

                <div class="mb-3">
                    <label for="rol" class="form-label">Rol</label>
                    <select class="form-select" id="rol" name="rol" required>
                        <option value="" disabled selected>Seleccione un rol...</option>
                        <option value="Administrador">Administrador</option>
                        <option value="Usuario">Usuario</option>
                    </select>
                    <div class="invalid-feedback">
                        Por favor, seleccione un rol.
                    </div>
                </div>

                <hr class="my-4">

                <div class="d-flex justify-content-between">
                    <button type="submit" class="btn btn-primary">Guardar Usuario</button>

                    <a href="${pageContext.request.contextPath}/tienda/usuarios/" class="btn btn-secondary">Cancelar</a>
                </div>

            </form>

        </div>
    </div>
</div>

<%@ include file ="/WEB-INF/jsp/fragmentos/footer.jspf"%>