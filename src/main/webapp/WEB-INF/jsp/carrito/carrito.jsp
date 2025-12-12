<%@ page import="org.joseAntonio.model.Producto" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.LinkedHashMap" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">
    <h1 class="mb-4">Carrito de la Compra</h1>
    <hr/>

    <%
        // Obtener ContextPath para las acciones de formulario
        String contextPath = request.getContextPath();

        // Obtener el carrito de la sesión
        Map<Producto, Integer> carrito = (Map<Producto, Integer>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new LinkedHashMap<>();
        }

        double totalGeneral = 0.0;
    %>

    <%-- Mensajes de Notificación y Error (AÑADIDO errorCarrito) --%>
    <%
        String mensajeCarrito = (String) session.getAttribute("mensajeCarrito");
        String errorCarrito = (String) session.getAttribute("errorCarrito");

        if (mensajeCarrito != null) {
            session.removeAttribute("mensajeCarrito");
    %>
    <div class="alert alert-success mt-3" role="alert">
        <i class="bi bi-info-circle-fill me-2"></i> <%= mensajeCarrito %>
    </div>
    <%
    } else if (errorCarrito != null) { // <-- Mostrar el error de autorización
        session.removeAttribute("errorCarrito");
    %>
    <div class="alert alert-warning mt-3" role="alert">
        <i class="bi bi-exclamation-triangle-fill me-2"></i> <%= errorCarrito %>
    </div>
    <%
        }
    %>

    <% if (carrito.isEmpty()) { %>
    <div class="alert alert-info" role="alert">
        Tu carrito está vacío
    </div>
    <p>
        <a href="<%=contextPath%>/tienda/productos" class="btn btn-primary">
            Ver Productos
        </a>
    </p>
    <% } else { %>

    <table class="table table-hover align-middle">
        <thead class="table-dark">
        <tr>
            <th>Producto</th>
            <th>Precio Unitario</th>
            <th>Cantidad</th>
            <th>Subtotal</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <%
            for (Map.Entry<Producto, Integer> entry : carrito.entrySet()) {
                Producto producto = entry.getKey();
                int cantidad = entry.getValue();
                double subtotal = producto.getPrecio() * cantidad;
                totalGeneral += subtotal;
        %>
        <tr>
            <td>
                <a href="<%=contextPath%>/tienda/productos/<%= producto.getId() %>">
                    <%= producto.getNombre() %>
                </a>
            </td>
            <td><%= String.format("%.2f euros", producto.getPrecio()) %></td>
            <td><%= cantidad %></td>
            <td><strong><%= String.format("%.2f euros", subtotal) %></strong></td>
            <td>
                <form action="<%=contextPath%>/tienda/carrito/remove" method="post" class="d-inline">
                    <input type="hidden" name="productoId" value="<%= producto.getId() %>">
                    <button type="submit" class="btn btn-danger btn-sm">
                        <i class="bi bi-trash-fill"></i> Eliminar
                    </button>
                </form>
            </td>
        </tr>
        <% } %>
        </tbody>
        <tfoot>
        <tr class="table-light">
            <td colspan="3" class="text-end"><h4>Total a Pagar:</h4></td>
            <td colspan="2"><h4><%= String.format("%.2f euros", totalGeneral) %></h4></td>
        </tr>
        </tfoot>
    </table>

    <div class="d-flex justify-content-end mt-4">
        <a href="<%=contextPath%>/tienda/productos" class="btn btn-secondary me-2">
            Seguir Comprando
        </a>

        <% if (totalGeneral > 0) { %>
        <form action="<%=contextPath%>/tienda/carrito/proceder-pago" method="post" class="d-inline">
            <button type="submit" class="btn btn-success btn-lg">
                Proceder al Pago (<%= String.format("%.2f euros", totalGeneral) %>)
            </button>
        </form>
        <% } %>
    </div>

    <% } %>
</div>

<%@ include file ="/WEB-INF/jsp/fragmentos/footer.jspf"%>