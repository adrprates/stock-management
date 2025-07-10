package com.pwii.stock_management.service.impl;

import com.pwii.stock_management.dto.SaidaEstoqueDto;
import com.pwii.stock_management.dto.UltimaMovimentacaoDto;
import com.pwii.stock_management.model.*;
import com.pwii.stock_management.repository.EstoqueRepository;
import com.pwii.stock_management.repository.SaidaEstoqueRepository;
import com.pwii.stock_management.service.EstoqueService;
import com.pwii.stock_management.service.SaidaEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SaidaEstoqueImpl implements SaidaEstoqueService {

    @Autowired
    private SaidaEstoqueRepository saidaEstoqueRepository;

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private EstoqueService estoqueService;

    @Override
    public List<SaidaEstoqueDto> getAllSaidasEstoque() {
        List<SaidaEstoque> saidasEstoque = saidaEstoqueRepository.findAll();
        List<SaidaEstoqueDto> saidasEstoqueDtos = new ArrayList<>();

        for (SaidaEstoque e : saidasEstoque) {
            Bebida bebida = e.getBebida();
            Cliente cliente = e.getCliente();

            if (bebida != null && cliente != null) {
                SaidaEstoqueDto dto = new SaidaEstoqueDto();
                dto.setId(e.getId());
                dto.setIdBebida(bebida.getId());
                dto.setNome(bebida.getNome());
                dto.setTipo(bebida.getTipo());
                dto.setVolume(bebida.getVolume());
                dto.setQuantidade(e.getQuantidade());
                dto.setIdCliente(cliente.getId());
                dto.setNomeCliente(cliente.getNome());
                dto.setCpfCliente(cliente.getCpf());
                dto.setDataSaida(e.getDataSaida());

                UltimaMovimentacaoDto ultima = estoqueService.getUltimaMovimentacao(bebida.getId());

                dto.setUltimaMovimentacao(
                        ultima != null &&
                                "saida".equals(ultima.getTipo()) &&
                                e.getId().equals(ultima.getId())
                );

                saidasEstoqueDtos.add(dto);
            }
        }

        return saidasEstoqueDtos;
    }


    @Override
    public void salvarSaidaEstoque(SaidaEstoque saidaEstoque) {
        Long idBebida = saidaEstoque.getBebida().getId();

        Estoque estoque = estoqueRepository.findById(idBebida)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        if (estoque.getQuantidade() < saidaEstoque.getQuantidade()) {
            throw new RuntimeException("Quantidade insuficiente no estoque");
        }

        SaidaEstoque salvar = saidaEstoqueRepository.save(saidaEstoque);
        estoqueService.salvarEstoque(idBebida, -salvar.getQuantidade(), "Saída manual");
    }

    @Override
    public void deletarSaidaById(Long id) {
        SaidaEstoque saida = saidaEstoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Saída não encontrada"));

        Long idBebida = saida.getBebida().getId();

        UltimaMovimentacaoDto ultima = estoqueService.getUltimaMovimentacao(idBebida);

        if (ultima == null
                || !"saida".equals(ultima.getTipo())
                || !saida.getId().equals(ultima.getId())) {
            throw new RuntimeException("Apenas a última saída pode ser removida.");
        }

        saidaEstoqueRepository.deleteById(id);
        estoqueService.salvarEstoque(idBebida, saida.getQuantidade(), "Reversão saída");
    }
}