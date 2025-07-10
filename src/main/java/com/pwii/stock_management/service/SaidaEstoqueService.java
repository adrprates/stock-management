package com.pwii.stock_management.service;

import com.pwii.stock_management.dto.SaidaEstoqueDto;
import com.pwii.stock_management.model.SaidaEstoque;

import java.util.List;

public interface SaidaEstoqueService {
    List<SaidaEstoqueDto> getAllSaidasEstoque();
    void salvarSaidaEstoque(SaidaEstoque saidaEstoque);
    void deletarSaidaById(Long id);
}
