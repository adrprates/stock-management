package com.sm.stock_management.service;

import com.sm.stock_management.model.Movimentacao;
import com.sm.stock_management.model.Produto;
import com.sm.stock_management.repository.MovimentacaoRepository;
import com.sm.stock_management.repository.ProdutoRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Adriano
 */

@Service
public class MovimentacaoService {
    
    @Autowired
    MovimentacaoRepository movimentacaoRepository;
    
    @Autowired
    ProdutoRepository produtoRepository;
    
    public Movimentacao adicionar(Movimentacao movimentacao){
        if(aplicarMovimentacao(movimentacao) == true){
            movimentacao.setId(null);
            movimentacaoRepository.save(movimentacao);
            return movimentacao;
        } else {
            return null;
        }
    }
    
    public Movimentacao buscarPorId(Integer id){
        return movimentacaoRepository.findById(id).orElseThrow();
    }
    
    public List<Movimentacao> buscarTodas(Produto produto){
        return movimentacaoRepository.findByProduto(produto);
    }
    
    public List<Movimentacao> buscarPorData(LocalDate data, Produto produto){
        return movimentacaoRepository.findByDataAndProduto(data, produto);
    }
    
    public Movimentacao obterUltimaMovimentacao() {
        return movimentacaoRepository.findTopByOrderByDataDescHoraDesc();
    }
    
    public boolean aplicarMovimentacao(Movimentacao movimentacao){
        Produto produto = movimentacao.getProduto();
        if(movimentacao.getTipo().equalsIgnoreCase("Inclusao")){
            produto.setQuantidade(produto.getQuantidade() + movimentacao.getQuantidade());
        } else{
            int resultado = produto.getQuantidade() - movimentacao.getQuantidade();
            if(resultado >= 0){
                produto.setQuantidade(produto.getQuantidade() - movimentacao.getQuantidade());
            } else {
                return false;
            }
        }
        produtoRepository.save(produto);
        return true;
    }
    
    public Movimentacao atualizar(Integer id, Movimentacao movimentacao){
        Movimentacao movimentacaoEncontrada = buscarPorId(id);
        
        movimentacaoEncontrada.setQuantidade(movimentacao.getQuantidade());
        movimentacaoEncontrada.setTipo(movimentacao.getTipo());
        movimentacaoEncontrada.setData(movimentacao.getData());
        movimentacaoEncontrada.setHora(movimentacao.getHora());
                
        if(aplicarMovimentacao(movimentacaoEncontrada) == true){
            movimentacaoRepository.save(movimentacaoEncontrada);
            return  movimentacaoEncontrada;
        } else{
            return null;
        }
    }
    
    public void excluir(Integer id){
        Movimentacao movimentacaoEncontrada = buscarPorId(id);
        
        movimentacaoRepository.deleteById(movimentacaoEncontrada.getId());
    }
}
