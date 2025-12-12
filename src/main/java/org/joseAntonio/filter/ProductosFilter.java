package org.joseAntonio.filter;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.joseAntonio.model.Usuario;

@WebFilter(urlPatterns = "/tienda/productos/*")
public class ProductosFilter extends HttpFilter implements Filter {

    private boolean isPublicOperation(HttpServletRequest httpRequest) {
        String pathInfo = httpRequest.getPathInfo();
        String method = httpRequest.getMethod();

        if ((pathInfo == null || "/".equals(pathInfo)) && "GET".equals(method)) {
            return true;
        }

        if (pathInfo != null) {
            pathInfo = pathInfo.replaceAll("/$", "");
            String[] pathsParts = pathInfo.split("/");
            if (pathsParts.length == 2 && pathsParts[1].matches("\\d+") && "GET".equals(method)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        if (isPublicOperation(httpRequest)) {
            chain.doFilter(request, response);
            return;
        }


        HttpSession session = httpRequest.getSession(false);
        Usuario usuario = (session != null) ? (Usuario) session.getAttribute("usuarioLogueado") : null;
        String urlLogin = httpRequest.getContextPath() + "/tienda/usuarios/login";

        if (usuario == null) {
            httpResponse.sendRedirect(urlLogin);
            return;
        }

        if ("Administrador".equals(usuario.getRol())) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/tienda/productos");
        }
    }


}