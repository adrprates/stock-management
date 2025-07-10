package com.pwii.stock_management.service;

import com.pwii.stock_management.model.Bebida;

import java.util.List;

public interface BebidaService {
    List<Bebida> getAllBebidas();
    void salvarBebida(Bebida bebida);
    Bebida getBebidaById(Long id);
    void deletarBebidaById(Long id);
}
