package com.pwii.stock_management.service;

import com.pwii.stock_management.dto.EstoqueDto;
import com.pwii.stock_management.dto.UltimaMovimentacaoDto;
import com.pwii.stock_management.model.Estoque;

import java.util.List;


public interface EstoqueService {
    List<EstoqueDto> getAllEstoque();
    Estoque salvarEstoque(Long idBebida, int delta, String descricao);
    public UltimaMovimentacaoDto getUltimaMovimentacao(Long idBebida);
}
