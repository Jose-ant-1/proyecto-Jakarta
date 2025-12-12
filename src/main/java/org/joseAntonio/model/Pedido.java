package org.joseAntonio.model;

import java.util.List;

public class Pedido {

    private int id;
    private int usuarioId;
    private double importe;
    private List<DetallePedidos> detalles;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }
    public int getUsuarioId() {
        return usuarioId;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }
    public double getImporte() {
        return importe;
    }

    public List<DetallePedidos> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedidos> detalles) {
        this.detalles = detalles;
    }
}
