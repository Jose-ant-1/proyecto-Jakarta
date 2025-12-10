package org.joseAntonio.dao;

import org.joseAntonio.model.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoDAOImpl extends AbstractDAOImpl implements ProductoDAO {
    @Override
    public void create(Producto producto) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs =  null;
        ResultSet rsGenKey = null;

        try {
            conn = connectDB();

            ps = conn.prepareStatement("insert into productos (nombre, precio) values (?, ?)", Statement.RETURN_GENERATED_KEYS);

            int idx = 1;
            ps.setString(idx++, producto.getNombre());
            ps.setDouble(idx++, producto.getPrecio());

            int rows =  ps.executeUpdate();
            if (rows == 0)
                System.out.println("INSERT de producto con 0 filas insertadas");

            rsGenKey = ps.getGeneratedKeys();
            if (rsGenKey.next())
                producto.setId(rsGenKey.getInt(1));


        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }


    }

    @Override
    public List<Producto> getAll() {
        Connection conn = null;
        Statement s = null;
        ResultSet rs =  null;
        List <Producto> productos = new ArrayList<>();
        try {
            conn = connectDB();
            s = conn.createStatement();
            rs = s.executeQuery("select * from productos");

            while (rs.next()) {
                Producto producto = new Producto();
                int idx = 1;
                producto.setId(rs.getInt(idx++));
                producto.setNombre(rs.getString(idx++));
                producto.setPrecio(rs.getDouble(idx++));
                productos.add(producto);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, s, rs);
        }


        return productos;
    }

    @Override
    public Optional<Producto> find(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs =  null;

        try {
            conn = connectDB();
            ps =  conn.prepareStatement("select * from productos where id = ?");
            int idx = 1;
            ps.setInt(idx++, id);

            rs =  ps.executeQuery();

            if (rs.next()) {
                Producto producto = new Producto();
                idx = 1;
                producto.setId(rs.getInt(idx++));
                producto.setNombre(rs.getString(idx++));
                producto.setPrecio(rs.getDouble(idx++));
                return Optional.of(producto);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }
        return Optional.empty();
    }

    @Override
    public void update(Producto producto) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs =  null;

        try {
            conn = connectDB();
            ps = conn.prepareStatement("update productos set nombre = ?, precio = ? where id = ?");
            int idx = 1;
            ps.setString(idx++, producto.getNombre());
            ps.setDouble(idx++, producto.getPrecio());
            ps.setInt(idx++, producto.getId());

            int rows =  ps.executeUpdate();

            if(rows == 0)
                System.out.println("UPDATE de producto con 0 filas insertadas");

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
        ResultSet rs =  null;

        try {
            conn = connectDB();

            ps =  conn.prepareStatement("delete from productos where id = ?");
            int idx = 1;
            ps.setInt(idx++, id);
            int rows =  ps.executeUpdate();
            if (rows == 0)
                System.out.println("DELETE de producto con 0 filas eliminadas");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }

    }

    @Override
    public List<Producto> buscarCoincidencia(String coincidencia) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs =  null;
        List<Producto> productos = new ArrayList<>();
        try {
            String coincidenciaSQL = "%" + coincidencia+ "%";
            conn = connectDB();
            ps = conn.prepareStatement("SELECT * FROM productos WHERE nombre LIKE ?");
            ps.setString(1, coincidenciaSQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                Producto producto = new Producto();
                int idx = 1;
                producto.setId(rs.getInt(idx++));
                producto.setNombre(rs.getString(idx++));
                producto.setPrecio(rs.getDouble(idx++));
                productos.add(producto);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }


        return productos;
    }
}
