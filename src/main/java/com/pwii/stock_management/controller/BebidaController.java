package com.pwii.stock_management.controller;

import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.service.BebidaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bebida")
public class BebidaController {

    @Autowired
    private BebidaService bebidaService;

    @GetMapping({"","/"})
    public String verHomePage(Model model) {
        model.addAttribute("listBebidas", bebidaService.getAllBebidas());
        return "bebida/index";
    }

    @GetMapping("/criar")
    public String criar(Model model) {
        model.addAttribute("bebida", new Bebida());
        return "bebida/criar";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute @Valid Bebida bebida, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "bebida/criar";
        }
        bebidaService.salvarBebida(bebida);
        return "redirect:/bebida";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Bebida bebida = bebidaService.getBebidaById(id);
        model.addAttribute("bebida", bebida);
        return "bebida/editar";
    }

    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute @Valid Bebida bebida, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "bebida/editar";
        }
        bebidaService.salvarBebida(bebida);
        return "redirect:/bebida";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        bebidaService.deletarBebidaById(id);
        return "redirect:/bebida";
    }
}
