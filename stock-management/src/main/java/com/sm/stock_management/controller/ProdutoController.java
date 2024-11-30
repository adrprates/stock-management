package com.sm.stock_management.controller;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.model.Produto;
import com.sm.stock_management.service.CategoriaService;
import com.sm.stock_management.service.ProdutoService;
import java.util.List;
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
public class ProdutoController {
    
    @Autowired
    ProdutoService produtoService;
    
    @Autowired
    CategoriaService categoriaService;

    //metodo para carregar a pagina de cadastrar produto
    @GetMapping("/cadastro-produto")
    public String cadastrar(Model model) {
    List<Categoria> categorias = categoriaService.buscarTodas();
    if (categorias == null || categorias.isEmpty()) {
        return "redirect:/listagem-produtos";
    }
    model.addAttribute("categorias", categorias);
    model.addAttribute("produto", new Produto());
    return "cadastro-produto";
}
    //metodo para salvar o produto na listagem de produtos
    //metodo usado para o cadastro de um novo produto e para a
    //atualizacao de um produto existente
    @PostMapping("/salvar-produto")
    public String processarFormulario(Model model, @ModelAttribute Produto produto) {
        if(produto.getId() != null){
            produtoService.atualizar(produto.getId(), produto);
            model.addAttribute("mensagem", "Produto atualizado com sucesso!");
        } else {
            produtoService.adicionar(produto);
            model.addAttribute("mensagem", "Produto cadastrado com sucesso!");
        }
        model.addAttribute("produto", new Produto());
        return "redirect:/listagem-produtos";
    }

    //metodo para carregar todos os produtos na pagina de listar os produtos
    @GetMapping("/listagem-produtos")
    public String listar(@RequestParam(required = false) String nome, Model model) {
        if (nome == null || nome.isBlank()) {
            model.addAttribute("produtos", produtoService.buscarTodos());
        } else {
            model.addAttribute("produtos", produtoService.buscarPorNome(nome));
        }
        return "listagem-produtos";
    }
    
    //metodo para carregar dados na pagina de atualizar produto
    @GetMapping("/atualizar-produto/{id}")
    public String atualizar(@PathVariable Integer id, Model model) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return "redirect:/listagem-produtos";
        }
        List<Categoria> categorias = categoriaService.buscarTodas();
        model.addAttribute("categorias", categorias);
        model.addAttribute("produto", produto);
        return "atualizar-produto";
    }
    
    //metodo para deletar produto
    @GetMapping("/deletar-produto/{id}")
    public String deletar(@PathVariable Integer id){
        Produto produto = produtoService.buscarPorId(id);
        if(produto == null){
            return "redirect:/listagem-produtos"; 
        } 
        categoriaService.excluir(id);
        return "redirect:/listagem-produtos";   
    }
}