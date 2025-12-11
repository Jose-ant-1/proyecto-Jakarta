package org.joseAntonio.dao;

import org.joseAntonio.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuariosDAOImpl extends AbstractDAOImpl implements UsuariosDAO {
    @Override
    public void create(Usuario usuario) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rsGenKeys = null;

        try {
            conn = connectDB();
            ps =  conn.prepareStatement("INSERT INTO usuarios (nombre,rol,password) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);

            int idx = 1;
            ps.setString(idx++, usuario.getNombre());
            ps.setString(idx++, usuario.getRol());
            ps.setString(idx++, usuario.getContrasenia());

            int rows = ps.executeUpdate();
            if (rows == 0)  System.out.println("INSERT de usuario con 0 filas insertadas");
            rsGenKeys = ps.getGeneratedKeys();
            if (rsGenKeys.next())  usuario.setId(rsGenKeys.getInt(1));

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }

    }

    @Override
    public List<Usuario> getAll() {

        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;

        List<Usuario> usuarios = new ArrayList<>();
        try {
            conn = connectDB();
            s = conn.createStatement();

            rs =  s.executeQuery("SELECT * FROM usuarios");

            while (rs.next()) {
                Usuario usuario = new Usuario();
                int idx = 1;
                usuario.setId(rs.getInt(idx++));
                usuario.setNombre(rs.getString(idx++));
                usuario.setRol(rs.getString(idx++));
                usuario.setContrasenia(rs.getString(idx));
                usuarios.add(usuario);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, s, rs);
        }

        return usuarios;
    }

    @Override
    public Optional<Usuario> find(int id) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectDB();
            ps =   conn.prepareStatement("SELECT * FROM usuarios WHERE id=?");

            int idx = 1;
            ps.setInt(idx, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                idx = 1;
                usuario.setId(rs.getInt(idx++));
                usuario.setNombre(rs.getString(idx++));
                usuario.setRol(rs.getString(idx++));
                usuario.setContrasenia(rs.getString(idx));
                return Optional.of(usuario);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }

        return Optional.empty();
    }

    @Override
    public void update(Usuario usuario) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = connectDB();

            ps = conn.prepareStatement("UPDATE usuarios SET nombre=?,rol=?,password=? WHERE id=?");
            int idx = 1;
            ps.setString(idx++, usuario.getNombre());
            ps.setString(idx++, usuario.getRol());
            ps.setString(idx++, usuario.getContrasenia());
            ps.setInt(idx++, usuario.getId());

            int rows = ps.executeUpdate();

            if (rows == 0)
                System.out.println("UPDATE de usuario con 0 filas insertadas");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }


    }

    @Override
    public void delete(int id) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = connectDB();
            ps = conn.prepareStatement("DELETE FROM usuarios WHERE id=?");
            int idx = 1;
            ps.setInt(idx, id);
            int rows = ps.executeUpdate();
            if (rows == 0)
                System.out.println("DELETE de usuario con 0 filas insertadas");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }  finally {
            closeDb(conn, ps, rs);
        }

    }

    @Override
    public Optional<Usuario> encontrarPorNombreYContrasenia(String nombre, String contrasenia) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectDB();
            // 1. Buscamos por el nombre y la columna 'password' de la base de datos.
            ps = conn.prepareStatement("SELECT * FROM usuarios WHERE nombre=? AND password=?");

            int idx = 1;
            ps.setString(idx++, nombre);
            ps.setString(idx++, contrasenia); // Nota: Esto asume que la contraseña está en texto plano, ¡Recuerda el hashing!

            rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                idx = 1;
                usuario.setId(rs.getInt(idx++));
                usuario.setNombre(rs.getString(idx++));
                usuario.setRol(rs.getString(idx++));
                // Aunque no es necesario devolver la contraseña, la asignamos para completar el objeto
                usuario.setContrasenia(rs.getString(idx));
                return Optional.of(usuario);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }

        return Optional.empty();
    }

}
