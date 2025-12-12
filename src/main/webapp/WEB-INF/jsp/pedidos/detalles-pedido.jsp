<%@ page import="java.util.List" %>
<%@ page import="org.joseAntonio.model.Pedido" %>
<%@ page import="org.joseAntonio.model.DetallePedidos" %>
<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">

    <h1 class="mb-4">Detalles del Pedido</h1>
    <hr/>

    <a href="<%= request.getContextPath() %>/tienda/pedidos" class="btn btn-secondary mb-4">
        Volver a Mis Pedidos
    </a>

    <%
        Pedido pedido = (Pedido) request.getAttribute("pedido");
        List<DetallePedidos> detalles = (List<DetallePedidos>) request.getAttribute("detalles");
    %>

    <h4>Pedido <%= pedido.getId() %></h4>
    <p>Realizado por el Usuario ID: <strong><%= pedido.getUsuarioId() %></strong></p>
    <h5 class="mb-4">Total: <%= String.format("%.2f euros", pedido.getImporte()) %></h5>

    <table class="table table-striped table-hover shadow">
        <thead class="table-dark">
        <tr>
            <th>ID Producto</th>
            <th>Cantidad</th>
            <th>Precio Unitario</th>
            <th>Subtotal</th>
        </tr>
        </thead>
        <tbody>
        <% for (DetallePedidos d : detalles) { %>
        <tr>
            <td><%= d.getProductoId() %></td>
            <td><%= d.getCantidad() %></td>
            <td><%= String.format("%.2f euros", d.getPrecio()) %></td>
            <td><%= String.format("%.2f euros", d.getCantidad() * d.getPrecio()) %></td>
        </tr>
        <% } %>
        </tbody>
    </table>

</div>

<%@ include file="/WEB-INF/jsp/fragmentos/footer.jspf" %>
