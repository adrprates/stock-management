package com.sm.stock_management.controller;

import com.sm.stock_management.model.Usuario;
import com.sm.stock_management.repository.UsuarioRepository;
import com.sm.stock_management.service.CategoriaService;
import com.sm.stock_management.service.MovimentacaoService;
import com.sm.stock_management.service.ProdutoService;
import com.sm.stock_management.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.location=classpath:/application-test.properties")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setId(1);
        usuarioLogado.setNome("Admin");
        usuarioLogado.setEmail("admin@email.com");
        usuarioLogado.setSenha("123456");
        usuarioLogado.setCargo("Gerente");

        session = new MockHttpSession();
        session.setAttribute("usuarioLogado", usuarioLogado);
    }

    @Test
    @DisplayName("Login: sucesso ao realizar login")
    void loginCase1() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("usuario@teste.com");
        usuario.setSenha("senha123");

        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(usuario);

        mockMvc.perform(post("/login")
                        .param("email", "usuario@teste.com")
                        .param("senha", "senha123")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-produtos"));
    }

    @Test
    @DisplayName("Login: falha ao tentar realizar login")
    void loginCase2() throws Exception {
        when(usuarioRepository.findByEmail("usuario@teste.com")).thenReturn(null);

        mockMvc.perform(post("/login")
                        .param("email", "usuario@teste.com")
                        .param("senha", "senhaErrada")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("mensagem", "E-mail e/ou senha inválidos!"))
                .andExpect(flash().attribute("tipoMensagem", "alert-danger"));
    }

    @Test
    @DisplayName("Logout: sucesso ao realizar logout")
    void logoutCase1() throws Exception {
        mockMvc.perform(get("/logout")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(request().sessionAttributeDoesNotExist("usuarioLogado"));
    }
}