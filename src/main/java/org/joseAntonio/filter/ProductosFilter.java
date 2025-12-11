package org.joseAntonio.filter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.joseAntonio.model.Usuario; // Ajusta el paquete si es necesario

@WebFilter(urlPatterns = "/tienda/productos/*")
public class ProductosFilter extends HttpFilter implements Filter {

    // Método auxiliar para identificar las operaciones que son SÓLO LECTURA (públicas)
    private boolean isPublicOperation(HttpServletRequest httpRequest) {
        String pathInfo = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();

        // 1. Listado (GET a /tienda/productos/ o /tienda/productos)
        if ((pathInfo == null || "/".equals(pathInfo)) && "GET".equals(method)) {
            return true;
        }

        // 2. Detalle (GET a /tienda/productos/{id})
        if (pathInfo != null) {
            pathInfo = pathInfo.replaceAll("/$", "");
            String[] pathsParts = pathInfo.split("/");
            // Verifica que haya 2 partes (el ID) y que el método sea GET
            if (pathsParts.length == 2 && pathsParts[1].matches("\\d+") && "GET".equals(method)) {
                return true;
            }
        }

        // 3. Todo lo demás es CRUD (Crear, Editar, Borrar) y es restringido.
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Si es una operación pública (Listar o Detalle), permitir el acceso a todos.
        if (isPublicOperation(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }

        // --- LÓGICA DE PROTECCIÓN PARA OPERACIONES CRUD (Restringidas) ---

        HttpSession session = httpRequest.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;
        String urlLogin = httpRequest.getContextPath() + "/tienda/usuarios/login";

        // A. Si se intenta CRUD sin usuario logueado, redirigir al login
        if (usuario == null) {
            httpResponse.sendRedirect(urlLogin);
            return;
        }

        // B. Si está logueado, verificar el rol para CRUD
        if ("Administrador".equals(usuario.getRol())) {
            // El administrador tiene permiso para CRUD
            chain.doFilter(request, response);
        } else {
            // Cliente (u otro rol) intenta CRUD: denegar y redirigir al inicio.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/tienda/productos");
        }
    }

    // ... (init y destroy) ...
}