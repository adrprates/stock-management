package com.sm.stock_management.repository;

import com.sm.stock_management.model.Categoria;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = "spring.config.location=classpath:/application-test.properties")
class CategoriaRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private Categoria createCategoria(Categoria categoria){
        this.entityManager.persist(categoria);
        return categoria;
    }
    
    @Test
    @DisplayName("Listagem retornando duas categorias")
    void findByNomeContainingCase1(){
        Categoria categoria1 = new Categoria();
        categoria1.setNome("Categoria 1");
        this.createCategoria(categoria1);

        Categoria categoria2 = new Categoria();
        categoria2.setNome("Categoria 2");
        this.createCategoria(categoria2);

        List<Categoria> categoriasEncontradas = categoriaRepository.findByNomeContaining("Categoria");

        assertThat(categoriasEncontradas.size()).isEqualTo(2);
        assertThat(categoriasEncontradas.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Listagem retornando uma categoria")
    void findByNomeContainingCase2(){
        Categoria categoria1 = new Categoria();
        categoria1.setNome("Categoria 1");
        this.createCategoria(categoria1);

        Categoria categoria2 = new Categoria();
        categoria2.setNome("Zxy");
        this.createCategoria(categoria2);

        List<Categoria> categoriasEncontradas = categoriaRepository.findByNomeContaining("Zxy");

        assertThat(categoriasEncontradas.size()).isEqualTo(1);
        assertThat(categoriasEncontradas.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Listagem retornando nenhuma categoria")
    void findByNomeContainingCase3(){
        List<Categoria> categoriasEncontradas = categoriaRepository.findByNomeContaining("Categoria");

        assertThat(categoriasEncontradas.isEmpty()).isTrue();
    }
}