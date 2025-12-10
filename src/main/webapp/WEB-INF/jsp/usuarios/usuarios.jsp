<%@ page import="java.util.List" %>
<%@ page import="org.joseAntonio.model.Usuario" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>

<div class="container mt-5 mb-5">

    <div class="row align-items-center mb-3">
        <div class="col-md-6">
            <h1 class="mb-0">Gestión de Usuarios</h1>
        </div>
        <div class="col-md-6 text-end">
            <form action="${pageContext.request.contextPath}/tienda/usuarios/crear" method="get" class="d-inline">
                <button type="submit" class="btn btn-success">
                    Crear Nuevo Usuario
                </button>
            </form>
        </div>
    </div>

    <hr class="my-4"/>

    <%
        // Obtener la lista de usuarios del request
        List<Usuario> listaUsuarios = (List<Usuario>)request.getAttribute("listaUsuarios");

        // Verificar si la lista no es nula y contiene elementos
        if (listaUsuarios != null && !listaUsuarios.isEmpty()) {
    %>

    <table class="table table-striped table-hover shadow-sm">
        <thead class="table-dark">
        <tr>
            <th scope="col" style="width: 10%;">ID</th>
            <th scope="col" style="width: 30%;">Nombre</th>
            <th scope="col" style="width: 20%;">Rol</th>
            <th scope="col" style="width: 40%;">Acciones</th>
        </tr>
        </thead>
        <tbody>
        <%
            // Iterar sobre la lista de usuarios
            for (Usuario usuario: listaUsuarios) {
                String contextPath = request.getContextPath();
        %>
        <tr>
            <td><%=usuario.getId()%></td>
            <td><%=usuario.getNombre()%></td>
            <td><%=usuario.getRol()%></td>
            <td>
                <div style="display: flex; flex-direction: row; gap: 5px;">

                    <form action="<%=contextPath%>/tienda/usuarios/<%=usuario.getId()%>" method="get">
                        <button type="submit" class="btn btn-primary btn-sm">
                            Ver Detalle
                        </button>
                    </form>

                    <form action="<%=contextPath%>/tienda/usuarios/editar/<%=usuario.getId()%>" method="get">
                        <button type="submit" class="btn btn-warning btn-sm">
                            Editar
                        </button>
                    </form>

                    <form action="<%=contextPath%>/tienda/usuarios/eliminar/<%=usuario.getId()%>" method="post">
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
        No se encontraron usuarios registrados.
    </div>
    <%
        }
    %>
</div>

<%@ include file ="/WEB-INF/jsp/fragmentos/footer.jspf"%>