<%@ page import="org.joseAntonio.model.Producto" %>
<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">

    <%
        Producto producto = (Producto) request.getAttribute("producto");
        String contextPath = request.getContextPath();

        if (producto != null) {
            String actionUrl = contextPath + "/tienda/productos/editar/" + producto.getId();

            String imagenUrl = (producto.getImagenUrl() != null) ? producto.getImagenUrl() : "";
    %>

    <h1 class="mb-4">Editar Producto: <%= producto.getNombre() %></h1>
    <hr class="my-4"/>

    <%-- Formulario de Edición --%>
    <form action="<%= actionUrl %>" method="post" class="shadow p-4 rounded bg-light">

        <%-- ID Oculto --%>
        <input type="hidden" name="id" value="<%= producto.getId() %>">

        <%-- Input para el Nombre --%>
        <div class="mb-3 row">
            <label for="nombre" class="col-sm-2 col-form-label fw-bold">Nombre</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="nombre" name="nombre"
                       value="<%= producto.getNombre() %>" required>
            </div>
        </div>

        <%-- Input para el Precio --%>
        <div class="mb-3 row">
            <label for="precio" class="col-sm-2 col-form-label fw-bold">Precio (€)</label>
            <div class="col-sm-10">
                <input type="number" step="0.01" min="0" class="form-control" id="precio" name="precio"
                       value="<%= producto.getPrecio() %>" required>
            </div>
        </div>

        <div class="mb-3 row">
            <label for="imagenUrl" class="col-sm-2 col-form-label fw-bold">URL Imagen</label>
            <div class="col-sm-10">
                <input type="url" class="form-control" id="imagenUrl" name="imagenUrl"
                       value="<%= imagenUrl %>" placeholder="URL completa de la imagen">
            </div>
        </div>

        <%-- Botones de Acción --%>
        <div class="mt-5 pt-3 border-top d-flex justify-content-end">

            <%-- Botón Cancelar --%>
            <a href="<%=contextPath%>/tienda/productos/" class="btn btn-secondary me-3">
                Cancelar
            </a>

            <%-- Botón Guardar --%>
            <button type="submit" class="btn btn-warning btn-lg">
                <i class="bi bi-pencil"></i> Guardar Cambios
            </button>
        </div>
    </form>

    <%
    } else {
    %>
    <div class="alert alert-danger mt-4" role="alert">
        <h4 class="alert-heading">Error de Edición</h4>
        <p>No se encontró el producto con el ID especificado para editar.</p>
        <hr>
        <a href="<%=contextPath%>/tienda/productos" class="btn btn-info">Volver al Listado</a>
    </div>
    <%
        }
    %>
</div>

<%@ include file ="/WEB-INF/jsp/fragmentos/footer.jspf"%>