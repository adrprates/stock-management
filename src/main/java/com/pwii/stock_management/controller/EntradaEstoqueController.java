package com.pwii.stock_management.controller;

import com.pwii.stock_management.model.EntradaEstoque;
import com.pwii.stock_management.service.BebidaService;
import com.pwii.stock_management.service.EntradaEstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/entradas-estoque")
public class EntradaEstoqueController {

    @Autowired
    private EntradaEstoqueService entradaEstoqueService;

    @Autowired
    private BebidaService bebidaService;

    @GetMapping({"", "/"})
    public String verHomePage(Model model) {
        model.addAttribute("listEntrada", entradaEstoqueService.getAllEntradasEstoque());
        return "entradas-estoque/index";
    }

    @GetMapping("/criar")
    public String criarEntrada(Model model) {
        model.addAttribute("entradaEstoque", new EntradaEstoque());
        model.addAttribute("bebidas", bebidaService.getAllBebidas());
        return "entradas-estoque/criar";
    }

    @PostMapping("/salvar")
    public String salvarEntrada(@ModelAttribute EntradaEstoque entradaEstoque, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "entradas-estoque/criar";
        }

        entradaEstoque.setDataEntrada(LocalDateTime.now());

        entradaEstoqueService.salvarEntradaEstoque(entradaEstoque);
        return "redirect:/entradas-estoque";
    }

    @GetMapping("/deletar/{id}")
    public String deletarEntrada(@PathVariable Long id) {
        entradaEstoqueService.deletarEntradaById(id);
        return "redirect:/entradas-estoque";
    }
}