package org.joseAntonio.model;

import java.util.Objects;

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private String imagenUrl;

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getNombre() {
        return nombre;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    public double getPrecio() {
        return precio;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
    public String getImagenUrl() {
        return imagenUrl;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Producto)) return false;

        Producto producto = (Producto) o;

        return getId() == producto.getId();
    }

    public int hashCode() {
        return Objects.hash(getId());
    }

}
