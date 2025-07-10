package com.pwii.stock_management.service.impl;

import com.pwii.stock_management.dto.EntradaEstoqueDto;
import com.pwii.stock_management.dto.UltimaMovimentacaoDto;
import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.model.EntradaEstoque;
import com.pwii.stock_management.repository.EntradaEstoqueRepository;
import com.pwii.stock_management.service.EntradaEstoqueService;
import com.pwii.stock_management.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntradaEstoqueImpl implements EntradaEstoqueService {

    @Autowired
    private EntradaEstoqueRepository entradaEstoqueRepository;

    @Autowired
    private EstoqueService estoqueService;

    @Override
    public List<EntradaEstoqueDto> getAllEntradasEstoque() {
        List<EntradaEstoque> entradasEstoque = entradaEstoqueRepository.findAll();
        List<EntradaEstoqueDto> entradasEstoqueDtos = new ArrayList<>();

        for (EntradaEstoque e : entradasEstoque) {
            Bebida bebida = e.getBebida();
            if (bebida != null) {
                EntradaEstoqueDto dto = new EntradaEstoqueDto();
                dto.setId(e.getId());
                dto.setIdBebida(bebida.getId());
                dto.setNome(bebida.getNome());
                dto.setTipo(bebida.getTipo());
                dto.setVolume(bebida.getVolume());
                dto.setQuantidade(e.getQuantidade());
                dto.setDataEntrada(e.getDataEntrada());

                UltimaMovimentacaoDto ultima = estoqueService.getUltimaMovimentacao(bebida.getId());

                dto.setUltimaMovimentacao(
                        ultima != null &&
                                "entrada".equals(ultima.getTipo()) &&
                                e.getId().equals(ultima.getId())
                );

                entradasEstoqueDtos.add(dto);
            }
        }
        return entradasEstoqueDtos;
    }


    @Override
    public void salvarEntradaEstoque(EntradaEstoque entradaEstoque) {
        EntradaEstoque salvar = entradaEstoqueRepository.save(entradaEstoque);
        estoqueService.salvarEstoque(
                salvar.getBebida().getId(),
                salvar.getQuantidade(),
                "Entrada manual"
        );
    }

    @Override
    public void deletarEntradaById(Long id) {
        EntradaEstoque entrada = entradaEstoqueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrada não encontrada"));

        Long idBebida = entrada.getBebida().getId();

        UltimaMovimentacaoDto ultima = estoqueService.getUltimaMovimentacao(idBebida);

        if (ultima == null
                || !"entrada".equals(ultima.getTipo())
                || !entrada.getId().equals(ultima.getId())) {
            throw new RuntimeException("Apenas a última entrada pode ser removida.");
        }

        entradaEstoqueRepository.deleteById(id);
        estoqueService.salvarEstoque(idBebida, -entrada.getQuantidade(), "Reversão entrada");
    }

}