package com.sm.stock_management.component;

import com.sm.stock_management.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 *
 * @author Adriano
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    private static final Map<String, List<String>> permissoes = new HashMap<>();

    static {
        List<String> gerentePermissoes = Arrays.asList(
                "/atualizar-categoria", "/atualizar-produto", "/atualizar-usuario", "/salvar-produto", "/salvar-movimentacao",
                "/cadastro-categoria", "/cadastro-produto", "/cadastro-usuario", "/deletar-categoria", "/deletar-produto",
                "/listagem-categorias", "/listagem-movimentacao-produto", "/listagem-produtos", "/listagem-usuarios",
                "/movimentacao-produto", "/salvar-categoria", "/salvar-usuario", "/deletar-usuario", "/deletar-movimentacao"
        );

        List<String> estoquistaPermissoes = new ArrayList<>(gerentePermissoes);
        estoquistaPermissoes.removeAll(Arrays.asList("/atualizar-usuario", "/cadastro-usuario", "/listagem-usuarios", "/salvar-usuario", "/deletar-usuario"));

        List<String> compradorVendedorPermissoes = Arrays.asList("/listagem-produtos");

        permissoes.put("Gerente", gerentePermissoes);
        permissoes.put("Estoquista", estoquistaPermissoes);
        permissoes.put("Comprador", compradorVendedorPermissoes);
        permissoes.put("Vendedor", compradorVendedorPermissoes);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("usuarioLogado") == null) {
            response.sendRedirect("/login");
            return false;
        }

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        Integer usuarioId = usuario.getId();

        if (usuarioId == null) {
            response.sendRedirect("/login");
            return false;
        }

        String cargo = usuario.getCargo();
        String requestURI = request.getRequestURI();
        
        if (requestURI.equals("/logout")) {
            return true;
        }

        if (cargo == null || !permissoes.containsKey(cargo)) {
            response.sendRedirect("/listagem-produtos");
            return false;
        }

        if (!temPermissao(cargo, requestURI)) {
            if (!requestURI.equals("/listagem-produtos")) {
                response.sendRedirect("/listagem-produtos");
                return false;
            }
        }

        return true;
    }

    private boolean temPermissao(String cargo, String requestURI) {
        List<String> urlsPermitidas = new ArrayList<>(permissoes.getOrDefault(cargo, Collections.emptyList()));
        urlsPermitidas.add("/listagem-produtos");
        return urlsPermitidas.stream().anyMatch(requestURI::startsWith);
    }
}