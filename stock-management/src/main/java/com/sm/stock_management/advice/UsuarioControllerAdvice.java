package com.sm.stock_management.advice;

import com.sm.stock_management.model.Usuario;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 *
 * @author Adriano
 */

@ControllerAdvice
public class UsuarioControllerAdvice {

    @ModelAttribute
    public void adicionarUsuarioNoModelo(HttpSession session, Model model) {
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado"); 
        if (usuarioLogado != null) {
            model.addAttribute("usuarioLogado", usuarioLogado);
        }
    }
}

