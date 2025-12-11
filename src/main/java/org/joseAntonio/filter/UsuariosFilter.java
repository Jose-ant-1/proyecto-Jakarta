package org.joseAntonio.filter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.joseAntonio.model.Usuario;

// Asumo que tienes el @WebInitParam para definir el rol de acceso
@WebFilter(
        urlPatterns = "/tienda/usuarios/*",
        initParams = @WebInitParam(name = "acceso-concedido-a-rol", value = "Administrador")
)
public class UsuariosFilter extends HttpFilter implements Filter {

    private String rolAcceso; // Inicializado en init (omitido aquí por brevedad)

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        // Asegúrate de que este método exista y funcione correctamente
        this.rolAcceso = fConfig.getInitParameter("acceso-concedido-a-rol");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String pathInfo = httpRequest.getPathInfo();
        String contextPath = httpRequest.getContextPath();

        // Manejar casos nulos en pathInfo para la ruta raíz: /tienda/usuarios
        if (pathInfo == null) {
            pathInfo = "/";
        }

        // -----------------------------------------------------------
        // 1. Rutas de ACCESO LIBRE (login y logout)
        // -----------------------------------------------------------

        if ("/login".equals(pathInfo) || "/logout".equals(pathInfo)) {
            // Permitir el paso para que el UsuariosServlet maneje la vista/lógica de login/logout
            chain.doFilter(request, response);
            return;
        }

        // -----------------------------------------------------------
        // 2. Rutas PROTEGIDAS (Listado de Usuarios, Crear, Editar, Eliminar)
        // -----------------------------------------------------------

        HttpSession session = httpRequest.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;

        String urlLogin = contextPath + "/tienda/usuarios/login";
        String urlTienda = contextPath + "/tienda/productos"; // Redirección para clientes

        // A. Si no hay usuario logueado, redirigir a login
        if (usuario == null) {
            httpResponse.sendRedirect(urlLogin);
            return;
        }

        // B. Si está logueado, verificar el rol
        if (rolAcceso.equals(usuario.getRol())) {
            // El Administrador tiene permiso para todas las rutas restantes
            chain.doFilter(request, response);
        } else {
            // El Cliente (u otro rol) intenta acceder a /tienda/usuarios/* (que no es /login o /logout)
            // Denegar y redirigir a la tienda/productos
            httpResponse.sendRedirect(urlTienda);
        }
    }

    // El método destroy se omite por brevedad
}