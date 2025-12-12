package org.joseAntonio.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.joseAntonio.dao.ProductoDAO;
import org.joseAntonio.dao.ProductoDAOImpl;
import org.joseAntonio.model.Producto;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "productosServlet", value = "/tienda/productos/*")
public class ProductosServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        RequestDispatcher dispatcher = null;
        String pathInfo = request.getPathInfo();
        ProductoDAO productoDAO = new ProductoDAOImpl();

        if (pathInfo == null || "/".equals(pathInfo)) {
            List<Producto> listaProductos = productoDAO.getAll();
            request.setAttribute("listaProductos", listaProductos);

            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/productos.jsp");
        } else {
            pathInfo = pathInfo.replaceAll("/$", "");
            String[] pathParts = pathInfo.split("/");

            // /tienda/productos/crear
            if (pathParts.length == 2 && "crear".equals(pathParts[1])) {
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/crear-producto.jsp");
                // /tienda/productos/{id} (ver detalle)
            } else if (pathParts.length == 2) {
                try {
                    int id = Integer.parseInt(pathParts[1]);
                    Optional<Producto> productoOpt = productoDAO.find(id);
                    if (productoOpt.isPresent()) {
                        request.setAttribute("producto", productoOpt.get());
                        dispatcher =  request.getRequestDispatcher("/WEB-INF/jsp/productos/detalle-producto.jsp");
                    } else {
                        dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/productos.jsp");
                    }
                } catch (NumberFormatException e) {
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/productos.jsp");
                }
                // tienda/productos/editar/{id} (edicion)
            } else if (pathParts.length == 3 && "editar".equals(pathParts[1])) {
                try {
                    int id = Integer.parseInt(pathParts[2]);
                    Optional<Producto> productoOpt = productoDAO.find(id);
                    if (productoOpt.isPresent()) {
                        request.setAttribute("producto", productoOpt.get());
                        dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/editar-producto.jsp");
                    } else {
                        dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/productos.jsp");
                    }
                } catch (NumberFormatException e) {
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/productos.jsp");
                }
            } else {
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/productos.jsp");
            }

        }

        if (dispatcher != null) {
            dispatcher.forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servlet: No se pudo encontrar la vista.");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductoDAO productoDAO = new ProductoDAOImpl();
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();

        if (pathInfo == null || "/".equals(pathInfo) || "/crear".equals(pathInfo) || pathInfo.endsWith("/crear")) {

            String nombre = request.getParameter("nombre");
            String precioStr = request.getParameter("precio");
            String imagenUrl = request.getParameter("imagenUrl");

            try {
                double precio = Double.parseDouble(precioStr);

                Producto nuevoProducto = new Producto();
                nuevoProducto.setNombre(nombre);
                nuevoProducto.setPrecio(precio);
                nuevoProducto.setImagenUrl(imagenUrl);
                productoDAO.create(nuevoProducto);

                response.sendRedirect(contextPath + "/tienda/productos");
                return;

            } catch (NumberFormatException e) {
                System.err.println("Error al parsear el precio: " + e.getMessage());
                response.sendRedirect(contextPath + "/tienda/productos/crear?error=precioInvalido");
                return;
            }

        } else if (pathInfo.startsWith("/editar/")) {
            try {
                int id =  Integer.parseInt(pathInfo.substring("/editar/".length()));

                Optional<Producto> productoOpt = productoDAO.find(id);

                if (productoOpt.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado para edición.");
                    return;
                }

                Producto productoAEditar = productoOpt.get();

                String nuevoNombre = request.getParameter("nombre");
                String nuevoPrecioStr = request.getParameter("precio");
                String nuevoImagenUrl = request.getParameter("imagenUrl");

                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);

                productoAEditar.setNombre(nuevoNombre);
                productoAEditar.setPrecio(nuevoPrecio);
                productoAEditar.setImagenUrl(nuevoImagenUrl);

                productoDAO.update(productoAEditar);

            } catch (NumberFormatException e) {
                throw new ServletException("Error de formato al editar producto", e);
            }

            response.sendRedirect(contextPath + "/tienda/productos");
            return;

        } else if (pathInfo.startsWith("/eliminar/")) {
            try {
                int id =  Integer.parseInt(pathInfo.substring("/eliminar/".length()));

                productoDAO.delete(id);

            } catch (NumberFormatException e) {
                throw new ServletException("Error de formato al eliminar producto (ID no válido)", e);
            }

            response.sendRedirect(contextPath + "/tienda/productos");
            return;
        }

        response.sendRedirect(contextPath + "/tienda/productos");
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
