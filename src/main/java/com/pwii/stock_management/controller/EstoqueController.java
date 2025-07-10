package com.pwii.stock_management.controller;

import com.pwii.stock_management.dto.UltimaMovimentacaoDto;
import com.pwii.stock_management.service.BebidaService;
import com.pwii.stock_management.service.EstoqueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private BebidaService bebidaService;

    @GetMapping({"","/"})
    public String verHomePage(Model model) {
        model.addAttribute("listEstoque", estoqueService.getAllEstoque());
        return "estoque/index";
    }
}