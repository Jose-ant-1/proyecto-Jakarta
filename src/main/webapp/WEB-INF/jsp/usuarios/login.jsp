<%@ page import="org.joseAntonio.model.Usuario" %>
<%@ include file="/WEB-INF/jsp/fragmentos/header.jspf" %>
<%-- Incluimos nav.jspf si queremos que aparezca el menú --%>
<%@ include file="/WEB-INF/jsp/fragmentos/nav.jspf" %>
<%-- Nota: El footer.jspf incluye el cierre de <body> y </html> --%>

<div class="container mt-5" style="max-width: 500px;">

    <h1 class="mb-4 text-center">Iniciar Sesion</h1>
    <hr/>

    <%-- Muestra el mensaje de error si existe --%>
    <%
        String errorLogin = (String) request.getAttribute("errorLogin");
        if (errorLogin != null) {
    %>
    <div class="alert alert-danger" role="alert">
        <strong>Error:</strong> <%= errorLogin %>
    </div>
    <%
        }
    %>

    <%-- Formulario de Login --%>
    <form action="${pageContext.request.contextPath}/tienda/usuarios/login" method="post" class="p-4 shadow rounded">

        <div class="mb-3">
            <label for="nombre" class="form-label">Nombre de Usuario</label>
            <input name="nombre" id="nombre" type="text" class="form-control" required/>
        </div>

        <div class="mb-3">
            <label for="contrasenia" class="form-label">Contraseña</label>
            <input name="contrasenia" id="contrasenia" type="password" class="form-control" required/>
        </div>

        <div class="d-grid gap-2 mt-4">
            <button type="submit" class="btn btn-success btn-lg">
                Login
            </button>
        </div>
    </form>

</div>

<%@ include file="/WEB-INF/jsp/fragmentos/footer.jspf" %>