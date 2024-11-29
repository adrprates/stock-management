package com.sm.stock_management.service;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.repository.CategoriaRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adriano
 */
@Service
public class CategoriaService {
    
    @Autowired
    CategoriaRepository categoriaRepository;
    
    public Categoria adicionar(Categoria categoria){
        categoria.setId(null);
        categoriaRepository.save(categoria);
        return categoria;
    }
    
    public Categoria buscarPorId(Integer id){
        return categoriaRepository.findById(id).orElseThrow();
    }
    
    public List<Categoria> buscarTodos(){
        return categoriaRepository.findAll();
    }
    
    public List<Categoria> buscarPorNome(String nome){
        return categoriaRepository.findByNameContaining(nome);
    }
    
    public Categoria atualizar(Integer id, Categoria categoria){
        Categoria categoriaEncontrada = buscarPorId(id);
        
        categoriaEncontrada.setNome(categoria.getNome());
        
        categoriaRepository.save(categoriaEncontrada);
        return  categoriaEncontrada;
    }
    
    public void excluir(Integer id){
        Categoria categoriaEncontrada = buscarPorId(id);
        
        categoriaRepository.deleteById(categoriaEncontrada.getId());
    }
    
}
