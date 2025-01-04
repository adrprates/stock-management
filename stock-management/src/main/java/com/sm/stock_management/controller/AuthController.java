package com.sm.stock_management.controller;

import com.sm.stock_management.model.Usuario;
import com.sm.stock_management.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Adriano
 */

@Controller
public class AuthController {
    
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/login")
    public String paginaLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String senha, HttpSession session, RedirectAttributes redirectAttributes) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(email);
        if (usuarioLogado != null && usuarioLogado.getSenha().equals(senha)) {
            session.setAttribute("usuarioLogado", usuarioLogado);
            return "redirect:/listagem-produtos";
        } else {
            redirectAttributes.addFlashAttribute("mensagem", "E-mail e/ou senha inválidos!");
            redirectAttributes.addFlashAttribute("tipoMensagem", "alert-danger");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}

