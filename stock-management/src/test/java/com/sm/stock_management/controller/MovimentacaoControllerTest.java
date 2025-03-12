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
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    @DisplayName("Nova: realizar nova movimentação de inclusão")
    void processarFormularioCase1() throws Exception {
        Produto produto = setUpProduto();
        produto.setQuantidade(10);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Inclusao");
        movimentacao.setData(setUpData());
        movimentacao.setHora(setUpHora());

        when(produtoService.buscarPorId(1)).thenReturn(produto);
        when(movimentacaoService.adicionar(any(Movimentacao.class)))
                .thenAnswer(invocation -> {
                    Movimentacao mov = invocation.getArgument(0);
                    Produto p = mov.getProduto();
                    if ("Inclusao".equals(mov.getTipo())) {
                        p.setQuantidade(p.getQuantidade() + mov.getQuantidade());
                    }
                    return mov;
                });

        mockMvc.perform(post("/salvar-movimentacao")
                        .session(session)
                        .flashAttr("movimentacao", movimentacao))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-movimentacao-produto/" + movimentacao.getProduto().getId()))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));
        assertEquals(20, produto.getQuantidade());
    }

    @Test
    @DisplayName("Nova: erro ao realizar nova movimentação inválida")
    void processarFormularioCase2() throws Exception {
        Produto produto = setUpProduto();

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);

        when(produtoService.buscarPorId(1)).thenReturn(produto);

        mockMvc.perform(post("/salvar-movimentacao")
                        .session(session)
                        .flashAttr("movimentacao", movimentacao))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-movimentacao-produto/" + movimentacao.getProduto().getId()))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-danger"));
    }

    @Test
    @DisplayName("Nova: realizar nova movimentação de retirada")
    void processarFormularioCase3() throws Exception {
        Produto produto = setUpProduto();
        produto.setQuantidade(20);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Retirada");
        movimentacao.setData(setUpData());
        movimentacao.setHora(setUpHora());

        when(produtoService.buscarPorId(1)).thenReturn(produto);
        when(movimentacaoService.adicionar(any(Movimentacao.class)))
                .thenAnswer(invocation -> {
                    Movimentacao mov = invocation.getArgument(0);
                    Produto p = mov.getProduto();
                    if ("Retirada".equals(mov.getTipo())) {
                        p.setQuantidade(p.getQuantidade() - mov.getQuantidade());
                    }
                    return mov;
                });

        mockMvc.perform(post("/salvar-movimentacao")
                        .session(session)
                        .flashAttr("movimentacao", movimentacao))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-movimentacao-produto/" + movimentacao.getProduto().getId()))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));

        assertEquals(10, produto.getQuantidade());
    }

    @Test
    @DisplayName("Nova: erro ao tentar realizar nova movimentação de retirada")
    void processarFormularioCase4() throws Exception {
        Produto produto = setUpProduto();
        produto.setQuantidade(20);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(21);
        movimentacao.setTipo("Retirada");
        movimentacao.setData(setUpData());
        movimentacao.setHora(setUpHora());

        when(produtoService.buscarPorId(1)).thenReturn(produto);
        when(movimentacaoService.adicionar(any(Movimentacao.class))).thenReturn(null);

        mockMvc.perform(post("/salvar-movimentacao")
                        .session(session)
                        .flashAttr("movimentacao", movimentacao))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-movimentacao-produto/" + movimentacao.getProduto().getId()))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-danger"));

        assertEquals(20, produto.getQuantidade());
    }

    @Test
    @DisplayName("Listagem: todas as movimentações de um produto")
    void listarCase1() throws Exception {
        Produto produto = setUpProduto();
        produto.setId(1);
        produto.setQuantidade(0);

        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setId(100);
        movimentacao1.setProduto(produto);
        movimentacao1.setQuantidade(10);
        movimentacao1.setTipo("Inclusao");
        movimentacao1.setData(setUpData());
        movimentacao1.setHora(setUpHora());

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setId(101);
        movimentacao2.setProduto(produto);
        movimentacao2.setQuantidade(10);
        movimentacao2.setTipo("Inclusao");
        movimentacao2.setData(setUpData());
        movimentacao2.setHora(setUpHora());

        List<Movimentacao> movimentacoes = Arrays.asList(movimentacao1, movimentacao2);

        Movimentacao ultimaMovimentacao = movimentacao2;

        when(produtoService.buscarPorId(1)).thenReturn(produto);
        when(movimentacaoService.buscarTodas(produto)).thenReturn(movimentacoes);
        when(movimentacaoService.obterUltimaMovimentacao()).thenReturn(ultimaMovimentacao);

        mockMvc.perform(get("/listagem-movimentacao-produto/{id}", produto.getId())
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("listagem-movimentacao-produto"))
                .andExpect(model().attributeExists("movimentacoes"))
                .andExpect(model().attributeExists("ultimaMovimentacao"))
                .andExpect(model().attribute("ultimaMovimentacao", ultimaMovimentacao))
                .andExpect(model().attribute("movimentacoes", hasSize(2)));
    }

    @Test
    @DisplayName("Listagem: busca de movimentações por data")
    void listarCase2() throws Exception {
        Produto produto = setUpProduto();
        produto.setId(1);
        produto.setQuantidade(0);

        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setId(100);
        movimentacao1.setProduto(produto);
        movimentacao1.setQuantidade(10);
        movimentacao1.setTipo("Inclusao");
        movimentacao1.setData(LocalDate.of(2025, 1, 1));
        movimentacao1.setHora(setUpHora());

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setId(101);
        movimentacao2.setProduto(produto);
        movimentacao2.setQuantidade(10);
        movimentacao2.setTipo("Inclusao");
        movimentacao2.setData(LocalDate.of(2025, 1, 1));
        movimentacao2.setHora(setUpHora());

        List<Movimentacao> movimentacoes = Arrays.asList(movimentacao1, movimentacao2);

        Movimentacao ultimaMovimentacao = movimentacao2;

        when(produtoService.buscarPorId(1)).thenReturn(produto);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dataAtual = LocalDate.parse("2025-01-01", formatter);

        when(movimentacaoService.buscarPorData(eq(dataAtual), any(Produto.class)))
                .thenReturn(movimentacoes);
        when(movimentacaoService.obterUltimaMovimentacao()).thenReturn(ultimaMovimentacao);

        mockMvc.perform(get("/listagem-movimentacao-produto/{id}", produto.getId())
                        .param("data", "2025-01-01")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("listagem-movimentacao-produto"))
                .andExpect(model().attributeExists("movimentacoes"))
                .andExpect(model().attributeExists("ultimaMovimentacao"))
                .andExpect(model().attribute("ultimaMovimentacao", ultimaMovimentacao));
    }

    @Test
    @DisplayName("Deletar: deletar movimentação de inclusão")
    void deletarCase1() throws Exception {
        Produto produto = setUpProduto();
        produto.setId(1);
        produto.setQuantidade(0);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1);
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Inclusao");
        movimentacao.setData(setUpData());
        movimentacao.setHora(setUpHora());

        when(movimentacaoService.buscarPorId(1)).thenReturn(movimentacao);
        doAnswer(invocation -> {
            produto.setQuantidade(produto.getQuantidade() + movimentacao.getQuantidade());
            return null;
        }).when(movimentacaoService).excluir(1);

        Movimentacao ultimaMovimentacao = movimentacao;
        when(movimentacaoService.obterUltimaMovimentacao()).thenReturn(ultimaMovimentacao);

        mockMvc.perform(get("/deletar-movimentacao/1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-movimentacao-produto/1"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"))
                .andExpect(flash().attributeExists("ultimaMovimentacao"))
                .andExpect(flash().attribute("ultimaMovimentacao", ultimaMovimentacao));

        assertEquals(10, produto.getQuantidade());
    }

    @Test
    @DisplayName("Deletar: deletar movimentação de retirada")
    void deletarCase2() throws Exception {
        Produto produto = setUpProduto();
        produto.setId(1);
        produto.setQuantidade(20);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1);
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Retirada");
        movimentacao.setData(setUpData());
        movimentacao.setHora(setUpHora());

        when(movimentacaoService.buscarPorId(1)).thenReturn(movimentacao);
        doAnswer(invocation -> {
            produto.setQuantidade(produto.getQuantidade() - movimentacao.getQuantidade());
            return null;
        }).when(movimentacaoService).excluir(1);

        Movimentacao ultimaMovimentacao = movimentacao;
        when(movimentacaoService.obterUltimaMovimentacao()).thenReturn(ultimaMovimentacao);

        mockMvc.perform(get("/deletar-movimentacao/1")
                        .session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-movimentacao-produto/1"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"))
                .andExpect(flash().attributeExists("ultimaMovimentacao"))
                .andExpect(flash().attribute("ultimaMovimentacao", ultimaMovimentacao));

        assertEquals(10, produto.getQuantidade());
    }

    @Test
    @DisplayName("Deletar: erro ao tentar deletar movimentação")
    void deletarCase3() throws Exception {
        Produto produto = setUpProduto();
        produto.setId(1);
        produto.setQuantidade(0);

        when(movimentacaoService.buscarPorId(1)).thenReturn(null);

        doNothing().when(movimentacaoService).excluir(1);

        mockMvc.perform(get("/deletar-movimentacao/1")
                        .session(session))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-danger"));

        assertEquals(0, produto.getQuantidade());
    }


}