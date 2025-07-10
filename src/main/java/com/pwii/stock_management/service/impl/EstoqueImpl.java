package com.pwii.stock_management.service.impl;

import com.pwii.stock_management.dto.EstoqueDto;
import com.pwii.stock_management.dto.UltimaMovimentacaoDto;
import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.model.EntradaEstoque;
import com.pwii.stock_management.model.Estoque;
import com.pwii.stock_management.model.SaidaEstoque;
import com.pwii.stock_management.repository.BebidaRepository;
import com.pwii.stock_management.repository.EntradaEstoqueRepository;
import com.pwii.stock_management.repository.EstoqueRepository;
import com.pwii.stock_management.repository.SaidaEstoqueRepository;
import com.pwii.stock_management.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class EstoqueImpl implements EstoqueService {

    @Autowired
    EstoqueRepository estoqueRepository;

    @Autowired
    BebidaRepository bebidaRepository;

    @Autowired
    EntradaEstoqueRepository entradaEstoqueRepository;

    @Autowired
    SaidaEstoqueRepository saidaEstoqueRepository;

    @Override
    public List<EstoqueDto> getAllEstoque() {
        List<Estoque> estoques = estoqueRepository.findAll();
        List<EstoqueDto> estoqueDtos = new ArrayList<>();

        for (Estoque e : estoques) {
            Bebida bebida = bebidaRepository.findById(e.getIdBebida()).orElse(null);
            if (bebida != null) {
                EstoqueDto dto = new EstoqueDto();
                dto.setIdBebida(bebida.getId());
                dto.setNome(bebida.getNome());
                dto.setTipo(bebida.getTipo());
                dto.setVolume(bebida.getVolume());
                dto.setQuantidade(e.getQuantidade());
                dto.setDescricao(e.getDescricao());
                estoqueDtos.add(dto);
            }
        }
        return estoqueDtos;
    }

    @Override
    public Estoque salvarEstoque(Long idBebida, int delta, String descricao) {
        Estoque estoque = estoqueRepository.findById(idBebida).orElse(null);

        if (estoque == null) {
            Estoque novo = new Estoque();
            novo.setIdBebida(idBebida);
            novo.setDescricao(descricao);
            novo.setQuantidade(0);
            estoque = estoqueRepository.save(novo);
        }

        estoque.setQuantidade(estoque.getQuantidade() + delta);

        if (descricao != null && !descricao.isEmpty()) {
            estoque.setDescricao(descricao);
        }

        return estoqueRepository.save(estoque);
    }

    @Override
    public UltimaMovimentacaoDto getUltimaMovimentacao(Long idBebida) {
        Bebida bebida = bebidaRepository.findById(idBebida).orElse(null);
        if (bebida == null) {
            return null;
        }

        EntradaEstoque ultimaEntrada = entradaEstoqueRepository.findFirstByBebidaOrderByDataEntradaDesc(bebida);
        SaidaEstoque ultimaSaida = saidaEstoqueRepository.findFirstByBebidaOrderByDataSaidaDesc(bebida);

        if (ultimaEntrada == null && ultimaSaida == null) {
            return null;
        }

        if (ultimaSaida == null || (ultimaEntrada != null && ultimaEntrada.getDataEntrada().isAfter(ultimaSaida.getDataSaida()))) {
            return new UltimaMovimentacaoDto("entrada", ultimaEntrada.getId());
        } else {
            return new UltimaMovimentacaoDto("saida", ultimaSaida.getId());
        }
    }
}