package com.sm.stock_management.component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *
 * @author Adriano
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object usuarioLogado = session.getAttribute("usuarioLogado");

        if (usuarioLogado == null && !request.getRequestURI().equals("/login")) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
