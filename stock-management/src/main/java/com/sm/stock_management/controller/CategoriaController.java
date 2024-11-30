package com.sm.stock_management.controller;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Adriano
 */
@Controller
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    //metodo para carregar a pagina de cadastrar categoria
    @GetMapping("/cadastro-categoria")
    public String cadastrar(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "cadastro-categoria";
    }

    //metodo para salvar a categoria na listagem de categorias
    //metodo usando para o cadastro de uma nova categoria e para a
    //atualizacao de uma categoria existente
    @PostMapping("/salvar-categoria")
    public String processarFormulario(Model model, @ModelAttribute Categoria categoria) {
        if(categoria.getId() != null){
            categoriaService.atualizar(categoria.getId(), categoria);
            model.addAttribute("mensagem", "Categoria atualizada com sucesso!");
        } else {
            categoriaService.adicionar(categoria);
            model.addAttribute("mensagem", "Categoria cadastrado com sucesso!");
        }
        model.addAttribute("categoria", new Categoria());
        return "redirect:/listagem-categorias";
    }

    //metodo para carregar todas as categorias na pagina de listar as categorias
    @GetMapping("/listagem-categorias")
    public String listar(@RequestParam(required = false) String nome, Model model) {
        if (nome == null || nome.isBlank()) {
            model.addAttribute("categorias", categoriaService.buscarTodas());
        } else {
            model.addAttribute("categorias", categoriaService.buscarPorNome(nome));
        }
        return "listagem-categorias";
    }
    
    //metodo para carregar dados na pagina de atualizar categoria
    @GetMapping("/atualizar-categoria/{id}")
    public String atualizar(@PathVariable Integer id, Model model) {
    Categoria categoria = categoriaService.buscarPorId(id);
    if (categoria == null) {
        return "redirect:/listagem-categorias";
    }
    model.addAttribute("categoria", categoria);
    return "atualizar-categoria";
}
    
    //metodo para deletar categoria
    @GetMapping("/deletar-categoria/{id}")
    public String deletar(@PathVariable Integer id){
        Categoria categoria = categoriaService.buscarPorId(id);
        if(categoria == null){
            return "redirect:/listagem-categorias"; 
        } 
        categoriaService.excluir(id);
        return "redirect:/listagem-categorias";   
    }
}