package com.sm.stock_management.controller;

import com.sm.stock_management.model.Usuario;
import com.sm.stock_management.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Adriano
 */

@Controller
public class UsuarioController {
    
    @Autowired
    UsuarioService usuarioService;

    //metodo para carregar a pagina de cadastrar usuario
    @GetMapping("/cadastro-usuario")
    public String cadastrar(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "cadastro-usuario";
    }

    //metodo para salvar o usuario na listagem de usuarios
    //metodo usado para o cadastro de um novo usuario e para a
    //atualizacao de um usuario existente
    @PostMapping("/salvar-usuario")
    public String processarFormulario(Model model, RedirectAttributes redirectAttributes, @ModelAttribute Usuario usuario) {
        if(usuario.getId() != null){
            usuarioService.atualizar(usuario.getId(), usuario);
            redirectAttributes.addFlashAttribute("mensagem", "Usuário atualizado com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "alert-success");
        } else {
            if(usuarioService.adicionar(usuario) == null){
                redirectAttributes.addFlashAttribute("mensagem", "Login já está em uso.");
                redirectAttributes.addFlashAttribute("tipoMensagem", "alert-danger");
                return "redirect:/cadastro-usuario";
            } else{
                redirectAttributes.addFlashAttribute("mensagem", "Usuário cadastrado com sucesso!");
                redirectAttributes.addFlashAttribute("tipoMensagem", "alert-success");
            }
        }
        model.addAttribute("usuario", new Usuario());
        return "redirect:/listagem-usuarios";
    }

    //metodo para carregar todos os usuarios na pagina de listar os usuarios
    @GetMapping("/listagem-usuarios")
    public String listar(@RequestParam(required = false) String nome, Model model) {
        if (nome == null || nome.isBlank()) {
            model.addAttribute("usuarios", usuarioService.buscarTodos());
        } else {
            model.addAttribute("usuarios", usuarioService.buscarPorNome(nome));
        }
        return "listagem-usuarios";
    }
    
    //metodo para carregar dados na pagina de atualizar usuario
    @GetMapping("/atualizar-usuario/{id}")
    public String atualizar(@PathVariable Integer id, Model model) {
    Usuario usuario = usuarioService.buscarPorId(id);
    if (usuario == null) {
        return "redirect:/listagem-usuarios";
    }
    model.addAttribute("usuario", usuario);
    return "atualizar-usuario";
}
    
    //metodo para deletar usuario
    @GetMapping("/deletar-usuario/{id}")
    public String deletar(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        Usuario usuario = usuarioService.buscarPorId(id);
        if(usuario == null){
            return "redirect:/listagem-usuarios"; 
        } 
        usuarioService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Usuário deletado com sucesso.");
        redirectAttributes.addFlashAttribute("tipoMensagem", "alert-success");
        return "redirect:/listagem-usuarios";   
    } 
}