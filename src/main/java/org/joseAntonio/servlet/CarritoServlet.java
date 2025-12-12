package org.joseAntonio.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.joseAntonio.dao.PedidoDAO;
import org.joseAntonio.dao.PedidoDAOImpl;
import org.joseAntonio.dao.ProductoDAO;
import org.joseAntonio.dao.ProductoDAOImpl;
import org.joseAntonio.model.DetallePedidos;
import org.joseAntonio.model.Pedido;
import org.joseAntonio.model.Producto;
import org.joseAntonio.model.Usuario;

import java.io.IOException;
import java.util.*;

@WebServlet("/tienda/carrito/*")
public class CarritoServlet extends HttpServlet {

    private static final String CARRO_SESSION_KEY = "carrito";
    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo != null && pathInfo.contains("/ver")) {
            request.getRequestDispatcher("/WEB-INF/jsp/carrito/carrito.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        if (pathInfo == null || "/".equals(pathInfo) || "/add".equals(pathInfo)) {
            doPostAddProduct(request, response);

        } else if ("/remove".equals(pathInfo)) {
            doPostRemoveProduct(request, response);
        }

        else if ("/proceder-pago".equals(pathInfo)) {
            doPostProcederPago(request, response);

        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void doPostAddProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int productoId;
        int cantidad;

        try {
            productoId = Integer.parseInt(request.getParameter("productoId"));
            cantidad = Integer.parseInt(request.getParameter("cantidad"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto o cantidad inválida.");
            return;
        }

        if (cantidad <= 0) {
            response.sendRedirect(request.getContextPath() + "/tienda/productos/" + productoId);
            return;
        }

        Optional<Producto> optProducto = productoDAO.find(productoId);

        if (optProducto.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado.");
            return;
        }

        Producto producto = optProducto.get();

        HttpSession session = request.getSession();
        Map<Producto, Integer> carrito = (Map<Producto, Integer>) session.getAttribute(CARRO_SESSION_KEY);

        if (carrito == null) {
            carrito = new HashMap<>();
            session.setAttribute(CARRO_SESSION_KEY, carrito);
        }

        carrito.put(producto, carrito.getOrDefault(producto, 0) + cantidad);

        session.setAttribute("mensajeCarrito", "Producto **" + producto.getNombre() + "** añadido al carrito. Cantidad total: " + carrito.get(producto));

        response.sendRedirect(request.getContextPath() + "/tienda/productos/" + productoId);
    }

    private void doPostRemoveProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int productoId;
        try {
            productoId = Integer.parseInt(request.getParameter("productoId"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de producto inválida.");
            return;
        }

        HttpSession session = request.getSession();

        Map<Producto, Integer> carrito = (Map<Producto, Integer>) session.getAttribute(CARRO_SESSION_KEY);

        if (carrito != null) {
            Producto productoToRemove = null;

            for (Producto p : carrito.keySet()) {
                if (p.getId() == productoId) {
                    productoToRemove = p;
                    break;
                }
            }

            if (productoToRemove != null) {
                carrito.remove(productoToRemove);
                session.setAttribute("mensajeCarrito", "Producto eliminado del carrito.");
            }
        }

        response.sendRedirect(request.getContextPath() + "/tienda/carrito/ver");
    }

    private void doPostProcederPago(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");


        Map<Producto, Integer> carrito = (Map<Producto, Integer>) session.getAttribute(CARRO_SESSION_KEY);

        if (carrito == null || carrito.isEmpty()) {
            session.setAttribute("errorCarrito", "El carrito está vacío. No se puede procesar el pago.");
            response.sendRedirect(request.getContextPath() + "/tienda/carrito/ver");
            return;
        }

        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setUsuarioId(usuario.getId());

        double totalImporte = 0.0;
        List<DetallePedidos> detalles = new ArrayList<>();

        for (Map.Entry<Producto, Integer> entry : carrito.entrySet()) {
            Producto producto = entry.getKey();
            int cantidad = entry.getValue();

            double subtotal = producto.getPrecio() * cantidad;
            totalImporte += subtotal;

            DetallePedidos detalle = new DetallePedidos();
            detalle.setProductoId(producto.getId());
            detalle.setCantidad(cantidad);
            detalle.setPrecio(producto.getPrecio()); // Guardar el precio actual del producto

            detalles.add(detalle);
        }

        nuevoPedido.setImporte(totalImporte);
        nuevoPedido.setDetalles(detalles);


        try {
            int pedidoId = pedidoDAO.create(nuevoPedido);

            session.removeAttribute(CARRO_SESSION_KEY);
            session.setAttribute("mensajeCarrito",
                    String.format("¡Pago Procesado Exitosamente! Pedido #%d generado por un total de %.2f €.",
                            pedidoId, totalImporte));

        } catch (RuntimeException e) {
            session.setAttribute("errorCarrito", "Error al procesar el pago. La transacción fue revertida. Intente de nuevo.");
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/tienda/carrito/ver");
    }


}