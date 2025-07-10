package com.pwii.stock_management.service;

import com.pwii.stock_management.model.Ingrediente;

import java.util.List;

public interface IngredienteService {
    List<Ingrediente> getAllIngredientes();
    void salvarIngrediente(Ingrediente ingrediente);
    Ingrediente getIngredienteById(Long id);
    void deletarIngredienteById(Long id);
}
