package com.pwii.stock_management.controller;

import com.pwii.stock_management.model.Cliente;
import com.pwii.stock_management.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping({"","/"})
    public String verHomePage(Model model) {
        model.addAttribute("listClientes", clienteService.getAllClientes());
        return "cliente/index";
    }

    @GetMapping("/criar")
    public String criar(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "cliente/criar";
    }

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute @Valid Cliente cliente, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "cliente/criar";
        }
        clienteService.salvarCliente(cliente);
        return "redirect:/cliente";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.getClienteById(id);
        model.addAttribute("cliente", cliente);
        return "cliente/editar";
    }

    @PostMapping("/atualizar")
    public String atualizar(@ModelAttribute @Valid Cliente cliente, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "cliente/editar";
        }
        clienteService.salvarCliente(cliente);
        return "redirect:/cliente";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        clienteService.deletarClienteById(id);
        return "redirect:/cliente";
    }
}
