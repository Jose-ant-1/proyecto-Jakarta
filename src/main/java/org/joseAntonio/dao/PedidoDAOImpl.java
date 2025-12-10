package org.joseAntonio.dao;

import org.joseAntonio.model.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PedidoDAOImpl extends AbstractDAOImpl implements PedidoDAO {
    @Override
    public void create(Pedido pedido) {

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs =  null;
        ResultSet rsGenKey = null;

        try {
            conn = connectDB();
            ps = conn.prepareStatement("INSERT INTO pedidos (usuario_id, importe) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);

            int idx = 1;
            ps.setInt(idx++,pedido.getId());
            ps.setDouble(idx++,pedido.getImporte());

            int rows = ps.executeUpdate();
            if (rows == 0)
                System.out.println("INSERT de pedidos con 0 filas insertadas");
            rsGenKey = ps.getGeneratedKeys();
            if (rsGenKey.next())
                pedido.setId(rsGenKey.getInt(1));

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            closeDb(conn, ps, rs);
        }

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
}
