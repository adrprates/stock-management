package com.pwii.stock_management.service.impl;

import com.pwii.stock_management.dto.ReceitaBebidaDto;
import com.pwii.stock_management.dto.ReceitaBebidaFormDto;
import com.pwii.stock_management.dto.ReceitaBebidaViewDto;
import com.pwii.stock_management.model.*;
import com.pwii.stock_management.repository.BebidaRepository;
import com.pwii.stock_management.repository.IngredienteRepository;
import com.pwii.stock_management.repository.ReceitaBebidaRepository;
import com.pwii.stock_management.service.ReceitaBebidaService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceitaBebidaImpl implements ReceitaBebidaService {

    @Autowired
    ReceitaBebidaRepository receitaBebidaRepository;

    @Autowired
    BebidaRepository bebidaRepository;

    @Autowired
    IngredienteRepository ingredienteRepository;

    @Override
    public List<ReceitaBebidaViewDto> getAllReceitaBebidas() {
        List<ReceitaBebida> receitas = receitaBebidaRepository.findAll();
        List<ReceitaBebidaViewDto> receitaDtos = new ArrayList<>();

        for (ReceitaBebida r : receitas) {
            Bebida bebida = bebidaRepository.findById(r.getId().getIdBebida()).orElse(null);
            Ingrediente ingrediente = ingredienteRepository.findById(r.getId().getIdIngrediente()).orElse(null);

            if (bebida != null && ingrediente != null) {
                ReceitaBebidaViewDto dto = new ReceitaBebidaViewDto();
                dto.setIdBebida(bebida.getId());
                dto.setIdIngrediente(ingrediente.getId());
                dto.setQuantidade(r.getQuantidade());
                dto.setUnidade_medida(r.getUnidadeMedida());
                dto.setBebida(bebida);
                dto.setIngrediente(ingrediente);
                receitaDtos.add(dto);
            }
        }

        return receitaDtos;
    }

    @Override
    public ReceitaBebidaDto getReceitaBebidaById(ReceitaBebidaId id) {
        ReceitaBebida receita = receitaBebidaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Receita não encontrada para o ID: " + id));

        ReceitaBebidaDto dto = new ReceitaBebidaDto();
        dto.setIdBebida(receita.getId().getIdBebida());
        dto.setIdIngrediente(receita.getId().getIdIngrediente());
        dto.setQuantidade(receita.getQuantidade());
        dto.setUnidadeMedida(receita.getUnidadeMedida());

        return dto;
    }


    @Override
    public void salvarReceitas(ReceitaBebidaFormDto form) {
        for (ReceitaBebidaDto dto : form.getIngredientes()) {
            if (dto.getIdIngrediente() != null) {
                ReceitaBebida receita = new ReceitaBebida();

                ReceitaBebidaId id = new ReceitaBebidaId();
                id.setIdBebida(form.getIdBebida());
                id.setIdIngrediente(dto.getIdIngrediente());
                receita.setId(id);

                receita.setQuantidade(dto.getQuantidade());
                receita.setUnidadeMedida(dto.getUnidadeMedida());

                Bebida bebida = bebidaRepository.findById(form.getIdBebida())
                        .orElseThrow(() -> new EntityNotFoundException("Bebida não encontrada"));
                Ingrediente ingrediente = ingredienteRepository.findById(dto.getIdIngrediente())
                        .orElseThrow(() -> new EntityNotFoundException("Ingrediente não encontrado"));

                receita.setBebida(bebida);
                receita.setIngrediente(ingrediente);

                receitaBebidaRepository.save(receita);
            }
        }
    }

    @Override
    @Transactional
    public void deletarReceitaByIdBebida(Long idBebida) {
        receitaBebidaRepository.deleteById_IdBebida(idBebida);
    }
}
