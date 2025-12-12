<%@ page import="java.util.List" %>
<%@ page import="org.joseAntonio.model.Pedido" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">
    <h1 class="mb-4">Mis Pedidos</h1>
    <hr/>

    <a href="<%= request.getContextPath() %>/tienda/productos" class="btn btn-secondary mb-4">
        ← Volver a la Tienda
    </a>

    <%
        // Obtener la lista de pedidos del request
        List<Pedido> listaPedidos = (List<Pedido>) request.getAttribute("listaPedidos");
    %>

    <% if (listaPedidos != null && !listaPedidos.isEmpty()) { %>
    <table class="table table-striped table-hover shadow">
        <thead class="table-dark">
        <tr>
            <th># Pedido</th>
            <th>Importe Total</th>
            <th>Acciones</th>
        </tr>
        </thead>
        <tbody>
        <% for (Pedido pedido : listaPedidos) { %>
        <tr>
            <td><%= pedido.getId() %></td>
            <td><strong><%= String.format("%.2f €", pedido.getImporte()) %></strong></td>
            <td>
                <a href="<%= request.getContextPath() %>/tienda/pedidos/<%= pedido.getId() %>"
                   class="btn btn-info btn-sm text-white">
                    Ver Detalles
                </a>
            </td>
        </tr>
        <% } %>
        </tbody>
    </table>
    <% } else { %>
    <div class="alert alert-info" role="alert">
        Aún no has realizado ningún pedido
    </div>
    <% } %>

</div>

<%@ include file="/WEB-INF/jsp/fragmentos/footer.jspf" %>