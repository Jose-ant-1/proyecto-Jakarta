package org.joseAntonio.dao;

import org.joseAntonio.model.DetallePedidos;

import java.util.List;
import java.util.Optional;

public interface DetallePedidosDAO {
    public void create(DetallePedidos detallePedidos);
    public List<DetallePedidos> getAll();
    public Optional<DetallePedidos> find(int id);
    public void update(DetallePedidos detallePedido);
    public void delete(int id);

}
