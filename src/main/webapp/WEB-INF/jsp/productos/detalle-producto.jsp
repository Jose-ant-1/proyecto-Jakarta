<%@ page import="org.joseAntonio.model.Producto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">

    <%
        Producto producto = (Producto)request.getAttribute("producto");
        String contextPath = request.getContextPath();

        if (producto != null) {
    %>

    <div class="row mb-4 align-items-center">
        <div class="col-md-8">
            <h1 class="display-5"><%= producto.getNombre() %></h1>
        </div>
        <div class="col-md-4 text-end">
            <a href="<%=contextPath%>/tienda/productos" class="btn btn-secondary">
                Volver al Listado
            </a>
        </div>
    </div>

    <hr class="my-4"/>

    <div class="row">

        <%-- COLUMNA IZQUIERDA: IMAGEN --%>
        <div class="col-md-6 mb-4">
            <div class="card shadow-sm h-100">
                <% if (producto.getImagenUrl() != null && !producto.getImagenUrl().isEmpty()) { %>
                <img src="<%= producto.getImagenUrl() %>" class="card-img-top img-fluid rounded" alt="<%= producto.getNombre() %>">
                <% } else { %>
                <div class="p-5 text-center bg-light">
                    <i class="bi bi-image-fill display-1 text-muted"></i>
                    <p class="mt-2">Imagen no disponible</p>
                </div>
                <% } %>
            </div>
        </div>

        <%-- COLUMNA DERECHA: INFORMACIÓN Y FORMULARIO DE COMPRA --%>
        <div class="col-md-6">
            <div class="card p-4 shadow-sm h-100">

                <h2 class="mb-4">Información del Producto</h2>

                <div class="d-flex justify-content-between align-items-center mb-4 border-bottom pb-3">
                    <span class="fs-4 fw-bold">Precio:</span>
                    <span class="fs-2 text-success fw-bold"><%= String.format("%.2f euros", producto.getPrecio()) %></span>
                </div>

                <div class="row mb-4">
                    <div class="col-4 fw-bold">Nombre:</div>
                    <div class="col-8"><%= producto.getNombre() %></div>
                </div>

                <hr/>

                <form action="<%=contextPath%>/tienda/carrito/add" method="post" class="mt-4">


                    <input type="hidden" name="productoId" value="<%= producto.getId() %>" />

                    <div class="row g-3 align-items-center">
                        <div class="col-auto">
                            <label for="cantidad" class="col-form-label fw-bold">Cantidad:</label>
                        </div>
                        <div class="col-auto">
                            <input type="number" id="cantidad" name="cantidad"
                                   value="1" min="1" class="form-control" style="width: 100px;" required>
                        </div>
                        <div class="col-auto">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="bi bi-cart-plus-fill"></i> Añadir al Carrito
                            </button>
                        </div>
                    </div>
                </form>

                <hr class="my-4"/>

                <%-- MENSAJE ÉXITO/ERROR DEL CARRITO --%>

                <%
                    String mensajeCarrito = (String) session.getAttribute("mensajeCarrito");
                    if (mensajeCarrito != null) {
                        session.removeAttribute("mensajeCarrito");
                %>
                <div class="alert alert-success mt-3" role="alert">
                    <i class="bi bi-check-circle-fill me-2"></i> <%= mensajeCarrito %>
                </div>
                <%
                    }
                %>


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
        <a href="<%=contextPath%>/tienda/productos" class="btn btn-danger">
            Volver al Listado de Productos
        </a>
    </div>
    <%
        }
    %>
</div>

<%@ include file ="/WEB-INF/jsp/fragmentos/footer.jspf"%>