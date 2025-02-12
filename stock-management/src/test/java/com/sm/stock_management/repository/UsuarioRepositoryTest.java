package com.sm.stock_management.repository;

import com.sm.stock_management.model.Usuario;
import jakarta.persistence.EntityManager;
import net.bytebuddy.utility.dispatcher.JavaDispatcher;
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
class UsuarioRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UsuarioRepository usuarioRepository;

    private Usuario createUsuario(Usuario usuario){
        this.entityManager.persist(usuario);
        return usuario;
    }

    @Test
    @DisplayName("Listagem retornando dois usuários pelo nome")
    void findByNomeContainingCase1() {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Joao");
        usuario1.setEmail("joao@gmail.com");
        usuario1.setSenha("123456");
        usuario1.setCargo("Estoquista");
        createUsuario(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Joana");
        usuario2.setEmail("joana@gmail.com");
        usuario2.setSenha("654321");
        usuario2.setCargo("Gerente");
        createUsuario(usuario2);

        List<Usuario> usuariosEncontrados = usuarioRepository.findByNomeContaining("Joa");

        assertThat(usuariosEncontrados.size()).isEqualTo(2);
        assertThat(usuariosEncontrados.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Listagem retornando um usuário pelo nome")
    void findByNomeContainingCase2() {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Toninho");
        usuario1.setEmail("toninho@gmail.com");
        usuario1.setSenha("123456");
        usuario1.setCargo("Estoquista");
        createUsuario(usuario1);

        Usuario usuario2 = new Usuario();
        usuario2.setNome("Joana");
        usuario2.setEmail("joaninha@gmail.com");
        usuario2.setSenha("654321");
        usuario2.setCargo("Gerente");
        createUsuario(usuario2);

        List<Usuario> usuariosEncontrados = usuarioRepository.findByNomeContaining("Toninho");

        assertThat(usuariosEncontrados.size()).isEqualTo(1);
        assertThat(usuariosEncontrados.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Listagem retornando nenhum usuário pelo nome")
    void findByNomeContainingCase3() {
        List<Usuario> usuariosEncontrados = usuarioRepository.findByNomeContaining("Tonho");

        assertThat(usuariosEncontrados.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Busca retornando e-mail existente como verdadeiro")
    void existsByEmailCase1() {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Toninho");
        usuario1.setEmail("toninho@gmail.com");
        usuario1.setSenha("123456");
        usuario1.setCargo("Estoquista");
        createUsuario(usuario1);

        boolean exists = usuarioRepository.existsByEmail(usuario1.getEmail());

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Busca retornando e-mail existente como falso")
    void existsByEmailCase2() {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Toninho");
        usuario1.setEmail("toninho@gmail.com");
        usuario1.setSenha("123456");
        usuario1.setCargo("Estoquista");
        createUsuario(usuario1);

        boolean exists = usuarioRepository.existsByEmail("maria@gmail.com");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Busca encontrando usuário pelo e-mail")
    void findByEmailCase1() {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Toninho");
        usuario1.setEmail("toninho@gmail.com");
        usuario1.setSenha("123456");
        usuario1.setCargo("Estoquista");
        createUsuario(usuario1);

        Usuario usuarioEncontrado = usuarioRepository.findByEmail(usuario1.getEmail());

        assertThat(usuarioEncontrado).isEqualTo(usuario1);
    }

    @Test
    @DisplayName("Busca não encontra usuário pelo e-mail")
    void findByEmailCase2() {
        Usuario usuario1 = new Usuario();
        usuario1.setNome("Toninho");
        usuario1.setEmail("toninho@gmail.com");
        usuario1.setSenha("123456");
        usuario1.setCargo("Estoquista");
        createUsuario(usuario1);

        Usuario usuarioEncontrado = usuarioRepository.findByEmail("teste@gmail.com");

        assertThat(usuarioEncontrado).isNotEqualTo(usuario1);
    }
}