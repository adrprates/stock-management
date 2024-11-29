package com.sm.stock_management.controller;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Adriano
 */
public class CategoriaController {

    @Autowired
    CategoriaService categoriaService;

    //metodo para cadastrar categoria
    @GetMapping("/cadastrar-categoria")
    public String cadastrar(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "cadastrar-categoria";
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

    //metodo para listar as categorias
    @GetMapping("/listagem-categorias")
    public String listar(Model model) {
        model.addAttribute("listaCategorias", categoriaService.buscarTodas());
        return "lista-filmes";
    }

    //metodo para atualiar categoria
    @GetMapping("/atualizar-categoria")
    public String atualizar(Model model) {
        model.addAttribute("categorias", categoriaService.buscarTodas());
        model.addAttribute("categoria", new Categoria());
        return "atualizar-filme";
    }
    
    //metodo de selecionar categoria por id
    @PostMapping("/dados-categoria")
    public String selecionar(@RequestParam("id") Integer id, Model model) {
        Categoria categoriaSelecionada = categoriaService.buscarPorId(id);
        if (categoriaSelecionada != null) {
            model.addAttribute("categoria", categoriaSelecionada); 
        } else {
            model.addAttribute("mensagem", "Categoria não encontrado.");
            model.addAttribute("categoria", new Categoria()); 
        }
        model.addAttribute("categorias", categoriaService.buscarTodas());
        return "atualizar-categoria";
    }
    
    //metodo para deletar categoria
    @PostMapping("/deletar-filme")
    public String deletar(Model model, @RequestParam("id") Integer id){
        Categoria categoriaSelecionada = categoriaService.buscarPorId(id);
        if(categoriaSelecionada!= null){
            categoriaService.excluir(id);
        } else {
            model.addAttribute("mensagem", "Categoria não encontrado.");
            model.addAttribute("categoria", new Categoria()); 
        }
        
        model.addAttribute("categorias", categoriaService.buscarTodas());
        return "redirect:/listagem-categorias";   
    }
}
