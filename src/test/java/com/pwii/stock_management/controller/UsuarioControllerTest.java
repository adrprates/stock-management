package com.pwii.stock_management.controller;

import com.pwii.stock_management.model.Usuario;
import com.pwii.stock_management.service.IUserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @AfterEach
    void tearDown() {
        reset(userService);
    }

    private Usuario usuarioExemplo() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setName("João");
        u.setEmail("joao@example.com");
        u.setPassword("123");
        u.setRoles(List.of("Gerente"));
        return u;
    }

    @Test
    @WithMockUser
    @DisplayName("GET /user - Lista usuários")
    void testListarUsuarios() throws Exception {
        when(userService.getAllUsuarios()).thenReturn(List.of(usuarioExemplo()));

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/index"))
                .andExpect(model().attributeExists("listUsuarios"))
                .andExpect(content().string(containsString("João")));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /user/editar/{id} - Exibe formulário de edição")
    void testEditarUsuario() throws Exception {
        when(userService.getUsuarioById(1L)).thenReturn(usuarioExemplo());

        mockMvc.perform(get("/user/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/editar"))
                .andExpect(model().attributeExists("usuario"))
                .andExpect(model().attributeExists("roles"))
                .andExpect(model().attribute("usuario", hasProperty("name", is("João"))))
                .andExpect(model().attribute("usuario", hasProperty("email", is("joao@example.com"))));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /user/atualizar - Atualiza usuário válido")
    void testAtualizarUsuarioValido() throws Exception {
        mockMvc.perform(post("/user/atualizar")
                        .with(csrf())
                        .param("id", "1")
                        .param("name", "João")
                        .param("email", "joao@example.com")
                        .param("password", "123")
                        .param("roles", "Gerente"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user"));

        verify(userService).saveUser(ArgumentMatchers.any(Usuario.class));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /user/deletar/{id} - Deleta usuário")
    void testDeletarUsuario() throws Exception {
        mockMvc.perform(get("/user/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/user"));

        verify(userService).deletarUsuarioById(1L);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /register - Exibe formulário de registro")
    void testFormularioRegistro() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registerUser"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /saveUser - Salva novo usuário")
    void testSalvarUsuario() throws Exception {
        when(userService.saveUser(any(Usuario.class))).thenReturn(10L);

        mockMvc.perform(post("/saveUser")
                        .with(csrf())
                        .param("name", "Maria")
                        .param("email", "maria@example.com")
                        .param("password", "abc123")
                        .param("roles", "Estoquista"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/registerUser"))
                .andExpect(model().attributeExists("msg"))
                .andExpect(model().attribute("msg", is("Usuário '10' salvo com sucesso !")));
    }


    @Test
    @WithMockUser
    @DisplayName("GET /accessDenied - Página de acesso negado")
    void testPaginaAccessoNegado() throws Exception {
        mockMvc.perform(get("/accessDenied"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/accessDeniedPage"));
    }
}

