package com.pwii.stock_management.service.impl;

import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.repository.BebidaRepository;
import com.pwii.stock_management.service.BebidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BebidaImpl implements BebidaService {

    @Autowired
    private BebidaRepository bebidaRepository;

    @Override
    public List<Bebida> getAllBebidas() {
        return bebidaRepository.findAll();
    }

    @Override
    public void salvarBebida(Bebida bebida) {
        bebidaRepository.save(bebida);
    }

    @Override
    public Bebida getBebidaById(Long id) {
        Optional<Bebida> optional = bebidaRepository.findById(id);
        Bebida bebida = null;
        if(optional.isPresent()){
            bebida = optional.get();
        } else{
            throw new RuntimeException("Bebida não encontrada para o id " + id);
        }
        return bebida;
    }

    @Override
    public void deletarBebidaById(Long id) {
        bebidaRepository.deleteById(id);
    }
}
