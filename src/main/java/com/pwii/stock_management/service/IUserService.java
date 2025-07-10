package com.pwii.stock_management.service;

import com.pwii.stock_management.model.Cliente;
import com.pwii.stock_management.model.Usuario;

import java.util.List;

public interface IUserService {
    public Long saveUser(Usuario usuario);
    List<Usuario> getAllUsuarios();
    Usuario getUsuarioById(Long id);
    void deletarUsuarioById(Long id);
}
