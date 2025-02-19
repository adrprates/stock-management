package com.sm.stock_management.repository;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.model.Produto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.location=classpath:/application-test.properties")
class ProdutoRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ProdutoRepository produtoRepository;

    private Produto createProduto(Produto produto){
        this.entityManager.persist(produto);
        return produto;
    }

    private Categoria setUpCategoria(){
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria");
        this.entityManager.persist(categoria);
        return categoria;
    }

    @Test
    @DisplayName("Buscar por nome que contém: listagem retornando dois produtos pelo nome")
    void findByNomeContainingCase1() {
        Categoria categoria = setUpCategoria();

        Produto produto1 = new Produto();
        produto1.setNome("Produto 1");
        produto1.setCategoria(categoria);
        produto1.setQuantidade(10);
        produto1.setPreco(1.00);
        produto1.setDescricao("Melhor produto de todos");
        createProduto(produto1);

        Produto produto2 = new Produto();
        produto2.setNome("Produto 2");
        produto2.setCategoria(categoria);
        produto2.setQuantidade(10);
        produto2.setPreco(1.00);
        produto1.setDescricao("Pior produto de todos");
        createProduto(produto2);

        List<Produto> produtosEncontrados = produtoRepository.findByNomeContaining("Produto");

        assertThat(produtosEncontrados.size()).isEqualTo(2);
        assertThat(produtosEncontrados.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Buscar por nome que contém: listagem retornando um produto pelo nome")
    void findByNomeContainingCase2() {
        Produto produto1 = new Produto();
        produto1.setNome("Produto 1");
        produto1.setCategoria(setUpCategoria());
        produto1.setQuantidade(10);
        produto1.setPreco(1.00);
        produto1.setDescricao("Melhor produto de todos");
        createProduto(produto1);

        Produto produto2 = new Produto();
        produto2.setNome("Teste");
        produto2.setCategoria(setUpCategoria());
        produto2.setQuantidade(10);
        produto2.setPreco(1.00);
        produto1.setDescricao("Pior produto de todos");
        createProduto(produto2);

        List<Produto> produtosEncontrados = produtoRepository.findByNomeContaining("Teste");

        assertThat(produtosEncontrados.size()).isEqualTo(1);
        assertThat(produtosEncontrados.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Buscar por nome que contém: listagem retornando nenhum produto pelo nome")
    void findByNomeContainingCase3() {
        List<Produto> produtosEncontrados = produtoRepository.findByNomeContaining("Teste");

        assertThat(produtosEncontrados.isEmpty()).isTrue();
    }
}