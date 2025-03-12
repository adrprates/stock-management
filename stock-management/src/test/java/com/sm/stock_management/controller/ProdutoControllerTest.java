package com.sm.stock_management.controller;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.model.Produto;
import com.sm.stock_management.model.Usuario;
import com.sm.stock_management.service.CategoriaService;
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

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.location=classpath:/application-test.properties")
class ProdutoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProdutoService produtoService;

    @Autowired
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

    private Categoria setUpCategoria(){
        Categoria categoria = new Categoria();
        categoria.setId(2);
        categoria.setNome("Categoria");
        return categoria;
    }

    @Test
    @DisplayName("Cadastrar: carregar página de cadastro")
    void cadastrar() throws Exception {
        mockMvc.perform(get("/cadastro-produto").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("cadastro-produto"))
                .andExpect(model().attributeExists("produto"));
    }

    @Test
    @DisplayName("Cadastrar: salvar um novo produto")
    void processarFormularioCase1() throws Exception {
        Produto produto = new Produto();
        produto.setNome("Produto");
        produto.setCategoria(setUpCategoria());
        produto.setPreco(10.00);
        produto.setDescricao("Melhor produto de todos");

        when(produtoService.adicionar(produto)).thenReturn(produto);

        mockMvc.perform(post("/salvar-produto")
                .session(session)
                .flashAttr("produto", produto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-produtos"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));
    }

    @Test
    @DisplayName("Cadastrar: erro ao salvar um novo produto")
    void processarFormularioCase2() throws Exception {
        Produto produto = new Produto();

        when(produtoService.adicionar(any(Produto.class))).thenReturn(null);

        mockMvc.perform(post("/salvar-produto")
                        .session(session)
                        .flashAttr("produto", produto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-produtos"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));
    }

    @Test
    @DisplayName("Atualizar: salvar produto atualizado")
    void processarFormularioCase3() throws Exception {
        Produto produtoAntigo = new Produto();
        produtoAntigo.setId(1);
        produtoAntigo.setNome("Antigo produto");
        produtoAntigo.setCategoria(setUpCategoria());
        produtoAntigo.setPreco(10.00);
        produtoAntigo.setDescricao("Melhor produto de todos");

        when(produtoService.buscarPorId(1)).thenReturn(produtoAntigo);

        Produto produtoNovo = new Produto();
        produtoNovo.setNome("Novo produto");
        produtoNovo.setCategoria(setUpCategoria());
        produtoNovo.setPreco(1.00);
        produtoNovo.setDescricao("Pior produto de todos");

        when(produtoService.atualizar(eq(1), any(Produto.class))).thenThrow(new RuntimeException("Erro ao salvar produto"));

        mockMvc.perform(post("/salvar-produto")
                        .session(session)
                        .flashAttr("produto", produtoNovo))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-produtos"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));
    }

    @Test
    @DisplayName("Atualizar: erro ao tentar salvar produto atualizado")
    void processarFormularioCase4() throws Exception {
        Produto produtoAntigo = new Produto();
        produtoAntigo.setId(1);
        produtoAntigo.setNome("Antigo produto");
        produtoAntigo.setCategoria(setUpCategoria());
        produtoAntigo.setPreco(10.00);
        produtoAntigo.setDescricao("Melhor produto de todos");

        when(produtoService.buscarPorId(1)).thenReturn(produtoAntigo);

        Produto produtoNovo = new Produto();
        produtoNovo.setNome("Novo produto");
        produtoNovo.setCategoria(setUpCategoria());
        produtoNovo.setPreco(1.00);
        produtoNovo.setDescricao("Pior produto de todos");

        when(produtoService.atualizar(eq(1), any(Produto.class))).thenAnswer(invocation -> {
            invocation.getArgument(1);
            produtoAntigo.setNome(produtoNovo.getNome());
            produtoAntigo.setCategoria(produtoNovo.getCategoria());
            produtoAntigo.setPreco(produtoNovo.getPreco());
            produtoAntigo.setDescricao(produtoNovo.getDescricao());
            return produtoAntigo;
        } );

        mockMvc.perform(post("/salvar-produto")
                        .session(session)
                        .flashAttr("produto", produtoAntigo))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-produtos"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));
    }

    @Test
    @DisplayName("Listagem: todos os produtos")
    void listarCase1() throws Exception {
        Produto produto1 = new Produto();
        produto1.setId(1);
        produto1.setNome("Produto 1");
        produto1.setCategoria(setUpCategoria());
        produto1.setPreco(10.00);
        produto1.setDescricao("Produto 1");

        Produto produto2 = new Produto();
        produto2.setId(2);
        produto2.setNome("Produto 2");
        produto2.setCategoria(setUpCategoria());
        produto2.setPreco(10.00);
        produto2.setDescricao("Produto 2");

        List<Produto> produtos = List.of(produto1, produto2);
        when(produtoService.buscarTodos()).thenReturn(produtos);

        mockMvc.perform(get("/listagem-produtos")
                .session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("listagem-produtos"))
                .andExpect(model().attributeExists("produtos"));

    }

    @Test
    @DisplayName("Listagem: busca de dois produtos pelo nome")
    void listarCase2() throws Exception {
        Produto produto1 = new Produto();
        produto1.setId(1);
        produto1.setNome("Produto 1");
        produto1.setCategoria(setUpCategoria());
        produto1.setPreco(10.00);
        produto1.setDescricao("Produto 1");

        Produto produto2 = new Produto();
        produto2.setId(2);
        produto2.setNome("Produto 2");
        produto2.setCategoria(setUpCategoria());
        produto2.setPreco(10.00);
        produto2.setDescricao("Produto 2");

        List<Produto> produtos = List.of(produto1, produto2);
        when(produtoService.buscarPorNome("Produto")).thenReturn(produtos);

        mockMvc.perform(get("/listagem-produtos")
                        .session(session)
                        .param("nome", "Produto"))
                .andExpect(status().isOk())
                .andExpect(view().name("listagem-produtos"))
                .andExpect(model().attributeExists("produtos"))
                .andExpect(model().attribute("produtos", hasSize(2)));

    }

    @Test
    @DisplayName("Atualizar: carregar página de atualização")
    void atualizar() throws Exception {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Produto 1");
        produto.setCategoria(setUpCategoria());
        produto.setPreco(10.00);
        produto.setDescricao("Produto 1");

        when(produtoService.buscarPorId(eq(1))).thenReturn(produto);

        mockMvc.perform(get("/atualizar-produto/1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("atualizar-produto"))
                .andExpect(model().attributeExists("produto"));
    }

    @Test
    @DisplayName("Deletar: deletar produto")
    void deletarCase1() throws Exception {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Produto 1");
        produto.setCategoria(setUpCategoria());
        produto.setPreco(10.00);
        produto.setDescricao("Produto 1");

        when(produtoService.buscarPorId(eq(1))).thenReturn(produto);
        doNothing().when(produtoService).excluir(1);

        mockMvc.perform(get("/deletar-produto/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-produtos"))
                .andExpect(flash().attributeExists("mensagem"))
                .andExpect(flash().attribute("tipoMensagem", "alert-success"));
    }

    @Test
    @DisplayName("Deletar: erro ao tentar deletar produto")
    void deletarCase2() throws Exception {
        mockMvc.perform(get("/deletar-produto/1").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/listagem-produtos"));
    }
}