<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>
<%-- Ya NO NECESITA enctype="multipart/form-data" --%>
<div class="container mt-5" style="width: 50%;">
    <h1 class="mb-4">Crear Nuevo Producto</h1>
    <hr/>

    <form action="${pageContext.request.contextPath}/tienda/productos/" method="post">

        <div class="mb-3 row">
            <label for="nombre" class="col-sm-3 col-form-label">Nombre:</label>
            <div class="col-sm-9">
                <input type="text" class="form-control" id="nombre" name="nombre" required>
            </div>
        </div>

        <div class="mb-3 row">
            <label for="precio" class="col-sm-3 col-form-label">Precio (€):</label>
            <div class="col-sm-9">
                <input type="number" step="0.01" class="form-control" id="precio" name="precio" required min="0.01">
            </div>
        </div>

        <div class="mb-3 row">
            <label for="imagenUrl" class="col-sm-3 col-form-label">URL Imagen:</label>
            <div class="col-sm-9">
                <input type="url" class="form-control" id="imagenUrl" name="imagenUrl" placeholder="https://ejemplo.com/imagen.jpg" required>
            </div>
        </div>

        <hr class="my-4"/>

        <div class="row">
            <div class="col-sm-12 text-end">
                <button type="submit" class="btn btn-success me-2">Guardar Producto</button>
                <a href="${pageContext.request.contextPath}/tienda/productos" class="btn btn-secondary">Cancelar</a>
            </div>
        </div>
    </form>
</div>

<%@ include file ="/WEB-INF/jsp/fragmentos/footer.jspf"%>