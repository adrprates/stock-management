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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String cadastrar(Model model, RedirectAttributes redirectAttributes) {
    List<Categoria> categorias = categoriaService.buscarTodas();
    if (categorias == null || categorias.isEmpty()) {
        redirectAttributes.addFlashAttribute("mensagem", "É preciso haver ao menos uma categoria cadastrada!");
        redirectAttributes.addFlashAttribute("tipoMensagem", "alert-danger");
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
    public String processarFormulario(Model model, RedirectAttributes redirectAttributes, @ModelAttribute Produto produto) {
        if(produto.getId() != null){
            produtoService.atualizar(produto.getId(), produto);
            redirectAttributes.addFlashAttribute("mensagem", "Produto atualizado com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "alert-success");
        } else {
            produtoService.adicionar(produto);
            redirectAttributes.addFlashAttribute("mensagem", "Produto cadastrado com sucesso!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "alert-success");
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
    public String deletar(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        Produto produto = produtoService.buscarPorId(id);
        if(produto == null){
            return "redirect:/listagem-produtos"; 
        } 
        produtoService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Produto deletado com sucesso!");
        redirectAttributes.addFlashAttribute("tipoMensagem", "alert-success");
        return "redirect:/listagem-produtos";   
    }
}