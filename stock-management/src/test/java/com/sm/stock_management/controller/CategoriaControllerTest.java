package com.sm.stock_management.controller;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.model.Usuario;
import com.sm.stock_management.service.CategoriaService;
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

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.location=classpath:/application-test.properties")
class CategoriaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoriaService categoriaService;

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
    @DisplayName("Cadastrar: carregar página de cadastro")
    void cadastrar() throws Exception {
        mockMvc.perform(get("/cadastro-categoria").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro-categoria"))
                .andExpect(model().attributeExists("categoria"));

    }

    @Test
    @DisplayName("Cadastrar: salvar nova categoria")
    void processarFormularioCase1() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNome("Eletrônicos");

        when(categoriaService.adicionar(any(Categoria.class))).thenReturn(categoria);

        mockMvc.perform(post("/salvar-categoria")
                        .session(session)
                        .flashAttr("categoria", categoria))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-categorias"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));

    }

    @Test
    @DisplayName("Cadastrar: erro ao salvar nova categoria")
    void processarFormularioCase2() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setNome("Eletrônicos");

        when(categoriaService.adicionar(any(Categoria.class))).thenThrow(new RuntimeException("Erro ao salvar categoria"));

        mockMvc.perform(post("/salvar-categoria")
                        .session(session)
                        .flashAttr("categoria", categoria))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-categorias"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-danger"));
    }

    @Test
    @DisplayName("Atualizar: salvar categoria atualizada")
    void processarFormularioCase3() throws Exception {
        Categoria categoriaAntiga = new Categoria();
        categoriaAntiga.setId(1);
        categoriaAntiga.setNome("Antiga categoria");

        when(categoriaService.buscarPorId(1)).thenReturn(categoriaAntiga);

        Categoria categoriaNova = new Categoria();
        categoriaNova.setNome("Nova categoria");

        when(categoriaService.atualizar(eq(1), any(Categoria.class))).thenAnswer(invocation -> {
            invocation.getArgument(1);
            categoriaAntiga.setNome(categoriaNova.getNome());
            return categoriaAntiga;
        });

        mockMvc.perform(post("/salvar-categoria")
                        .session(session)
                        .flashAttr("categoria", categoriaAntiga))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-categorias"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));

    }
    @Test
    @DisplayName("Atualizar: erro ao salvar categoria atualizada")
    void processarFormularioCase4() throws Exception {
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNome("Informática");

        when(categoriaService.atualizar(eq(1), any(Categoria.class)))
                .thenThrow(new RuntimeException("Erro ao salvar categoria"));

        mockMvc.perform(post("/salvar-categoria")
                        .session(session)
                        .flashAttr("categoria", categoria))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-categorias"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-danger"));
    }

    @Test
    @DisplayName("Listagem: todas as categorias")
    void listarCase1() throws Exception {
        List<Categoria> categorias = List.of(new Categoria(1, "Eletrônicos"));
        when(categoriaService.buscarTodas()).thenReturn(categorias);

        mockMvc.perform(get("/listagem-categorias").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("listagem-categorias"))
                .andExpect(model().attributeExists("categorias"));
    }

    @Test
    @DisplayName("Listagem: busca de duas categorias pelo nome")
    void listarCase2() throws Exception {
        Categoria categoria1 = new Categoria();
        categoria1.setId(1);
        categoria1.setNome("Categoria 1");

        Categoria categoria2 = new Categoria();
        categoria2.setId(2);
        categoria2.setNome("Categoria 2");

        List<Categoria> categorias = List.of(categoria1, categoria2);

        when(categoriaService.buscarPorNome("Categoria")).thenReturn(categorias);

        mockMvc.perform(get("/listagem-categorias")
                        .session(session)
                        .param("nome", "Categoria"))
                .andExpect(status().isOk())
                .andExpect(view().name("listagem-categorias"))
                .andExpect(model().attributeExists("categorias"))
                .andExpect(model().attribute("categorias", hasSize(2)));
    }

    @Test
    @DisplayName("Listagem: busca de nenhuma categoria pelo nome")
    void listarCase3() throws Exception {
        List<Categoria> categorias = List.of();

        when(categoriaService.buscarPorNome("Categoria")).thenReturn(categorias);

        mockMvc.perform(get("/listagem-categorias")
                        .session(session)
                        .param("nome", "Categoria"))
                .andExpect(status().isOk())
                .andExpect(view().name("listagem-categorias"))
                .andExpect(model().attributeExists("categorias"))
                .andExpect(model().attribute("categorias", hasSize(0)));
    }

    @Test
    @DisplayName("Atualizar: carregar página de atualizar")
    void atualizar() throws Exception {
        Categoria categoria = new Categoria(1, "Eletrônicos");
        when(categoriaService.buscarPorId(1)).thenReturn(categoria);

        mockMvc.perform(get("/atualizar-categoria/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("atualizar-categoria"))
                .andExpect(model().attributeExists("categoria"));
    }

    @Test
    @DisplayName("Deletar: deletar categoria")
    void deletarCase1() throws Exception {
        Categoria categoria = new Categoria(1, "Eletrônicos");
        when(categoriaService.buscarPorId(1)).thenReturn(categoria);
        doNothing().when(categoriaService).excluir(1);

        mockMvc.perform(get("/deletar-categoria/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-categorias"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));
    }

    @Test
    @DisplayName("Deletar: erro ao tentar excluir categoria inexistente")
    void deletarCase2() throws Exception {
        when(categoriaService.buscarPorId(99)).thenReturn(null);

        mockMvc.perform(get("/deletar-categoria/99").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-categorias"));
    }
}