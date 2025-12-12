package org.joseAntonio.dao;

import org.joseAntonio.model.Pedido;

import java.util.List;
import java.util.Optional;

public interface PedidoDAO {

    public int create(Pedido pedido);
    public List<Pedido> getAll();
    public Optional<Pedido> find(int id);
    public void update(Pedido pedido);
    public void delete(int id);
    public List<Pedido> findAllPedidos();
    public List<Pedido> findPedidosByUsuarioId(int usuarioId);
    public Pedido findById(int id);
}