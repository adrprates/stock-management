package com.sm.stock_management.service;

import com.sm.stock_management.model.Produto;
import com.sm.stock_management.repository.ProdutoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adriano
 */

@Service
public class ProdutoService {
    
    @Autowired
    ProdutoRepository produtoRepository;
    
    public Produto adicionar(Produto produto){
        produto.setId(null);
        produto.setQuantidade(0);
        produtoRepository.save(produto);
        return produto;
    }
    
    public Produto buscarPorId(Integer id){
        return produtoRepository.findById(id).orElseThrow();
    }
    
    public List<Produto> buscarTodos(){
        return produtoRepository.findAll();
    }
    
    public List<Produto> buscarPorNome(String nome){
        return produtoRepository.findByNomeContaining(nome);
    }
    
    public Produto atualizar(Integer id, Produto produto){
        Produto produtoEncontrado = buscarPorId(id);
        
        produtoEncontrado.setNome(produto.getNome());
        produtoEncontrado.setCategoria(produto.getCategoria());
        produtoEncontrado.setPreco(produto.getPreco());
        produtoEncontrado.setDescricao(produto.getDescricao());
        
        produtoRepository.save(produtoEncontrado);
        return  produtoEncontrado;
    }
    
    public void excluir(Integer id){
        Produto produtoEncontrado = buscarPorId(id);
        
        produtoRepository.deleteById(produtoEncontrado.getId());

    }
    
}