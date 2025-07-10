package com.pwii.stock_management.controller;

import com.pwii.stock_management.model.SaidaEstoque;
import com.pwii.stock_management.service.BebidaService;
import com.pwii.stock_management.service.ClienteService;
import com.pwii.stock_management.service.SaidaEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/saidas-estoque")
public class SaidaEstoqueController {

    @Autowired
    private SaidaEstoqueService saidaEstoqueService;

    @Autowired
    private BebidaService bebidaService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping({"", "/"})
    public String verHomePage(Model model) {
        model.addAttribute("listSaida", saidaEstoqueService.getAllSaidasEstoque());
        return "saidas-estoque/index";
    }

    @GetMapping("/criar")
    public String criarSaida(Model model) {
        model.addAttribute("saidaEstoque", new SaidaEstoque());
        model.addAttribute("bebidas", bebidaService.getAllBebidas());
        model.addAttribute("clientes", clienteService.getAllClientes());
        return "saidas-estoque/criar";
    }

    @PostMapping("/salvar")
    public String salvarSaida(@ModelAttribute SaidaEstoque saidaEstoque, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "saidas-estoque/criar";
        }

        saidaEstoque.setDataSaida(LocalDateTime.now());
        saidaEstoqueService.salvarSaidaEstoque(saidaEstoque);
        return "redirect:/saidas-estoque";
    }

    @GetMapping("/deletar/{id}")
    public String deletarSaida(@PathVariable Long id) {
        saidaEstoqueService.deletarSaidaById(id);
        return "redirect:/saidas-estoque";
    }
}