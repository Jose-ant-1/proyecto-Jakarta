<%@ page import="org.joseAntonio.model.Producto" %>
<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">

    <%
        // 1. Obtener el objeto Producto del request (Cargado por ProductosServlet)
        Producto producto = (Producto)request.getAttribute("producto");
        String contextPath = request.getContextPath();

        if (producto != null) {
    %>

    <div class="row mb-4 align-items-center">
        <div class="col-md-8">
            <%-- Usamos el nombre del producto en el encabezado --%>
            <h1 class="display-5"><%= producto.getNombre() %></h1>
        </div>
        <div class="col-md-4 text-end">
            <%-- Botón Volver al Listado --%>
            <a href="<%=contextPath%>/tienda/productos" class="btn btn-secondary">
                Volver al Listado
            </a>
        </div>
    </div>

    <hr class="my-4"/>

    <%-- Estructura de Detalle (Imagen a la izquierda, Info a la derecha) --%>
    <div class="row">

        <%-- COLUMNA IZQUIERDA: IMAGEN --%>
        <div class="col-md-6 mb-4">
            <div class="card shadow-lg">
                <%
                    String imagenUrl = producto.getImagenUrl();
                    if (imagenUrl != null && !imagenUrl.isEmpty()) {
                %>
                <%-- Mostrar la imagen con un estilo que la contenga --%>
                <img src="<%= imagenUrl %>" class="card-img-top"
                     alt="Imagen de <%= producto.getNombre() %>"
                     style="max-height: 450px; object-fit: contain; padding: 20px;">
                <% } else { %>
                <%-- Placeholder si no hay imagen --%>
                <div class="card-img-top bg-light d-flex align-items-center justify-content-center" style="height: 450px;">
                    <span class="text-muted fs-4">No hay imagen disponible</span>
                </div>
                <% } %>
            </div>
        </div>

        <%-- COLUMNA DERECHA: INFORMACIÓN --%>
        <div class="col-md-6">
            <div class="card p-4 shadow-sm h-100">

                <h2 class="mb-4">Informacion del Producto</h2>

                <%-- Precio (Destacado) --%>
                <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
                    <span class="fs-4 fw-bold">Precio:</span>
                    <span class="fs-2 text-success fw-bold"><%= String.format("%.2f euros", producto.getPrecio()) %></span>
                </div>

                <%-- Nombre --%>
                <div class="row mb-4">
                    <div class="col-4 fw-bold">Nombre:</div>
                    <div class="col-8"><%= producto.getNombre() %></div>
                </div>

                <hr/>

                <%-- NOTA: Se eliminan los botones de acción (Editar y Eliminar) --%>

            </div>
        </div>

    </div>

    <%
    } else {
    %>
    <div class="alert alert-danger mt-4" role="alert">
        <h4 class="alert-heading">Producto No Encontrado</h4>
        <p>Lo sentimos, no pudimos encontrar el producto solicitado con ese ID.</p>
        <hr>
        <a href="<%=contextPath%>/tienda/productos" class="btn btn-info">Volver al Listado de Productos</a>
    </div>
    <%
        }
    %>
</div>

<%@ include file ="/WEB-INF/jsp/fragmentos/footer.jspf"%>