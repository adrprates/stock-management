package com.sm.stock_management.controller;

import com.sm.stock_management.model.Usuario;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.location=classpath:/application-test.properties")
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    private MockHttpSession session;

    @BeforeEach
    void setUsuarioLogado() {
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
    @DisplayName("Cadastrar: carregar página de cadastro")
    void cadastrar() throws Exception {
        mockMvc.perform(get("/cadastro-usuario").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro-usuario"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    @DisplayName("Cadastrar: salvar novo usuário")
    void processarFormularioCase1() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Estoquista");
        usuario.setEmail("estoquista@email.com");
        usuario.setSenha("estoquista123");
        usuario.setCargo("Estoquista");

        when(usuarioService.adicionar(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/salvar-usuario")
                        .session(session)
                        .flashAttr("usuario", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-usuarios"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));
    }

    @Test
    @DisplayName("Cadastrar: erro ao salvar novo usuário")
    void processarFormularioCase2() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setNome("Estoquista");
        usuario.setEmail("estoquista@email.com");
        usuario.setSenha("estoquista123");
        usuario.setCargo("Estoquista");

        when(usuarioService.adicionar(any(Usuario.class))).thenReturn(null);

        mockMvc.perform(post("/salvar-usuario")
                        .session(session)
                        .flashAttr("usuario", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cadastro-usuario"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-danger"));
    }

    @Test
    @DisplayName("Atualizar: salvar categoria atualizada")
    void processarFormularioCase3() throws Exception {
        Usuario usuarioAntigo = new Usuario();
        usuarioAntigo.setId(2);
        usuarioAntigo.setNome("Antigo Estoquista");
        usuarioAntigo.setEmail("antigoestoquista@email.com");
        usuarioAntigo.setSenha("estoquista123");
        usuarioAntigo.setCargo("Estoquista");

        when(usuarioService.buscarPorId(2)).thenReturn(usuarioAntigo);

        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setNome("Novo Estoquista");
        usuarioNovo.setEmail("novoestoquista@email.com");
        usuarioNovo.setSenha("novoestoquista");
        usuarioNovo.setCargo("Estoquista");

        when(usuarioService.atualizar(eq(2), any(Usuario.class))).thenAnswer(invocation -> {
            invocation.getArgument(1);
            usuarioAntigo.setNome(usuarioNovo.getNome());
            usuarioAntigo.setEmail(usuarioNovo.getEmail());
            usuarioAntigo.setSenha(usuarioNovo.getSenha());
            usuarioAntigo.setCargo(usuarioNovo.getCargo());
            return usuarioAntigo;
        });

        mockMvc.perform(post("/salvar-usuario")
                        .session(session)
                        .flashAttr("usuario", usuarioAntigo))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-usuarios"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));
    }

    @Test
    @DisplayName("Atualizar: erro ao salvar usuário atualizado")
    void processarFormularioCase4() throws Exception {
        Usuario usuarioAntigo = new Usuario();
        usuarioAntigo.setNome("Antigo Estoquista");
        usuarioAntigo.setEmail("antigoestoquista@email.com");
        usuarioAntigo.setSenha("estoquista123");
        usuarioAntigo.setCargo("Estoquista");

        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setNome("Novo Estoquista");
        usuarioNovo.setEmail("novoestoquista@email.com");
        usuarioNovo.setSenha("novaSenha123");
        usuarioNovo.setCargo("Estoquista");

        when(usuarioService.buscarPorId(1)).thenReturn(usuarioAntigo);
        when(usuarioService.atualizar(eq(1), any(Usuario.class)))
                .thenThrow(new RuntimeException("Erro ao salvar usuário"));

        mockMvc.perform(post("/salvar-usuario")
                        .session(session)
                        .flashAttr("usuario", usuarioNovo))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cadastro-usuario"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-danger"));
    }


    @Test
    @DisplayName("Listagem: todos os usuários")
    void listarCase1() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setNome("Estoquista");
        usuario1.setEmail("estoquista@email.com");
        usuario1.setSenha("estoquista123");
        usuario1.setCargo("Estoquista");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setNome("Comprador");
        usuario2.setEmail("comprador@email.com");
        usuario2.setSenha("comprador123");
        usuario2.setCargo("Comprador");

        List<Usuario> usuarios = List.of(usuario1, usuario2);
        when(usuarioService.buscarTodos()).thenReturn(usuarios);

        mockMvc.perform(get("/listagem-usuarios").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("listagem-usuarios"))
                .andExpect(model().attributeExists("usuarios"));
    }

    @Test
    @DisplayName("Listagem: busca de dois usuários pelo nome")
    void listarCase2() throws Exception {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setNome("Estoquista Um");
        usuario1.setEmail("estoquistaum@email.com");
        usuario1.setSenha("estoquista123");
        usuario1.setCargo("Estoquista");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setNome("Estoquista Dois");
        usuario2.setEmail("estoquistadois@email.com");
        usuario2.setSenha("estoquista321");
        usuario2.setCargo("Estoquista");

        List<Usuario> usuarios = List.of(usuario1, usuario2);
        when(usuarioService.buscarPorNome(("Estoquista"))).thenReturn(usuarios);

        mockMvc.perform(get("/listagem-usuarios")
                .session(session)
                .param("nome", "Estoquista"))
                .andExpect(status().isOk())
                .andExpect(view().name("listagem-usuarios"))
                .andExpect(model().attributeExists("usuarios"))
                .andExpect(model().attribute("usuarios", hasSize(2)));


    }

    @Test
    @DisplayName("Atualizar: carregar página de atualização")
    void atualizar() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Estoquista Um");
        usuario.setEmail("estoquistaum@email.com");
        usuario.setSenha("estoquista123");
        usuario.setCargo("Estoquista");

        when(usuarioService.buscarPorId(1)).thenReturn(usuario);

        mockMvc.perform(get("/atualizar-usuario/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("atualizar-usuario"))
                .andExpect(model().attributeExists("usuario"));
    }

    @Test
    @DisplayName("Deletar: deletar usuário")
    void deletarCase1() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setId(2);
        usuario.setNome("Gerente");
        usuario.setEmail("gerente@email.com");
        usuario.setSenha("gerente123");
        usuario.setCargo("Gerente");

        when(usuarioService.buscarPorId(2)).thenReturn(usuario);
        doNothing().when(usuarioService).excluir(2);

        mockMvc.perform(get("/deletar-usuario/2").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-usuarios"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));
    }

    @Test
    @DisplayName("Deletar: erro ao tentar excluir o próprio usuário")
    void deletarCase2() throws Exception {
        mockMvc.perform(get("/deletar-usuario/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-usuarios"));
    }
}