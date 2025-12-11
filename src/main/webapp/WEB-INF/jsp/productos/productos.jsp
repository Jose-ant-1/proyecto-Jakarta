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

        // Verificar si la lista no es nula y contiene elementos
        if (listaProductos != null && !listaProductos.isEmpty()) {
    %>

    <table class="table table-striped table-hover shadow-sm">
        <thead class="table-dark">
        <tr>
            <th scope="col" style="width: 10%;">ID</th>
            <th scope="col" style="width: 40%;">Nombre</th>
            <th scope="col" style="width: 20%;">Precio (€)</th>
            <th scope="col" style="width: auto;">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <%
            // Iterar sobre la lista de productos
            for (Producto producto: listaProductos) {
                String contextPath = request.getContextPath();
        %>
        <tr>
            <td><%=producto.getId()%></td>
            <td><%=producto.getNombre()%></td>
            <td><%=String.format("%.2f", producto.getPrecio())%></td>
            <td>
                <div style="display: flex; flex-direction: row; gap: 5px;">

                    <form action="<%=contextPath%>/tienda/productos/<%=producto.getId()%>" method="get">
                        <button type="submit" class="btn btn-primary btn-sm">
                            Ver
                        </button>
                    </form>

                    <form action="<%=contextPath%>/tienda/productos/editar/<%=producto.getId()%>" method="get">
                        <button type="submit" class="btn btn-warning btn-sm">
                            Editar
                        </button>
                    </form>

                    <form action="<%=contextPath%>/tienda/productos/eliminar/<%=producto.getId()%>" method="post">
                        <button type="submit" class="btn btn-danger btn-sm">
                            Eliminar
                        </button>
                    </form>
                </div>
            </td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

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