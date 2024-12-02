package com.sm.stock_management.controller;

import com.sm.stock_management.model.Usuario;
import com.sm.stock_management.service.UsuarioService;
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
@RequestMapping("/usuario")
public class UsuarioAPIController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    //adicionar novo usuario
    @PostMapping("/adicionar")
    public ResponseEntity<Usuario> adicionarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioService.adicionar(usuario);
        return new ResponseEntity<>(novoUsuario, HttpStatus.CREATED);
    }
    
    //buscar usuario por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Integer id) {
        Usuario usuarioEncontrado = usuarioService.buscarPorId(id);
        if (usuarioEncontrado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuarioEncontrado, HttpStatus.OK);
    }
    
    //buscar todos os usuarios
    @GetMapping("/buscarTodos")
    public ResponseEntity<List<Usuario>> buscarTodos() {
        List<Usuario> usuarios = usuarioService.buscarTodos();
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }
    
    //atualizar usuario existente
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Usuario> atualizarProduto(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Usuario usuarioAtualizado = usuarioService.atualizar(id, usuario);
        if (usuarioAtualizado == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(usuarioAtualizado, HttpStatus.OK);
    }
    
    //deletar produto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        Usuario usuario = usuarioService.buscarPorId(id);
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}