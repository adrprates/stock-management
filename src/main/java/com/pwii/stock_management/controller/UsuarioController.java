package com.pwii.stock_management.controller;

import com.pwii.stock_management.model.Usuario;
import com.pwii.stock_management.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private IUserService userService;

    @GetMapping({"/user"})
    public String verHomePage(Model model) {
        model.addAttribute("listUsuarios", userService.getAllUsuarios());
        return "user/index";
    }

    @GetMapping("user/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Usuario usuario = userService.getUsuarioById(id);
        model.addAttribute("usuario", usuario);

        List<String> cargosDisponiveis = List.of("Gerente",
                "Estoquista");
        model.addAttribute("roles", cargosDisponiveis);

        return "user/editar";
    }

    @PostMapping("user/atualizar")
    public String atualizar(@ModelAttribute @Valid Usuario usuario, BindingResult resultado) {
        if (resultado.hasErrors()) {
            return "usuario/editar";
        }
        userService.saveUser(usuario);
        return "redirect:/user";
    }

    @GetMapping("user/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        userService.deletarUsuarioById(id);
        return "redirect:/user";
    }

    @GetMapping("/register")
    public String register() {
        return "user/registerUser";
    }

    @PostMapping("/saveUser")
    public String saveUser(
            @ModelAttribute Usuario usuario,
            Model model) {
        Long id = userService.saveUser(usuario);
        String message = "Usuário '" + id + "' salvo com sucesso !";
        model.addAttribute("msg", message);
        return "user/registerUser";
    }

    @GetMapping("/accessDenied")
    public String getAccessDeniedPage() {
        return "user/accessDeniedPage";
    }
}