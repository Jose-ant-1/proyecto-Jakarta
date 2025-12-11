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
                    // Manejar la excepción sin propagarla fuera
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/productos.jsp");
                }
            } else {
                // ruta desconocida
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/productos.jsp");
            }

        }

        if (dispatcher != null) {
            dispatcher.forward(request, response);
        } else {
            // Este caso de error es poco probable si la lógica de arriba está bien
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servlet: No se pudo encontrar la vista.");
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ProductoDAO productoDAO = new ProductoDAOImpl();
        String pathInfo = request.getPathInfo();
        String contextPath = request.getContextPath();

        // 1. Manejo de CREACIÓN: /tienda/productos/ o /tienda/productos/crear
        if (pathInfo == null || "/".equals(pathInfo) || "/crear".equals(pathInfo) || pathInfo.endsWith("/crear")) {

            String nombre = request.getParameter("nombre");
            String precioStr = request.getParameter("precio");

            try {
                double precio = Double.parseDouble(precioStr);

                Producto nuevoProducto = new Producto();
                nuevoProducto.setNombre(nombre);
                nuevoProducto.setPrecio(precio);

                productoDAO.create(nuevoProducto);

                // Redirigir al listado principal (Post-Redirect-Get)
                response.sendRedirect(contextPath + "/tienda/productos");
                return;

            } catch (NumberFormatException e) {
                // Manejo básico de error si el precio no es un número válido
                System.err.println("Error al parsear el precio: " + e.getMessage());
                // Podrías redirigir de vuelta al formulario de creación con un mensaje de error.
                response.sendRedirect(contextPath + "/tienda/productos/crear?error=precioInvalido");
                return;
            }

            // 2. Manejo de EDICIÓN: /tienda/productos/editar/{id}
            // Nota: En la vista de listado, el botón de editar dirige a /editar/{id} (GET), pero
            // el formulario dentro de editar-producto.jsp debe hacer POST a /editar/{id} para este bloque.
        } else if (pathInfo.startsWith("/editar/")) {
            try {
                // Extraer el ID de la URL: /editar/{id}
                int id =  Integer.parseInt(pathInfo.substring("/editar/".length()));

                Optional<Producto> productoOpt = productoDAO.find(id);

                if (productoOpt.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Producto no encontrado para edición.");
                    return;
                }

                Producto productoAEditar = productoOpt.get();

                // Actualizar campos
                String nuevoNombre = request.getParameter("nombre");
                String nuevoPrecioStr = request.getParameter("precio");

                double nuevoPrecio = Double.parseDouble(nuevoPrecioStr);

                productoAEditar.setNombre(nuevoNombre);
                productoAEditar.setPrecio(nuevoPrecio);

                productoDAO.update(productoAEditar);

            } catch (NumberFormatException e) {
                // Error si el ID o el precio no son números
                throw new ServletException("Error de formato al editar producto", e);
            }

            // Redirigir al listado después de la edición
            response.sendRedirect(contextPath + "/tienda/productos");
            return;

            // 3. Manejo de ELIMINACIÓN: /tienda/productos/eliminar/{id}
        } else if (pathInfo.startsWith("/eliminar/")) {
            try {
                // Extraer el ID de la URL: /eliminar/{id}
                int id =  Integer.parseInt(pathInfo.substring("/eliminar/".length()));

                productoDAO.delete(id);

            } catch (NumberFormatException e) {
                throw new ServletException("Error de formato al eliminar producto (ID no válido)", e);
            }

            // Redirigir al listado después de la eliminación
            response.sendRedirect(contextPath + "/tienda/productos");
            return;
        }

        // Si llega aquí, es una ruta POST desconocida, redirigir al listado
        response.sendRedirect(contextPath + "/tienda/productos");
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
