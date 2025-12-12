package org.joseAntonio.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.joseAntonio.dao.*;
import org.joseAntonio.model.*;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "pedidoServlet", value = "/tienda/pedidos/*")
public class PedidoServlet extends HttpServlet {

    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuarioLogueado");
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/tienda/usuarios/login");
            return;
        }

        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo)) {

            List<Pedido> pedidos;

            if ("Administrador".equalsIgnoreCase(usuario.getRol())) {
                pedidos = pedidoDAO.findAllPedidos();
            } else {
                pedidos = pedidoDAO.findPedidosByUsuarioId(usuario.getId());
            }

            request.setAttribute("listaPedidos", pedidos);

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/WEB-INF/jsp/pedidos/pedidos.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (pathInfo.matches("^/\\d+$")) {

            int pedidoId = Integer.parseInt(pathInfo.substring(1));

            Pedido pedido = pedidoDAO.findById(pedidoId);

            if (pedido == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            if (!"Administrador".equalsIgnoreCase(usuario.getRol())
                    && pedido.getUsuarioId() != usuario.getId()) {

                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }

            DetallePedidosDAO detalleDAO = new DetallePedidosDAOImpl();
            List<DetallePedidos> detalles = detalleDAO.findByPedidoId(pedidoId);

            request.setAttribute("pedido", pedido);
            request.setAttribute("detalles", detalles);

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/WEB-INF/jsp/pedidos/detalles-pedido.jsp");
            dispatcher.forward(request, response);
            return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}
