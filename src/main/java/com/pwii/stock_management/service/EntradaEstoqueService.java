package com.pwii.stock_management.service;

import com.pwii.stock_management.dto.EntradaEstoqueDto;
import com.pwii.stock_management.model.EntradaEstoque;

import java.util.List;

public interface EntradaEstoqueService {
    List<EntradaEstoqueDto> getAllEntradasEstoque();
    void salvarEntradaEstoque(EntradaEstoque entradaEstoque);
    void deletarEntradaById(Long id);
}
