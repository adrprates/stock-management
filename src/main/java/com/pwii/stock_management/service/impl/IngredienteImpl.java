package com.pwii.stock_management.service.impl;

import com.pwii.stock_management.model.Ingrediente;
import com.pwii.stock_management.repository.IngredienteRepository;
import com.pwii.stock_management.service.IngredienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngredienteImpl implements IngredienteService {

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Override
    public List<Ingrediente> getAllIngredientes () {
        return ingredienteRepository.findAll();
    }

    @Override
    public void salvarIngrediente(Ingrediente ingrediente) {
        ingredienteRepository.save(ingrediente);
    }

    @Override
    public Ingrediente getIngredienteById(Long id) {
        Optional<Ingrediente> optional = ingredienteRepository.findById(id);
        Ingrediente ingrediente = null;
        if(optional.isPresent()){
            ingrediente = optional.get();
        } else{
            throw new RuntimeException("Ingrediente não encontrado para o id " + id);
        }
        return ingrediente;
    }

    @Override
    public void deletarIngredienteById(Long id) {
        ingredienteRepository.deleteById(id);
    }

}
