<%@ page import="java.util.List" %>
<%@ page import="org.joseAntonio.model.Producto" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">

    <div class="row align-items-center mb-3">
        <div class="col-md-6">
            <h1 class="mb-0">Listado de Productos</h1>
        </div>
        <div class="col-md-6 text-end">
            <form action="${pageContext.request.contextPath}/tienda/productos/crear" method="get" class="d-inline">
                <button type="submit" class="btn btn-success">
                    Crear Nuevo Producto
                </button>
            </form>
        </div>
    </div>

    <hr class="my-4"/>

    <%
        // Obtener la lista de productos del request (cargada por ProductosServlet.doGet)
        List<Producto> listaProductos = (List<Producto>)request.getAttribute("listaProductos");
        String contextPath = request.getContextPath(); // Definir contextPath una vez

        // Verificar si la lista no es nula y contiene elementos
        if (listaProductos != null && !listaProductos.isEmpty()) {
    %>

    <%-- CUADRÍCULA DE PRODUCTOS (Usando clases de Bootstrap: row y col) --%>
    <div class="row row-cols-1 row-cols-md-3 g-4">
        <%
            // Iterar sobre la lista de productos
            for (Producto producto: listaProductos) {
        %>

        <%-- Inicio de la Tarjeta/Cuadro para cada producto. Ocupa 4 columnas en desktop (3 por fila) --%>
        <div class="col">
            <div class="card h-100 shadow-sm">

                <%-- 1. Zona de Imagen --%>
                <%
                    String imagenUrl = producto.getImagenUrl();
                    if (imagenUrl != null && !imagenUrl.isEmpty()) {
                %>
                <img src="<%= imagenUrl %>" class="card-img-top"
                     alt="<%= producto.getNombre() %>"
                     style="height: 200px; object-fit: contain; padding: 10px;">
                <% } else { %>
                <div class="card-img-top bg-light d-flex align-items-center justify-content-center" style="height: 200px;">
                    <span class="text-muted">No Image Available</span>
                </div>
                <% } %>

                <div class="card-body">
                    <%-- 2. Nombre y ID --%>
                    <h5 class="card-title"><%= producto.getNombre()%></h5>

                    <%-- 3. Precio --%>
                    <h4 class="text-primary mb-3"><%=String.format("%.2f €", producto.getPrecio())%></h4>
                </div>

                <%-- 4. Botones de Acción --%>
                <div class="card-footer bg-light">
                    <div class="d-flex justify-content-between align-items-center">

                        <%-- Botón de Ver Detalle --%>
                        <form action="<%=contextPath%>/tienda/productos/<%=producto.getId()%>" method="get" class="me-1">
                            <button type="submit" class="btn btn-primary btn-sm">
                                Ver Detalle
                            </button>
                        </form>

                        <div class="btn-group">
                            <%-- Botón de Editar --%>
                            <form action="<%=contextPath%>/tienda/productos/editar/<%=producto.getId()%>" method="get" class="me-1">
                                <button type="submit" class="btn btn-warning btn-sm">
                                    Editar
                                </button>
                            </form>

                            <%-- Botón de Eliminar (Confirmación removida) --%>
                            <form action="<%=contextPath%>/tienda/productos/eliminar/<%=producto.getId()%>" method="post">
                                <button type="submit" class="btn btn-danger btn-sm">
                                    Eliminar
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <%
            } // Fin del bucle for
        %>
    </div>
    <%-- Fin de la Cuadrícula --%>

    <%
    } else {
    %>
    <div class="alert alert-info mt-4" role="alert">
        No se encontraron productos registrados.
    </div>
    <%
        }
    %>
</div>

<%@ include file ="/WEB-INF/jsp/fragmentos/footer.jspf"%>