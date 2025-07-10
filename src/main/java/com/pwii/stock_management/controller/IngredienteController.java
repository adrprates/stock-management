package com.pwii.stock_management.controller;

import com.pwii.stock_management.model.Ingrediente;
import com.pwii.stock_management.service.IngredienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ingrediente")
public class IngredienteController {

    @Autowired
    private IngredienteService ingredienteService;

    @GetMapping({"","/"})
    public String verHomePage(Model model) {
        model.addAttribute("listIngredientes", ingredienteService.getAllIngredientes());
        return "ingrediente/index";
    }

    @GetMapping("/criar")
    public String criar(Model model) {
        model.addAttribute("ingrediente", new Ingrediente());
        return "ingrediente/criar";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute @Valid Ingrediente ingrediente, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "ingrediente/criar";
        }
        ingredienteService.salvarIngrediente(ingrediente);
        return "redirect:/ingrediente";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Ingrediente ingrediente = ingredienteService.getIngredienteById(id);
        model.addAttribute("ingrediente", ingrediente);
        return "ingrediente/editar";
    }

    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute @Valid Ingrediente ingrediente, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "ingrediente/editar";
        }
        ingredienteService.salvarIngrediente(ingrediente);
        return "redirect:/ingrediente";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        ingredienteService.deletarIngredienteById(id);
        return "redirect:/ingrediente";
    }
}