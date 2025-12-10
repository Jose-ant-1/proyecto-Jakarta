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
        RequestDispatcher dispatcher;
        String pathInfo = request.getPathInfo();
        ProductoDAO productoDAO = new ProductoDAOImpl();

        if (pathInfo == null || "/".equals(pathInfo)) {
            List<Producto> listaProductos = productoDAO.getAll();
            request.setAttribute("listaProductos", listaProductos);
        } else {
            pathInfo = pathInfo.replaceAll("/$", "");
            String[] pathParts = pathInfo.split("/");
            if (pathParts.length == 2 && "crear".equals(pathParts[1])) {
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/crear-producto.jsp");
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
                    throw new RuntimeException(e);
                }
            } else {
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/productos/productos.jsp");
            }
            dispatcher.forward(request, response);
        }


    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

}
