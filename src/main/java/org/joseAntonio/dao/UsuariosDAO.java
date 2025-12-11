package org.joseAntonio.dao;

import org.joseAntonio.model.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuariosDAO {
    public void create(Usuario usuario);
    public List<Usuario> getAll();
    public Optional<Usuario> find(int id);
    public void update(Usuario usuario);
    public void delete(int id);
    Optional<Usuario> encontrarPorNombreYContrasenia(String nombre, String contrasenia);
}
