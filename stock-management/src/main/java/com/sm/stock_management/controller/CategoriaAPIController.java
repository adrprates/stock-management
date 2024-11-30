package com.sm.stock_management.controller;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.service.CategoriaService;
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
@RequestMapping("/categoria")
public class CategoriaAPIController {
    
    @Autowired
    private CategoriaService categoriaService;
    
    //adicionar nova categoria
    @PostMapping("/adicionar")
    public ResponseEntity<Categoria> adicionarCategoria(@RequestBody Categoria categoria) {
        Categoria novaCategoria = categoriaService.adicionar(categoria);
        return new ResponseEntity<>(novaCategoria, HttpStatus.CREATED);
    }
    
    //buscar categoria por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Categoria> buscarPorId(@PathVariable Integer id) {
        Categoria categoriaEncontrada = categoriaService.buscarPorId(id);
        if (categoriaEncontrada == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoriaEncontrada, HttpStatus.OK);
    }
    
    //buscar todas as categorias
    @GetMapping("/buscarTodos")
    public ResponseEntity<List<Categoria>> buscarTodas() {
        List<Categoria> categorias = categoriaService.buscarTodas();
        return new ResponseEntity<>(categorias, HttpStatus.OK);
    }
    
    //atualizar categoria existente
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Categoria> atualizarCategoria(@PathVariable Integer id, @RequestBody Categoria categoria) {
        Categoria categoriaAtualizada = categoriaService.atualizar(id, categoria);
        if (categoriaAtualizada == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categoriaAtualizada, HttpStatus.OK);
    }
    
    //deletar categoria
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        if (categoria == null) {
            return ResponseEntity.notFound().build();
        }
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}