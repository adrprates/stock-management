package com.sm.stock_management.controller;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.model.Movimentacao;
import com.sm.stock_management.model.Produto;
import com.sm.stock_management.model.Usuario;
import com.sm.stock_management.service.CategoriaService;
import com.sm.stock_management.service.MovimentacaoService;
import com.sm.stock_management.service.ProdutoService;
import com.sm.stock_management.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.location=classpath:/application-test.properties")
class MovimentacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovimentacaoService movimentacaoService;

    @MockBean
    private ProdutoService produtoService;

    @MockBean
    private CategoriaService categoriaService;

    @MockBean
    private UsuarioService usuarioService;

    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        Usuario usuarioLogado = new Usuario();
        usuarioLogado.setId(1);
        usuarioLogado.setNome("Admin");
        usuarioLogado.setEmail("admin@email.com");
        usuarioLogado.setSenha("123456");
        usuarioLogado.setCargo("Gerente");

        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNome("Categoria");

        categoriaService.adicionar(categoria);

        session = new MockHttpSession();
        session.setAttribute("usuarioLogado", usuarioLogado);
    }

    private Produto setUpProduto(){
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNome("Categoria");

        categoriaService.adicionar(categoria);

        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Produto");
        produto.setCategoria(categoria);
        produto.setQuantidade(0);
        produto.setPreco(10.0);
        return produto;
    }

    private LocalTime setUpHora(){
        return LocalTime.now();
    }

    private LocalDate setUpData(){
        return LocalDate.now();
    }

    @Test
    @DisplayName("Nova: carregar página de realizar nova movimentação")
    void novaCase1() throws Exception {
        Produto produto = setUpProduto();

        when(produtoService.buscarPorId(1)).thenReturn(produto);

        mockMvc.perform(get("/movimentacao-produto/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("movimentacao-produto"))
                .andExpect(model().attributeExists("produto"))
                .andExpect(model().attributeExists("movimentacao"))
                .andExpect(model().attribute("produto", produto));
    }

    @Test
    @DisplayName("Nova: erro ao tentar carregar página para movimentar um produto inexistente")
    void novaCase2() throws Exception {
        when(produtoService.buscarPorId(1)).thenReturn(null);

        mockMvc.perform(get("/movimentacao-produto/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-produtos"));
    }

    @Test
    @DisplayName("Realizar: realizar nova movimentação")
    void processarFormularioCase1() {
        Produto produto = setUpProduto();

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Inclusao");
        movimentacao.setData(setUpData());
        movimentacao.setHora(setUpHora());


    }

    @Test
    void listar() {
    }

    @Test
    void atualizar() {
    }

    @Test
    void deletar() {
    }
}