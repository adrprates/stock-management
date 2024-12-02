package com.sm.stock_management.controller;

import com.sm.stock_management.model.Movimentacao;
import com.sm.stock_management.model.Produto;
import com.sm.stock_management.service.MovimentacaoService;
import com.sm.stock_management.service.ProdutoService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Adriano
 */

@RestController
@RequestMapping("/movimentacao")
public class MovimentacaoAPIController {
    
    @Autowired
    private MovimentacaoService movimentacaoService;
    
     @Autowired
    private ProdutoService produtoService;

    //adicionar nova movimentacao
    @PostMapping("/adicionar")
    public ResponseEntity<Movimentacao> adicionarMovimentacao(@RequestBody Movimentacao movimentacao) {
        Produto produto = produtoService.buscarPorId(movimentacao.getProduto().getId());
        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        movimentacao.setProduto(produto);
        Movimentacao novaMovimentacao = movimentacaoService.adicionar(movimentacao);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaMovimentacao);
    }

    //buscar movimentacao por ID
    @GetMapping("/{id}")
    public ResponseEntity<Movimentacao> buscarPorId(@PathVariable Integer id) {
        Movimentacao movimentacao = movimentacaoService.buscarPorId(id);
        if (movimentacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(movimentacao);
    }

    //lisstar todas as movimentacoes de um produto
    @GetMapping("/produto/{id}")
    public ResponseEntity<List<Movimentacao>> listarPorProduto(
            @PathVariable Integer id,
            @RequestParam(required = false) String data) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Movimentacao> movimentacoes;
        if (data == null || data.isEmpty()) {
            movimentacoes = movimentacaoService.buscarTodas(produto);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataAtual = LocalDate.parse(data, formatter);
            movimentacoes = movimentacaoService.buscarPorData(dataAtual, produto);
        }
        return ResponseEntity.ok(movimentacoes);
    }

    //atualizar movimentacao
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Movimentacao> atualizarMovimentacao(
            @PathVariable Integer id,
            @RequestBody Movimentacao movimentacao) {
        Movimentacao movimentacaoExistente = movimentacaoService.buscarPorId(id);
        if (movimentacaoExistente == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Produto produto = produtoService.buscarPorId(movimentacao.getProduto().getId());
        if (produto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        movimentacao.setProduto(produto);
        Movimentacao movimentacaoAtualizada = movimentacaoService.atualizar(id, movimentacao);
        return ResponseEntity.ok(movimentacaoAtualizada);
    }

    //deletar movimentacao
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<Void> deletarMovimentacao(@PathVariable Integer id) {
        Movimentacao movimentacao = movimentacaoService.buscarPorId(id);
        if (movimentacao == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        movimentacaoService.excluir(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}