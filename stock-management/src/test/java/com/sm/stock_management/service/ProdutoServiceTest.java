package com.sm.stock_management.service;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.model.Produto;
import com.sm.stock_management.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProdutoServiceTest {

    @Mock
    ProdutoRepository produtoRepository;

    @InjectMocks
    ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private Categoria setUpCategoria(){
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria");
        return categoria;
    }

    @Test
    @DisplayName("Adicionando um novo produto")
    void adicionarCase1() {
        Produto produto = new Produto();
        produto.setNome("Produto");
        produto.setCategoria(setUpCategoria());
        produto.setPreco(10.00);
        produto.setDescricao("Melhor produto de todos");

        when(produtoRepository.save(produto)).thenReturn(produto);

        Produto produtoResultado = produtoService.adicionar(produto);

        verify(produtoRepository).save(produto);
        assertNotNull(produtoResultado);
        assertEquals(produto.getNome(), produtoResultado.getNome());
    }

    @Test
    @DisplayName("Exceção ao tentar adicionar um produto nulo")
    void adicionarCase2() {
        assertThrows(NullPointerException.class, () -> produtoService.adicionar(null));
    }

    @Test
    @DisplayName("Buscando produto pelo id")
    void buscarPorIdCase1() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Produto");
        produto.setCategoria(setUpCategoria());
        produto.setPreco(10.00);
        produto.setDescricao("Melhor produto de todos");

        when(produtoRepository.findById(1)).thenReturn(Optional.of(produto));

        Produto produtoResultado = produtoService.buscarPorId(1);

        verify(produtoRepository).findById(1);
        assertNotNull(produtoResultado);
        assertEquals(produto.getNome(), produtoResultado.getNome());
    }

    @Test
    @DisplayName("Exceção ao tentar buscar produto por id inexistente")
    void buscarPorIdCase2() {
        when(produtoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> produtoService.buscarPorId(1));
    }

    @Test
    @DisplayName("Listagem de todos os produtos")
    void buscarTodosCase1() {
        Produto produto1 = new Produto();
        produto1.setNome("Produto 1");
        produto1.setCategoria(setUpCategoria());
        produto1.setPreco(1.00);
        produto1.setDescricao("Melhor produto de todos");

        Produto produto2 = new Produto();
        produto2.setNome("Teste");
        produto2.setCategoria(setUpCategoria());
        produto2.setPreco(1.00);
        produto1.setDescricao("Pior produto de todos");

        List<Produto> produtos = Arrays.asList(produto1, produto2);

        when(produtoRepository.findAll()).thenReturn(produtos);

        List<Produto> produtosEncontrados = produtoService.buscarTodos();

        assertEquals(2, produtosEncontrados.size());
    }

    @Test
    @DisplayName("Lista vazia de produtos")
    void buscarTodosCase2() {
        when(produtoRepository.findAll()).thenReturn(List.of());

        List<Produto> produtosEncontrados = produtoService.buscarTodos();

        assertTrue(produtosEncontrados.isEmpty());
    }

    @Test
    @DisplayName("Busca retornando dois produtos pelo nome")
    void buscarPorNomeCase1() {
        Produto produto1 = new Produto();
        produto1.setNome("Produto 1");
        produto1.setCategoria(setUpCategoria());
        produto1.setPreco(1.00);
        produto1.setDescricao("Melhor produto de todos");

        Produto produto2 = new Produto();
        produto2.setNome("Produto 2");
        produto2.setCategoria(setUpCategoria());
        produto2.setPreco(1.00);
        produto1.setDescricao("Pior produto de todos");

        List<Produto> produtos = Arrays.asList(produto1, produto2);

        when(produtoRepository.findByNomeContaining("Produto")).thenReturn(produtos);

        List<Produto> produtosEncontrados = produtoService.buscarPorNome("Produto");

        assertEquals(2, produtosEncontrados.size());
    }

    @Test
    @DisplayName("Busca retornando nenhum produto pelo nome")
    void buscarPorNomeCase2() {
        when(produtoRepository.findByNomeContaining("Produto")).thenReturn(List.of());

        List<Produto> produtosEncontrados = produtoService.buscarPorNome("Produto");

        assertTrue(produtosEncontrados.isEmpty());
    }

    @Test
    @DisplayName("Atualizando produto")
    void atualizarCase1() {
        Produto produtoAntigo = new Produto();
        produtoAntigo.setId(1);
        produtoAntigo.setNome("Antigo");
        produtoAntigo.setCategoria(setUpCategoria());
        produtoAntigo.setPreco(10.00);
        produtoAntigo.setDescricao("Melhor produto de todos");

        Produto produtoNovo = new Produto();
        produtoNovo.setNome("Novo");
        produtoNovo.setCategoria(setUpCategoria());
        produtoNovo.setPreco(1);
        produtoNovo.setDescricao("Pior produto de todos");

        when(produtoRepository.findById(1)).thenReturn(Optional.of(produtoAntigo));
        when(produtoRepository.save(produtoAntigo)).thenReturn(produtoAntigo);

        Produto produtoResultado = produtoService.atualizar(1, produtoNovo);

        assertEquals(produtoAntigo.getNome(), produtoResultado.getNome());
    }

    @Test
    @DisplayName("Exceção ao tentar atualizar usuário inexistente")
    void atualizarCase2() {
        Produto produto = new Produto();
        produto.setNome("Novo");
        produto.setCategoria(setUpCategoria());
        produto.setPreco(1);
        produto.setDescricao("Pior produto de todos");

        when(produtoRepository.findById(88)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> produtoService.atualizar(88, produto));

    }

    @Test
    @DisplayName("Excluindo produto")
    void excluirCase1() {
        Produto produto = new Produto();
        produto.setId(1);
        produto.setNome("Novo");
        produto.setCategoria(setUpCategoria());
        produto.setPreco(1);
        produto.setDescricao("Pior produto de todos");

        when(produtoRepository.findById(1)).thenReturn(Optional.of(produto));
        doNothing().when(produtoRepository).delete(produto);

        produtoService.excluir(1);

        verify(produtoRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Exceção para tentativa de excluir produto inexistente")
    void excluirCase2() {
        when(produtoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> produtoService.excluir(1));
    }
}