package com.sm.stock_management.controller;

import com.sm.stock_management.model.Produto;
import com.sm.stock_management.service.ProdutoService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Adriano
 */

@RestController
@RequestMapping("/produto")
public class ProdutoAPIController {
    
    @Autowired
    private ProdutoService produtoService;
    
    //adicionar novo produto
    @PostMapping("/adicionar")
    public ResponseEntity<Produto> adicionarProduto(@RequestBody Produto produto) {
        Produto novoProduto = produtoService.adicionar(produto);
        return new ResponseEntity<>(novoProduto, HttpStatus.CREATED);
    }
    
    //buscar produto por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Produto> buscarPorId(@PathVariable Integer id) {
        Produto produtoEncontrado = produtoService.buscarPorId(id);
        if (produtoEncontrado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(produtoEncontrado, HttpStatus.OK);
    }
    
    //buscar todas os produtos
    @GetMapping("/buscarTodos")
    public ResponseEntity<List<Produto>> buscarTodos() {
        List<Produto> produtos = produtoService.buscarTodos();
        return new ResponseEntity<>(produtos, HttpStatus.OK);
    }
    
    //atualizar produto existente
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Integer id, @RequestBody Produto produto) {
        Produto produtoAtualizado = produtoService.atualizar(id, produto);
        if (produtoAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(produtoAtualizado, HttpStatus.OK);
    }
    
    //deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return ResponseEntity.notFound().build();
        }
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
