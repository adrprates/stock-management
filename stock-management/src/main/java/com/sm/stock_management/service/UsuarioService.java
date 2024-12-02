package com.sm.stock_management.service;

import com.sm.stock_management.model.Usuario;
import com.sm.stock_management.repository.UsuarioRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adriano
 */

@Service
public class UsuarioService {
    
    @Autowired
    UsuarioRepository usuarioRepository;
    
    public Usuario adicionar(Usuario usuario){
        usuario.setId(null);
        usuarioRepository.save(usuario);
        return usuario;
    }
    
    public Usuario buscarPorId(Integer id){
        return usuarioRepository.findById(id).orElseThrow();
    }
    
    public List<Usuario> buscarTodos(){
        return usuarioRepository.findAll();
    }
    
    public List<Usuario> buscarPorNome(String nome){
        return usuarioRepository.findByNomeContaining(nome);
    }
    
    public Usuario atualizar(Integer id, Usuario usuario){
        Usuario usuarioEncontrado = buscarPorId(id);
        
        usuarioEncontrado.setNome(usuario.getNome());
        usuarioEncontrado.setLogin(usuario.getLogin());
        usuarioEncontrado.setSenha(usuario.getSenha());
        usuarioEncontrado.setCargo(usuario.getCargo());
          
        usuarioRepository.save(usuarioEncontrado);
        return usuarioEncontrado;
    }
    
    public void excluir(Integer id){
        Usuario usuarioEncontrado = buscarPorId(id);
        
        usuarioRepository.deleteById(usuarioEncontrado.getId());
    }
}