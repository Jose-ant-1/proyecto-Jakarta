package org.joseAntonio.dao;

import org.joseAntonio.model.DetallePedidos;
import org.joseAntonio.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DetallePedidosDAOImpl extends AbstractDAOImpl implements DetallePedidosDAO {
    @Override
    public void create(DetallePedidos detallePedidos) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rsGenKey = null;

        try {
            conn = connectDB();
            ps = conn.prepareStatement("INSERT INTO detalle_pedidos VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);

            int idx = 1;
            ps.setInt(idx++, detallePedidos.getId());
            ps.setInt(idx++, detallePedidos.getPedidoId());
            ps.setInt(idx++, detallePedidos.getProductoId());
            ps.setInt(idx++, detallePedidos.getCantidad());
            ps.setDouble(idx++, detallePedidos.getPrecio());

            int rows = ps.executeUpdate();
            if (rows == 0)
                System.out.println("INSERT de detallePedidos con 0 filas insertadas");
            rsGenKey = ps.getGeneratedKeys();
            if (rsGenKey.next())
                detallePedidos.setId(rsGenKey.getInt(1));

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }
    }

    @Override
    public List<DetallePedidos> getAll() {

        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;

        List<DetallePedidos> detallePedidos = new ArrayList<>();

        try {
            conn = connectDB();
            s = conn.createStatement();
            rs = s.executeQuery("SELECT * FROM detalle_pedidos");

            while (rs.next()) {
                DetallePedidos detallePedido = new DetallePedidos();
                int idx = 1;
                detallePedido.setId(rs.getInt(idx++));
                detallePedido.setPedidoId(rs.getInt(idx++));
                detallePedido.setProductoId(rs.getInt(idx++));
                detallePedido.setCantidad(rs.getInt(idx++));
                detallePedido.setPrecio(rs.getDouble(idx++));
                detallePedidos.add(detallePedido);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, s, rs);
        }
        return detallePedidos;
    }

    @Override
    public Optional<DetallePedidos> find(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectDB();
            ps = conn.prepareStatement("SELECT * FROM detalle_pedidos WHERE id = ?");
            int idx = 1;
            ps.setInt(idx++, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                DetallePedidos detallePedido = new DetallePedidos();
                idx = 1;
                detallePedido.setId(rs.getInt(idx++));
                detallePedido.setPedidoId(rs.getInt(idx++));
                detallePedido.setProductoId(rs.getInt(idx++));
                detallePedido.setCantidad(rs.getInt(idx++));
                detallePedido.setPrecio(rs.getDouble(idx++));
                return Optional.of(detallePedido);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }
        return Optional.empty();
    }

    @Override
    public void update(DetallePedidos detallePedidos) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectDB();
            ps = conn.prepareStatement("UPDATE detalle_pedidos SET pedido_id = ?, producto_id = ?, cantidad = ?, precio_unitario = ? WHERE id = ?");
            int idx = 1;
            ps.setInt(idx++, detallePedidos.getPedidoId());
            ps.setInt(idx++, detallePedidos.getProductoId());
            ps.setInt(idx++, detallePedidos.getCantidad());
            ps.setDouble(idx++, detallePedidos.getPrecio());
            ps.setInt(idx++, detallePedidos.getId());
            int rows =  ps.executeUpdate();
            if (rows == 0)
                System.out.println("UPDATE de detallePedidos con 0 filas actualizadas");

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
            ps =  conn.prepareStatement("DELETE FROM detalle_pedidos WHERE id = ?");

            int idx = 1;
            ps.setInt(idx++, id);
            int rows = ps.executeUpdate();
            if (rows == 0)
                System.out.println("DELETE de detallePedidos con 0 filas actualizadas");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }
    }

    @Override
    public void saveBatch(List<DetallePedidos> detalles, Connection conn) throws SQLException {
        PreparedStatement ps = null;

        final String INSERT_DETALLE = "INSERT INTO detalle_pedidos (pedido_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";

        try {
            ps = conn.prepareStatement(INSERT_DETALLE);

            for (DetallePedidos detalle : detalles) {
                int idx = 1;
                ps.setInt(idx++, detalle.getPedidoId());
                ps.setInt(idx++, detalle.getProductoId());
                ps.setInt(idx++, detalle.getCantidad());
                ps.setDouble(idx++, detalle.getPrecio());
                ps.addBatch();
            }

            ps.executeBatch();

        } finally {
            closeDb(conn, ps, null);
        }
    }

    @Override
    public List<DetallePedidos> findByPedidoId(int pedidoId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<DetallePedidos> detalles = new ArrayList<>();

        try {
            conn = connectDB();
            ps = conn.prepareStatement(
                    "SELECT id, pedido_id, producto_id, cantidad, precio_unitario FROM detalle_pedidos WHERE pedido_id = ?"
            );

            ps.setInt(1, pedidoId);
            rs = ps.executeQuery();

            while (rs.next()) {
                DetallePedidos detallePedido = new DetallePedidos();
                int idx = 1;
                // Asumo el orden de las columnas: id, pedido_id, producto_id, cantidad, precio
                detallePedido.setId(rs.getInt(idx++));
                detallePedido.setPedidoId(rs.getInt(idx++));
                detallePedido.setProductoId(rs.getInt(idx++));
                detallePedido.setCantidad(rs.getInt(idx++));
                detallePedido.setPrecio(rs.getDouble(idx++));
                detalles.add(detallePedido);
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error al buscar detalles por ID de pedido: " + pedidoId, e);
        } finally {
            closeDb(conn, ps, rs);
        }
        return detalles;
    }

}
