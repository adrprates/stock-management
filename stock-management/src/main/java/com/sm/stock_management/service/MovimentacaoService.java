package com.sm.stock_management.service;

import com.sm.stock_management.model.Movimentacao;
import com.sm.stock_management.repository.MovimentacaoRepository;
import java.util.Date;
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
    
    public Movimentacao adicionar(Movimentacao movimentacao){
        movimentacao.setId(null);
        movimentacaoRepository.save(movimentacao);
        return movimentacao;
    }
    
    public Movimentacao buscarPorId(Integer id){
        return movimentacaoRepository.findById(id).orElseThrow();
    }
    
    public List<Movimentacao> buscarTodas(){
        return movimentacaoRepository.findAll();
    }
    
    public List<Movimentacao> buscarPorData(Date data){
        return movimentacaoRepository.findByData(data);
    }
    
    public Movimentacao atualizar(Integer id, Movimentacao movimentacao){
        Movimentacao movimentacaoEncontrada = buscarPorId(id);
        
        movimentacaoEncontrada.setQuantidade(movimentacao.getQuantidade());
        movimentacaoEncontrada.setTipo(movimentacao.getTipo());
        movimentacaoEncontrada.setData(movimentacao.getData());
        movimentacaoEncontrada.setHora(movimentacao.getHora());
        
        movimentacaoRepository.save(movimentacaoEncontrada);
        return  movimentacaoEncontrada;
    }
    
    public void excluir(Integer id){
        Movimentacao movimentacaoEncontrada = buscarPorId(id);
        
        movimentacaoRepository.deleteById(movimentacaoEncontrada.getId());
    }
}