package com.sm.stock_management.controller;

import com.sm.stock_management.model.Movimentacao;
import com.sm.stock_management.model.Produto;
import com.sm.stock_management.service.MovimentacaoService;
import com.sm.stock_management.service.ProdutoService;
import java.util.Date;
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
public class MovimentacaoController {
    
    @Autowired
    MovimentacaoService movimentacaoService;
    
    @Autowired
    ProdutoService produtoService;
    
    //metodo para carregar a pagina de realizar uma nova movimentacao
    @GetMapping("/movimentacao-produto/{id}")
    public String nova(@PathVariable Integer id, Model model) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return "redirect:/listagem-produtos";
        }
        model.addAttribute("produto", new Produto());
        model.addAttribute("movimentacao", new Movimentacao());
        return "movimentacao-produto";
    }
    
    //metodo para salvar a movimentacao na listagem de movimentacoes de um produto
    //metodo usado para realizar uma nova movimentacao e para a
    //atualizar uma movimentacao existente
    @PostMapping("/salvar-movimentacao/{id}")
    public String processarFormulario(@PathVariable Integer id, Model model, @ModelAttribute Movimentacao movimentacao) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return "redirect:/listagem-produtos";
        }
        if(movimentacao.getId() != null){
            movimentacaoService.atualizar(movimentacao.getId(), movimentacao);
            model.addAttribute("mensagem", "Movimentação atualizada com sucesso!");
        } else {
            movimentacaoService.adicionar(movimentacao);
            model.addAttribute("mensagem", "Movimentação realizada com sucesso!");
        }
        model.addAttribute("movimentacao", new Movimentacao());
        return "redirect:/listagem-movimentacao-produto";
    }

    //metodo para carregar todas as movimnetacoes do produto acessado na pagina de listar movimentacoes
    @GetMapping("/listagem-movimentacao-produto/{id}")
    public String listar(@RequestParam(required = false) Date data, @PathVariable Integer id, Model model) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return "redirect:/listagem-produtos";
        }
        model.addAttribute("produto", produto);
        if (data == null) {
            model.addAttribute("movimentacoes", movimentacaoService.buscarTodas(produto));
        } else {
            model.addAttribute("movimentacoes", movimentacaoService.buscarPorData(data, produto));
        }
        return "listagem-movimentacao-produto";
    }
    
    //metodo para carregar dados na pagina de atualizar movimentacao
    @GetMapping("/atualizar-produto/{id_produto}/{id_movimentacao}")
    public String atualizar(@PathVariable Integer idProduto, @PathVariable Integer idMovimentacao, Model model) {
        Produto produto = produtoService.buscarPorId(idProduto);
        Movimentacao movimentacao = movimentacaoService.buscarPorId(idMovimentacao);
        if (produto == null) {
            return "redirect:/listagem-produtos";
        } else if (movimentacao == null){
            return "redirect:/listagem-movimentacao-produto";
        }
        model.addAttribute("movimentacao", movimentacao);
        return "atualizar-movimentacao-produto";
    }
    
    //metodo para deletar movimentacao
    @GetMapping("/deletar-movimentacao/{id}")
    public String deletar(@PathVariable Integer id){
        Movimentacao movimentacao = movimentacaoService.buscarPorId(id);
        if(movimentacao == null){
            return "redirect:/listagem-movimentacao-produto"; 
        } 
        movimentacaoService.excluir(id);
        return "redirect:/listagem-movimentacao-produto";   
    }
}