package org.joseAntonio.dao;

import org.joseAntonio.model.DetallePedidos;
import org.joseAntonio.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAOImpl extends AbstractDAOImpl implements PedidoDAO {

    private final DetallePedidosDAO detalleDAO = new DetallePedidosDAOImpl(); // NUEVO

    public int create(Pedido pedido) {
        Connection conn = null;
        PreparedStatement psPedido = null;
        ResultSet rsGenKey = null;

        try {
            conn = connectDB();
            conn.setAutoCommit(false); // Inicia la transacción

            // Insertar el pedido
            psPedido = conn.prepareStatement(
                    "INSERT INTO pedidos (usuario_id, importe) VALUES (?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            psPedido.setInt(1, pedido.getUsuarioId());
            psPedido.setDouble(2, pedido.getImporte());
            psPedido.executeUpdate();

            rsGenKey = psPedido.getGeneratedKeys();
            if (rsGenKey.next()) {
                pedido.setId(rsGenKey.getInt(1));
            }

            // Guardar detalles usando la misma conexión
            for (DetallePedidos detalle : pedido.getDetalles()) {
                detalle.setPedidoId(pedido.getId());
            }
            detalleDAO.saveBatch(pedido.getDetalles(), conn);

            conn.commit(); // Commit solo después de todo
        } catch (SQLException | ClassNotFoundException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            throw new RuntimeException("Error al guardar pedido y detalles", e);
        } finally {
            // Cerrar solo statement y resultset
            closeDb(null, psPedido, rsGenKey);

            // Cerrar conexión al final
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }

        return pedido.getId();
    }

    @Override
    public List<Pedido> getAll() {
        Connection conn = null;
        Statement s = null;
        ResultSet rs = null;
        List <Pedido> pedidos = new ArrayList<>();
        try {
            conn = connectDB();
            s =  conn.createStatement();
            rs =  s.executeQuery("SELECT * FROM pedidos");

            while (rs.next()) {
                Pedido pedido = new Pedido();
                int idx = 1;
                pedido.setId(rs.getInt(idx++));
                pedido.setUsuarioId(rs.getInt(idx++));
                pedido.setImporte(rs.getDouble(idx++));
                pedidos.add(pedido);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, s, rs);
        }
        return pedidos;
    }

    @Override
    public Optional<Pedido> find(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectDB();
            ps = conn.prepareStatement("SELECT * FROM pedidos WHERE id = ?");
            int idx = 1;
            ps.setInt(idx++,id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Pedido pedido = new Pedido();
                idx = 1;
                pedido.setId(rs.getInt(idx++));
                pedido.setUsuarioId(rs.getInt(idx++));
                pedido.setImporte(rs.getDouble(idx++));
                return Optional.of(pedido);
            }


        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }
        return Optional.empty();
    }

    @Override
    public void update(Pedido pedido) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectDB();
            ps = conn.prepareStatement("UPDATE pedidos SET usuario_id = ?, importe = ? WHERE id = ?");
            int idx = 1;
            ps.setInt(idx++,pedido.getUsuarioId());
            ps.setDouble(idx++,pedido.getImporte());
            ps.setInt(idx++,pedido.getId());

            int rows = ps.executeUpdate();
            if (rows == 0)
                System.out.println("UPDATE pedidos con 0 filas actualizadas");

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
            ps = conn.prepareStatement("DELETE FROM pedidos WHERE id = ?");

            int idx = 1;
            ps.setInt(idx++,id);
            int rows = ps.executeUpdate();
            if (rows == 0)
                System.out.println("DELETE de pedidos con 0 filas eliminadas");

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }
    }

    @Override
    public List<Pedido> findAllPedidos() {
        Connection conn = null;
        PreparedStatement ps = null; // Usamos PreparedStatement por consistencia, aunque Statement es válido aquí
        ResultSet rs = null;

        List<Pedido> pedidos = new ArrayList<>();

        final String SQL = "SELECT id, usuario_id, importe FROM pedidos ORDER BY id DESC";

        try {
            conn = connectDB();
            ps = conn.prepareStatement(SQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido();
                int idx = 1;
                pedido.setId(rs.getInt(idx++));
                pedido.setUsuarioId(rs.getInt(idx++));
                pedido.setImporte(rs.getDouble(idx++));

                pedidos.add(pedido);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error al buscar todos los pedidos", e);
        } finally {
            closeDb(conn, ps, rs);
        }

        return pedidos;
    }


    public List<Pedido> findPedidosByUsuarioId(int usuarioId) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<Pedido> pedidos = new ArrayList<>();

        final String SQL = "SELECT id, usuario_id, importe FROM pedidos WHERE usuario_id = ? ORDER BY id DESC";

        try {
            conn = connectDB();
            ps = conn.prepareStatement(SQL);

            ps.setInt(1, usuarioId);
            rs = ps.executeQuery();

            while (rs.next()) {
                Pedido pedido = new Pedido();
                int idx = 1;
                pedido.setId(rs.getInt(idx++));         // Columna 1: id
                pedido.setUsuarioId(rs.getInt(idx++));  // Columna 2: usuario_id
                pedido.setImporte(rs.getDouble(idx++)); // Columna 3: importe

                pedidos.add(pedido);
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error al buscar pedidos del usuario con ID: " + usuarioId, e);
        } finally {
            closeDb(conn, ps, rs);
        }

        return pedidos;
    }

    public Pedido findById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = connectDB();
            ps = conn.prepareStatement(
                    "SELECT id, usuario_id, importe FROM pedidos WHERE id = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                Pedido pedido = new Pedido();
                int idx = 1;
                pedido.setId(rs.getInt(idx++));
                pedido.setUsuarioId(rs.getInt(idx++));
                pedido.setImporte(rs.getDouble(idx++));
                return pedido;
            }

            return null;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error al buscar pedido por ID: " + id, e);
        } finally {
            closeDb(conn, ps, rs);
        }
    }


}
