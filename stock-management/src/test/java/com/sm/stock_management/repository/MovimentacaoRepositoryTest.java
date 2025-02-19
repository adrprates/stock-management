package com.sm.stock_management.repository;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.model.Movimentacao;
import com.sm.stock_management.model.Produto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.location=classpath:/application-test.properties")
class MovimentacaoRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    MovimentacaoRepository movimentacaoRepository;

    private Movimentacao createMovimentacao(Movimentacao movimentacao) {
        this.entityManager.persist(movimentacao);
        return movimentacao;
    }

    private Categoria setUpCategoria(){
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria");
        this.entityManager.persist(categoria);
        return categoria;
    }

    private Produto setUpProduto() {
        Produto produto = new Produto();
        produto.setNome("Produto");
        Categoria categoria = setUpCategoria();
        produto.setCategoria(categoria);
        produto.setQuantidade(10);
        produto.setPreco(1.00);
        produto.setDescricao("Melhor produto de todos");
        this.entityManager.persist(produto);
        return produto;
    }

    private LocalDate setUpData() {
        return LocalDate.of(2025, 1, 1);
    }

    private LocalTime setUpHora() {
        return LocalTime.of(8, 0);
    }

    @Test
    @DisplayName("Buscar por data e produto: listagem retornando duas movimentações pela data e produto")
    void findByDataAndProdutoCase1() {
        Produto produto = setUpProduto();
        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setProduto(produto);
        movimentacao1.setQuantidade(10);
        movimentacao1.setTipo("Inclusao");
        movimentacao1.setData(setUpData());
        movimentacao1.setHora(setUpHora());
        createMovimentacao(movimentacao1);

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setProduto(produto);
        movimentacao2.setQuantidade(10);
        movimentacao2.setTipo("Retirada");
        movimentacao2.setData(setUpData());
        movimentacao2.setHora(setUpHora());
        createMovimentacao(movimentacao2);

        List<Movimentacao> movimentacoesEncontradas = movimentacaoRepository.findByDataAndProduto(setUpData(), produto);

        assertThat(movimentacoesEncontradas.size()).isEqualTo(2);
        assertThat(movimentacoesEncontradas.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Buscar por data e produto: listagem retornando uma movimentação pela data e produto")
    void findByDataAndProdutoCase2() {
        Produto produto = setUpProduto();
        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setProduto(produto);
        movimentacao1.setQuantidade(10);
        movimentacao1.setTipo("Inclusao");
        movimentacao1.setData(setUpData());
        movimentacao1.setHora(setUpHora());
        createMovimentacao(movimentacao1);

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setProduto(produto);
        movimentacao2.setQuantidade(10);
        movimentacao2.setTipo("Retirada");
        movimentacao2.setData(setUpData().plusDays(1));
        movimentacao2.setHora(setUpHora());
        createMovimentacao(movimentacao2);

        List<Movimentacao> movimentacoesEncontradas = movimentacaoRepository.findByDataAndProduto(setUpData(), produto);

        assertThat(movimentacoesEncontradas.size()).isEqualTo(1);
        assertThat(movimentacoesEncontradas.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Buscar por data e produto: listagem nenhuma movimentação pela data e produto")
    void findByDataAndProdutoCase3() {
        Produto produto = setUpProduto();

        List<Movimentacao> movimentacoesEncontradas = movimentacaoRepository.findByDataAndProduto(setUpData(), produto);

        assertThat(movimentacoesEncontradas.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Buscar por produto: listagem retornando duas movimentações pelo produto")
    void findByProdutoCase1() {
        Produto produto = setUpProduto();
        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setProduto(produto);
        movimentacao1.setQuantidade(10);
        movimentacao1.setTipo("Inclusao");
        movimentacao1.setData(setUpData());
        movimentacao1.setHora(setUpHora());
        createMovimentacao(movimentacao1);

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setProduto(produto);
        movimentacao2.setQuantidade(10);
        movimentacao2.setTipo("Retirada");
        movimentacao2.setData(setUpData());
        movimentacao2.setHora(setUpHora());
        createMovimentacao(movimentacao2);

        List<Movimentacao> movimentacoesEncontradas = movimentacaoRepository.findByProduto(produto);

        assertThat(movimentacoesEncontradas.size()).isEqualTo(2);
        assertThat(movimentacoesEncontradas.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Buscar por produto: listagem retornando uma movimentação pelo produto")
    void findByProdutoCase2() {
        Produto produto = setUpProduto();
        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setProduto(produto);
        movimentacao1.setQuantidade(10);
        movimentacao1.setTipo("Inclusao");
        movimentacao1.setData(setUpData());
        movimentacao1.setHora(setUpHora());
        createMovimentacao(movimentacao1);

        List<Movimentacao> movimentacoesEncontradas = movimentacaoRepository.findByProduto(produto);

        assertThat(movimentacoesEncontradas.size()).isEqualTo(1);
        assertThat(movimentacoesEncontradas.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Buscar por produto: lista vazia de movimentação por produto")
    void findByProdutoCase3() {
        Produto produto = setUpProduto();

        List<Movimentacao> movimentacoesEncontradas = movimentacaoRepository.findByProduto(produto);

        assertThat(movimentacoesEncontradas.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Buscar última movimentação: busca retornando a movimentação mais recente")
    void findTopByOrderByDataDescHoraDescCase1() {
        Produto produto = setUpProduto();
        Movimentacao movimentacao1 = new Movimentacao();
        movimentacao1.setProduto(produto);
        movimentacao1.setQuantidade(10);
        movimentacao1.setTipo("Inclusao");
        movimentacao1.setData(setUpData());
        movimentacao1.setHora(setUpHora());
        createMovimentacao(movimentacao1);

        Movimentacao movimentacao2 = new Movimentacao();
        movimentacao2.setProduto(produto);
        movimentacao2.setQuantidade(10);
        movimentacao2.setTipo("Retirada");
        movimentacao2.setData(setUpData());
        movimentacao2.setHora(setUpHora());
        createMovimentacao(movimentacao2);

        Movimentacao moovimentacaoEncontrada = movimentacaoRepository.findTopByOrderByDataDescHoraDesc();

        assertThat(moovimentacaoEncontrada).isNotNull();
        assertThat(moovimentacaoEncontrada).isEqualTo(movimentacao1);
    }

    @Test
    @DisplayName("Buscar última movimentação: busca retornando nenhuma movimentação")
    void findTopByOrderByDataDescHoraDescCase2() {

        Movimentacao moovimentacaoEncontrada = movimentacaoRepository.findTopByOrderByDataDescHoraDesc();

        assertThat(moovimentacaoEncontrada).isNull();
    }
}