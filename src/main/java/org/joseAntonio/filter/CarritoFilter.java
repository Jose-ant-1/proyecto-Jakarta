package org.joseAntonio.filter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.joseAntonio.model.Usuario;

@WebFilter(urlPatterns = "/tienda/carrito/*")
public class CarritoFilter extends HttpFilter implements Filter {

    private static final String ROL_REQUERIDO = "Usuario";
    private static final String PATH_PAGO = "/proceder-pago";
    private static final String SESSION_KEY_URL = "url_previo_login";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String pathInfo = httpRequest.getPathInfo();
        String contextPath = httpRequest.getContextPath();
        String urlLogin = contextPath + "/tienda/usuarios/login";
        String urlCarrito = contextPath + "/tienda/carrito/ver";

        if (pathInfo == null || !PATH_PAGO.equals(pathInfo)) {
            chain.doFilter(request, response);
            return;
        }


        HttpSession session = httpRequest.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;
        String rolUsuario = (usuario != null) ? usuario.getRol() : null;

        if (usuario == null) {
            HttpSession newSession = httpRequest.getSession(true);
            newSession.setAttribute(SESSION_KEY_URL, urlCarrito);

            httpResponse.sendRedirect(urlLogin);
            return;
        }

        if (ROL_REQUERIDO.equals(rolUsuario)) {
            chain.doFilter(request, response);
        } else {
            HttpSession newSession = httpRequest.getSession(true);
            newSession.setAttribute("errorCarrito", "Solo los usuarios con el rol '" + ROL_REQUERIDO + "' pueden proceder al pago.");
            httpResponse.sendRedirect(urlCarrito);
        }
    }
}