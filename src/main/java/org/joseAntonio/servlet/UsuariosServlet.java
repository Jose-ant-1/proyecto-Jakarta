package org.joseAntonio.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.joseAntonio.dao.UsuariosDAO;
import org.joseAntonio.dao.UsuariosDAOImpl;
import org.joseAntonio.model.Usuario;
import org.joseAntonio.utils.Utils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "usuariosServlet", value = "/tienda/usuarios/*")
public class UsuariosServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

// UsuariosServlet.java (Método doGet corregido)

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UsuariosDAO usuariosDAO = new UsuariosDAOImpl();
        RequestDispatcher dispatcher = null; // Inicializar a null

        String pathInfo = request.getPathInfo();

        // /tienda/usuarios/ - /tienda/usuarios (Listado general)

        if (pathInfo == null || "/".equals(pathInfo)) {
            List<Usuario> usuarios = usuariosDAO.getAll();
            request.setAttribute("listaUsuarios", usuarios);
            dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/usuarios/usuarios.jsp");
        } else {
            pathInfo = pathInfo.replaceAll("/$", "");
            String[] pathsParts = pathInfo.split("/");

            // /tienda/usuarios/crear
            if (pathsParts.length == 2 && "crear".equals(pathsParts[1])) {
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/usuarios/crear-usuario.jsp");

                // /tienda/usuarios/editar/{id} (Vista de Edición)
            } else if (pathsParts.length == 3 && "editar".equals(pathsParts[1])) {
                try {
                    int id = Integer.parseInt(pathsParts[2]);
                    Optional<Usuario> usuarioOpt = usuariosDAO.find(id);
                    if (usuarioOpt.isPresent()) {
                        request.setAttribute("usuario", usuarioOpt.get());
                        dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/usuarios/editar-usuario.jsp");
                    } else {
                        // ID válido, pero usuario no existe -> Volver al listado
                        dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/usuarios/usuarios.jsp");
                    }
                } catch (NumberFormatException e) {
                    // ID no es un número -> Volver al listado
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/usuarios/usuarios.jsp");
                }

                // /tienda/usuarios/{id} (Ver Detalle)
            } else if (pathsParts.length == 2) {
                try {
                    int id = Integer.parseInt(pathsParts[1]);
                    Optional<Usuario> usuarioOpt = usuariosDAO.find(id);
                    if (usuarioOpt.isPresent()) {
                        request.setAttribute("usuario", usuarioOpt.get());
                        dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/usuarios/detalle-usuario.jsp");
                    } else {
                        // ID válido, pero usuario no existe -> Volver al listado
                        dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/usuarios/usuarios.jsp");
                    }
                } catch (NumberFormatException e) {
                    // Si pathsParts[1] no es un número (ej: /tienda/usuarios/otro) -> Volver al listado
                    dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/usuarios/usuarios.jsp");
                }

                // Ruta desconocida

            } else {
                // Cualquier otra ruta no manejada, por defecto volvemos al listado
                List<Usuario> usuarios = usuariosDAO.getAll();
                request.setAttribute("listaUsuarios", usuarios);
                dispatcher = request.getRequestDispatcher("/WEB-INF/jsp/usuarios/usuarios.jsp");
            }
        }

        // Llamada  al forward (Debe ser la última línea)
        if (dispatcher != null) {
            dispatcher.forward(request, response);
        } else {
            // En caso de que, por alguna razón, dispatcher siga siendo null (caso improbable con la lógica anterior)
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servlet.");
        }
    }

    //REVISAR EDITAR
    //REVISAR EDITAR
    //REVISAR EDITAR
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UsuariosDAO usuariosDAO = new UsuariosDAOImpl();
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || "/".equals(pathInfo) || pathInfo.endsWith("/crear")) {
            Usuario usuario = new Usuario();
            usuario.setNombre(request.getParameter("nombre"));
            usuario.setRol(request.getParameter("rol"));
            try {
                usuario.setContrasenia(Utils.hashPassword(request.getParameter("password")));
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            usuariosDAO.create(usuario);
            response.sendRedirect(request.getContextPath() + "/tienda/usuarios");
            return;

        } else if (pathInfo.startsWith("/editar")) {
            try {
                int id =  Integer.parseInt(pathInfo.substring("/editar/".length()));

                Optional<Usuario> usuarioOpt = usuariosDAO.find(id);

                if (usuarioOpt.isEmpty()) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Usuario no encontrado.");
                    return;
                }
                Usuario usuario = usuarioOpt.get();
                usuario.setNombre(request.getParameter("nombre"));
                usuario.setRol(request.getParameter("rol"));
                String nuevaContrasenia = request.getParameter("contrasenia");
                if (nuevaContrasenia != null && !nuevaContrasenia.isBlank()) {
                    usuario.setContrasenia(Utils.hashPassword(nuevaContrasenia));
                }
                // Si no se proporcionó una nueva, el objeto 'usuario' aún tendrá la
                // contraseña hasheada existente de la base de datos.

                usuariosDAO.update(usuario);

            } catch (NumberFormatException | NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
            response.sendRedirect(request.getContextPath() + "/tienda/usuarios");
            return;
        } else if (pathInfo.startsWith("/eliminar/")) {
            try {
                int id =  Integer.parseInt(pathInfo.substring("/eliminar/".length()));
                usuariosDAO.delete(id);
            } catch (NumberFormatException e) {
                throw new RuntimeException(e);
            }
            response.sendRedirect(request.getContextPath() + "/tienda/usuarios");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/tienda/usuarios");

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UsuariosDAO usuariosDAO = new UsuariosDAOImpl();
        String id = request.getParameter("id");
        String nombre = request.getParameter("nombre");
        String rol =  request.getParameter("rol");
        String password =  request.getParameter("password");
        Usuario usuario = new Usuario();
        try {
            int codigo = Integer.parseInt(id);
            usuario.setId(codigo);
            usuario.setNombre(nombre);
            usuario.setRol(rol);
            usuario.setContrasenia(password);
            usuariosDAO.update(usuario);
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }


    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher;
        UsuariosDAO usuarioDAO = new UsuariosDAOImpl();
        String id = request.getParameter("id");

        try {
            int codgio = Integer.parseInt(id);
            usuarioDAO.delete(codgio);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }


    }
}
