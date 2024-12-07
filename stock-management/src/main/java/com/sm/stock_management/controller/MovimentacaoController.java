package com.sm.stock_management.controller;

import com.sm.stock_management.model.Movimentacao;
import com.sm.stock_management.model.Produto;
import com.sm.stock_management.service.MovimentacaoService;
import com.sm.stock_management.service.ProdutoService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
        model.addAttribute("produto", produto);
        model.addAttribute("movimentacao", new Movimentacao());
        return "movimentacao-produto";
    }
    
    //metodo para salvar a movimentacao na listagem de movimentacoes de um produto
    //metodo usado para realizar uma nova movimentacao e para a
    //atualizar uma movimentacao existente
    @PostMapping("/salvar-movimentacao")
    public String processarFormulario(Model model, RedirectAttributes redirectAttributes, @ModelAttribute Movimentacao movimentacao) {
        Produto produto = produtoService.buscarPorId(movimentacao.getProduto().getId());
        if (produto == null) {
            return "redirect:/listagem-produtos";
        }
        movimentacao.setProduto(produto);
    
        if (movimentacao.getId() != null) {
            if(movimentacaoService.atualizar(movimentacao.getId(), movimentacao) == null){
                redirectAttributes.addFlashAttribute("mensagem", "Erro: Quantidade insuficiente de produtos para serem retirados!");
                redirectAttributes.addFlashAttribute("tipoMensagem", "alert-danger");
            } else{
                redirectAttributes.addFlashAttribute("mensagem", "Movimentação atualizada com sucesso!");
                redirectAttributes.addFlashAttribute("tipoMensagem", "alert-success");
            }
        } else {
            if(movimentacaoService.adicionar(movimentacao) == null){
                redirectAttributes.addFlashAttribute("mensagem", "Erro: Quantidade insuficiente de produtos para serem retirados!");
                redirectAttributes.addFlashAttribute("tipoMensagem", "alert-danger");
            } else{
                redirectAttributes.addFlashAttribute("mensagem", "Movimentação realizada com sucesso!");
                redirectAttributes.addFlashAttribute("tipoMensagem", "alert-success");
            }
        }
        model.addAttribute("movimentacao", new Movimentacao());
        return "redirect:/listagem-movimentacao-produto/" + movimentacao.getProduto().getId();
    }

    //metodo para carregar todas as movimentacoes do produto acessado na pagina de listar movimentacoes
    @GetMapping("/listagem-movimentacao-produto/{id}")
    public String listar(@RequestParam(required = false) String data, @PathVariable Integer id, Model model) {
        Produto produto = produtoService.buscarPorId(id);
        if (produto == null) {
            return "redirect:/listagem-produtos";
        }
        model.addAttribute("produto", produto);

        List<Movimentacao> movimentacoes;
        if (data == null || data.isEmpty()) {
            movimentacoes = movimentacaoService.buscarTodas(produto);
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dataAtual = LocalDate.parse(data, formatter);            
            movimentacoes = movimentacaoService.buscarPorData(dataAtual, produto);
        }

        Movimentacao ultimaMovimentacao = movimentacaoService.obterUltimaMovimentacao();
        model.addAttribute("movimentacoes", movimentacoes);
        model.addAttribute("ultimaMovimentacao", ultimaMovimentacao);

        return "listagem-movimentacao-produto";
    }
    
    //metodo para carregar dados na pagina de atualizar movimentacao
    @GetMapping("/atualizar-produto/{id_produto}/{id_movimentacao}")
    public String atualizar(@PathVariable Integer id_produto, @PathVariable Integer id_movimentacao, Model model) {
        Produto produto = produtoService.buscarPorId(id_produto);
        Movimentacao movimentacao = movimentacaoService.buscarPorId(id_movimentacao);
        if (produto == null) {
            return "redirect:/listagem-produtos";
        }
        model.addAttribute("produto", produto);
        if (movimentacao == null){
            return "redirect:/listagem-movimentacao-produto";
        }
        model.addAttribute("movimentacao", movimentacao);
        return "atualizar-movimentacao-produto";
    }
    
    //metodo para deletar movimentacao
    @GetMapping("/deletar-movimentacao/{id}")
    public String deletar(@PathVariable Integer id, RedirectAttributes redirectAttributes){
        Movimentacao movimentacao = movimentacaoService.buscarPorId(id);
        int produtoId = movimentacao.getProduto().getId();
        if(movimentacao == null){
            return "redirect:/listagem-movimentacao-produto/" + produtoId; 
        } 
        movimentacaoService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagem", "Movimentação deletada com sucesso!");
        redirectAttributes.addFlashAttribute("tipoMensagem", "alert-success");
        return "redirect:/listagem-movimentacao-produto/" + produtoId;   
    }
}