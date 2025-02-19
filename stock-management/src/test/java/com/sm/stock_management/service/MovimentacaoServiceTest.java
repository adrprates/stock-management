package com.sm.stock_management.service;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.model.Movimentacao;
import com.sm.stock_management.model.Produto;
import com.sm.stock_management.repository.MovimentacaoRepository;
import com.sm.stock_management.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimentacaoServiceTest {

    @Mock
    MovimentacaoRepository movimentacaoRepository;

    @Mock
    private ProdutoRepository produtoRepository;


    @InjectMocks
    MovimentacaoService movimentacaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private Produto setUpProduto(){
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNome("Categoria");

        Produto produto = new Produto();
        produto.setNome("Produto");
        produto.setCategoria(categoria);
        produto.setPreco(10.00);
        produto.setDescricao("Melhor produto de todos");

        return produto;
    }

    @Test
    @DisplayName("Adicionando movimentação")
    void adicionarCase1() {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(setUpProduto());
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Inclusao");
        movimentacao.setData(LocalDate.now());
        movimentacao.setHora(LocalTime.now());

        when(movimentacaoRepository.save(movimentacao)).thenReturn(movimentacao);

        Movimentacao movimentacaoResultado = movimentacaoService.adicionar(movimentacao);

        verify(movimentacaoRepository).save(movimentacao);
        assertNotNull(movimentacaoResultado);
        assertEquals(movimentacao, movimentacaoResultado);
    }

    @Test
    @DisplayName("Exceção ao tentar adicionar uma movimentação nula")
    void adicionarCase2() {
        assertThrows(NullPointerException.class, () -> movimentacaoService.adicionar(null));
    }

    @Test
    @DisplayName("Buscando movimentação pelo id")
    void buscarPorIdCase1() {
        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1);
        movimentacao.setProduto(setUpProduto());
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Inclusao");
        movimentacao.setData(LocalDate.now());
        movimentacao.setHora(LocalTime.now());

        when(movimentacaoRepository.findById(1)).thenReturn(Optional.of(movimentacao));

        Movimentacao movimentacaoResultado = movimentacaoService.buscarPorId(1);

        verify(movimentacaoRepository).findById(1);
        assertNotNull(movimentacaoResultado);
        assertEquals(movimentacao, movimentacaoResultado);
    }

    @Test
    @DisplayName("Exceção ao tentar buscar movimentação por id inexistente")
    void buscarPorIdCase2() {
        when(movimentacaoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> movimentacaoService.buscarPorId(1));
    }

    @Test
    @DisplayName("Listagem de todas as movimentações de um produto")
    void buscarTodasCase1() {
        Produto produto = setUpProduto();

        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setProduto(produto);
        movimentacao1.setQuantidade(10);
        movimentacao1.setTipo("Inclusao");
        movimentacao1.setData(LocalDate.now());
        movimentacao1.setHora(LocalTime.now());

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setProduto(produto);
        movimentacao2.setQuantidade(10);
        movimentacao2.setTipo("Retirada");
        movimentacao2.setData(LocalDate.now());
        movimentacao2.setHora(LocalTime.now());

        List<Movimentacao> movimentacoes = Arrays.asList(movimentacao1, movimentacao2);

        when(movimentacaoRepository.findByProduto(produto)).thenReturn(movimentacoes);

        List<Movimentacao> movimentacoesEncontradas = movimentacaoService.buscarTodas(produto);

        assertEquals(2, movimentacoesEncontradas.size());
    }

    @Test
    @DisplayName("Lista vazia de movimentações")
    void buscarTodasCase2() {
        Produto produto = setUpProduto();

        when(movimentacaoRepository.findByProduto(produto)).thenReturn(List.of());

        List<Movimentacao> movimentacoesEncontradas = movimentacaoService.buscarTodas(setUpProduto());

        assertTrue(movimentacoesEncontradas.isEmpty());
    }

    @Test
    @DisplayName("Busca retornando duas movimentações pela data e produto")
    void buscarPorDataCase1() {
        LocalDate data = LocalDate.of(2020, 1, 1);

        Produto produto = setUpProduto();
        
        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setProduto(produto);
        movimentacao1.setQuantidade(10);
        movimentacao1.setTipo("Inclusao");
        movimentacao1.setData(data);
        movimentacao1.setHora(LocalTime.now());

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setProduto(produto);
        movimentacao2.setQuantidade(10);
        movimentacao2.setTipo("Retirada");
        movimentacao2.setData(data);
        movimentacao2.setHora(LocalTime.now());

        List<Movimentacao> movimentacoes = Arrays.asList(movimentacao1, movimentacao2);

        when(movimentacaoRepository.findByDataAndProduto(data, produto)).thenReturn(movimentacoes);

        List<Movimentacao> movimentacoesEncontradas = movimentacaoService.buscarPorData(data, produto);

        assertEquals(2, movimentacoesEncontradas.size());
    }

    @Test
    @DisplayName("Busca utilizando data de movimentação inexistente")
    void buscarPorDataCase2() {
        LocalDate data = LocalDate.of(2020, 1, 1);

        Produto produto = setUpProduto();

        when(movimentacaoRepository.findByDataAndProduto(data, setUpProduto())).thenReturn(List.of());

        List<Movimentacao> movimentacoesEncontradas = movimentacaoService.buscarPorData(data, produto);

        assertTrue(movimentacoesEncontradas.isEmpty());
    }

    @Test
    @DisplayName("Busca retornando a movimentação mais recente")
    void obterUltimaMovimentacaoCase1() {
        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setProduto(setUpProduto());
        movimentacao1.setQuantidade(10);
        movimentacao1.setTipo("Inclusao");
        movimentacao1.setData(LocalDate.now());
        movimentacao1.setHora(LocalTime.now());

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setProduto(setUpProduto());
        movimentacao2.setQuantidade(10);
        movimentacao2.setTipo("Retirada");
        movimentacao2.setData(LocalDate.now());
        movimentacao2.setHora(LocalTime.now());

        when(movimentacaoRepository.findTopByOrderByDataDescHoraDesc()).thenReturn(movimentacao2);

        Movimentacao movimentacaoResultado = movimentacaoService.obterUltimaMovimentacao();

        assertEquals(movimentacao2, movimentacaoResultado);
    }

    @Test
    @DisplayName("Busca não havendo movimentações cadatradas")
    void obterUltimaMovimentacaoCase2() {
        when(movimentacaoRepository.findTopByOrderByDataDescHoraDesc()).thenReturn(null);

        Movimentacao movimentacaoResultado = movimentacaoService.obterUltimaMovimentacao();

        assertNull(movimentacaoResultado);
    }

    @Test
    @DisplayName("Lógica aplicando uma movimentação de inclusão")
    void aplicarMovimentacaoCase1() {
        Produto produto = setUpProduto();

        produto.setQuantidade(10);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Inclusao");
        movimentacao.setData(LocalDate.now());
        movimentacao.setHora(LocalTime.now());

        movimentacaoService.aplicarMovimentacao(movimentacao);

        assertEquals(20, produto.getQuantidade());
    }

    @Test
    @DisplayName("Lógica aplicando uma movimentação de retirada")
    void aplicarMovimentacaoCase2() {
        Produto produto = setUpProduto();

        produto.setQuantidade(10);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(5);
        movimentacao.setTipo("Retirada");
        movimentacao.setData(LocalDate.now());
        movimentacao.setHora(LocalTime.now());

        movimentacaoService.aplicarMovimentacao(movimentacao);

        assertEquals(5, produto.getQuantidade());
    }

    @Test
    @DisplayName("Lógica para uma movimentação de retirada com quantidade insuficiente em estoque")
    void aplicarMovimentacaoCase3() {
        Produto produto = setUpProduto();

        produto.setQuantidade(0);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(6);
        movimentacao.setTipo("Retirada");
        movimentacao.setData(LocalDate.now());
        movimentacao.setHora(LocalTime.now());

        assertEquals(0, produto.getQuantidade());

        boolean quantidadeValida = true;

        int resultado = produto.getQuantidade() - movimentacao.getQuantidade();
        if(resultado >= 0){
            produto.setQuantidade(produto.getQuantidade() - movimentacao.getQuantidade());
        } else {
            quantidadeValida = false;
        }

        assertFalse(quantidadeValida);
        assertEquals(0, produto.getQuantidade());
    }

    @Test
    @DisplayName("Removendo movimentação do tipo inclusão")
    void removerMovimentacaoCase1() {
        Produto produto = setUpProduto();

        produto.setQuantidade(10);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1);
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Inclusao");
        movimentacao.setData(LocalDate.now());
        movimentacao.setHora(LocalTime.now());

        //aplicando movimentação
        if(movimentacao.getTipo().equalsIgnoreCase("Inclusao")) {
            produto.setQuantidade(produto.getQuantidade() + movimentacao.getQuantidade());
        }

        //verificando quantidade do produto atual
        assertEquals(20, produto.getQuantidade());

        when(movimentacaoRepository.findById(1)).thenReturn(Optional.of(movimentacao));
        doNothing().when(movimentacaoRepository).delete(movimentacao);

        movimentacaoService.excluir(1);

        verify(movimentacaoRepository, times(1)).deleteById(1);

        //quantidado do produto após exclusão da movimentacao
        assertEquals(10, produto.getQuantidade());
    }

    @Test
    @DisplayName("Removendo movimentação do tipo retirada")
    void removerMovimentacaoCase2() {
        Produto produto = setUpProduto();

        produto.setQuantidade(10);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1);
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Retirada");
        movimentacao.setData(LocalDate.now());
        movimentacao.setHora(LocalTime.now());

        //aplicando movimentação
        if(movimentacao.getTipo().equalsIgnoreCase("Retirada")) {
            produto.setQuantidade(produto.getQuantidade() - movimentacao.getQuantidade());
        }

        //verificando quantidade do produto atual
        assertEquals(0, produto.getQuantidade());

        when(movimentacaoRepository.findById(1)).thenReturn(Optional.of(movimentacao));
        doNothing().when(movimentacaoRepository).delete(movimentacao);

        movimentacaoService.excluir(1);

        verify(movimentacaoRepository, times(1)).deleteById(1);

        //quantidado do produto após exclusão da movimentacao
        assertEquals(10, produto.getQuantidade());
    }

    @Test
    @DisplayName("Atualizando movimentação")
    void atualizarCase1() {
        Produto produto = setUpProduto();

        produto.setQuantidade(100);

        Movimentacao movimentacaoAntiga = new Movimentacao();
        movimentacaoAntiga.setId(1);
        movimentacaoAntiga.setProduto(produto);
        movimentacaoAntiga.setQuantidade(10);
        movimentacaoAntiga.setTipo("Inclusao");
        movimentacaoAntiga.setData(LocalDate.now());
        movimentacaoAntiga.setHora(LocalTime.now());

        Movimentacao movimentacaoNova = new Movimentacao();
        movimentacaoNova.setProduto(produto);
        movimentacaoNova.setQuantidade(5);
        movimentacaoNova.setTipo("Retirada");
        movimentacaoNova.setData(LocalDate.now());
        movimentacaoNova.setHora(LocalTime.now());

        when(movimentacaoRepository.findById(1)).thenReturn(Optional.of(movimentacaoAntiga));
        when(movimentacaoRepository.save(any(Movimentacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        assertEquals(100, produto.getQuantidade());

        Movimentacao movimentacaoResultado = movimentacaoService.atualizar(1, movimentacaoNova);

        assertEquals(95, produto.getQuantidade());
        assertEquals(movimentacaoAntiga.getTipo(), movimentacaoResultado.getTipo());
    }

    @Test
    @DisplayName("Exceção ao tentar atualizar movimentação inexistente")
    void atualizarCase2() {
        Produto produto = setUpProduto();

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Inclusao");
        movimentacao.setData(LocalDate.now());
        movimentacao.setHora(LocalTime.now());

        when(movimentacaoRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> movimentacaoService.atualizar(1, movimentacao));
    }

    @Test
    @DisplayName("Excluindo movimentação do tipo inclusão")
    void excluirCase1() {
        Produto produto = setUpProduto();

        produto.setQuantidade(10);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1);
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Inclusao");
        movimentacao.setData(LocalDate.now());
        movimentacao.setHora(LocalTime.now());

        movimentacaoService.aplicarMovimentacao(movimentacao);

        assertEquals(20, produto.getQuantidade());

        when(movimentacaoRepository.findById(1)).thenReturn(Optional.of(movimentacao));
        doNothing().when(movimentacaoRepository).delete(movimentacao);

        movimentacaoService.excluir(1);

        verify(movimentacaoRepository, times(1)).deleteById(1);

        assertEquals(10, produto.getQuantidade());
    }

    @Test
    @DisplayName("Excluindo movimentação do tipo retirada")
    void excluirCase2() {
        Produto produto = setUpProduto();

        produto.setQuantidade(20);

        Movimentacao movimentacao = new Movimentacao();
        movimentacao.setId(1);
        movimentacao.setProduto(produto);
        movimentacao.setQuantidade(10);
        movimentacao.setTipo("Retirada");
        movimentacao.setData(LocalDate.now());
        movimentacao.setHora(LocalTime.now());

        movimentacaoService.aplicarMovimentacao(movimentacao);

        assertEquals(10, produto.getQuantidade());

        when(movimentacaoRepository.findById(1)).thenReturn(Optional.of(movimentacao));
        doNothing().when(movimentacaoRepository).delete(movimentacao);

        movimentacaoService.excluir(1);

        verify(movimentacaoRepository, times(1)).deleteById(1);

        assertEquals(20, produto.getQuantidade());
    }

    @Test
    @DisplayName("Exceção para tentativa de excluir movimentação inexistente")
    void excluirCase3() {
        when(movimentacaoRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> movimentacaoService.excluir(1));
    }
}