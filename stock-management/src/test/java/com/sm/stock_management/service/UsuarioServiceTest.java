package com.sm.stock_management.service;

import com.sm.stock_management.model.Usuario;
import com.sm.stock_management.repository.UsuarioRepository;
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

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Adicionando um novo usuário")
    void adicionarCase1() {
        Usuario usuario = new Usuario();
        usuario.setNome("Toninho");
        usuario.setEmail("toninho@gmail.com");
        usuario.setSenha("123456");
        usuario.setCargo("Estoquista");

        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Usuario usuarioResultado = usuarioService.adicionar(usuario);

        verify(usuarioRepository).save(usuario);
        assertNotNull(usuarioResultado);
        assertEquals(usuario.getEmail(), usuarioResultado.getEmail());
    }

    @Test
    @DisplayName("Exceção ao tentar adicionar um usuário nulo")
    void adicionarCase2() {
        assertThrows(NullPointerException.class, () -> usuarioService.adicionar(null));
    }

    @Test
    @DisplayName("Buscando usuário pelo id")
    void buscarPorIdCase1() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Toninho");
        usuario.setEmail("toninho@gmail.com");
        usuario.setSenha("123456");
        usuario.setCargo("Estoquista");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));

        Usuario usuarioResultado = usuarioService.buscarPorId(1);

        verify(usuarioRepository).findById(1);
        assertNotNull(usuarioResultado);
        assertEquals(usuario.getEmail(), usuarioResultado.getEmail());
    }

    @Test
    @DisplayName("Exceção ao tentar buscar usuário por id inexistente")
    void buscarPorIdCase2() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> usuarioService.buscarPorId(1));
    }

    @Test
    @DisplayName("Listagem de todos os usuários")
    void buscarTodosCase1() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setNome("Jorge");
        usuario1.setEmail("jorge@gmail.com");
        usuario1.setSenha("123456");
        usuario1.setCargo("Gerente");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setNome("Jonas");
        usuario2.setEmail("jonas@gmail.com");
        usuario2.setSenha("123456");
        usuario2.setCargo("Estoquista");

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> usuarioEncontrados = usuarioService.buscarTodos();

        assertEquals(2, usuarioEncontrados.size());
    }

    @Test
    @DisplayName("Listagem vazia de usuários")
    void buscarTodosCase2() {
        when(usuarioRepository.findAll()).thenReturn(List.of());

        List<Usuario> usuarioResultado = usuarioService.buscarTodos();

        assertTrue(usuarioResultado.isEmpty());
    }

    @Test
    @DisplayName("Busca retornando dois usuários pelo nome")
    void buscarPorNomeCase1() {
        Usuario usuario1 = new Usuario();
        usuario1.setId(1);
        usuario1.setNome("Jorge");
        usuario1.setEmail("jorge@gmail.com");
        usuario1.setSenha("123456");
        usuario1.setCargo("Gerente");

        Usuario usuario2 = new Usuario();
        usuario2.setId(2);
        usuario2.setNome("Jonas");
        usuario2.setEmail("jonas@gmail.com");
        usuario2.setSenha("123456");
        usuario2.setCargo("Estoquista");

        List<Usuario> usuarios = Arrays.asList(usuario1, usuario2);

        when(usuarioRepository.findByNomeContaining("Jo")).thenReturn(usuarios);

        List<Usuario> usuariosEncontrados = usuarioService.buscarPorNome("Jo");

        assertEquals(2, usuariosEncontrados.size());
    }

    @Test
    @DisplayName("Busca retornando nenhum usuário pelo nome")
    void buscarPorNomeCase2() {
        when(usuarioRepository.findByNomeContaining("Jo")).thenReturn(List.of());

        List<Usuario> usuarioResultado = usuarioService.buscarPorNome("Jo");

        assertTrue(usuarioResultado.isEmpty());
    }

    @Test
    @DisplayName("Atualizando usuário")
    void atualizarCase1() {
        Usuario usuarioAntigo = new Usuario();
        usuarioAntigo.setId(1);
        usuarioAntigo.setNome("Toninho");
        usuarioAntigo.setEmail("toninho@gmail.com");
        usuarioAntigo.setSenha("123456");
        usuarioAntigo.setCargo("Estoquista");

        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setNome("Antonio");
        usuarioNovo.setEmail("antonio@gmail.com");
        usuarioNovo.setSenha("123456");
        usuarioNovo.setCargo("Gerente");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuarioAntigo));
        when(usuarioRepository.save(usuarioAntigo)).thenReturn(usuarioAntigo);

        Usuario usuarioResultado = usuarioService.atualizar(1, usuarioNovo);

        assertEquals(usuarioAntigo.getNome(), usuarioResultado.getNome());
    }

    @Test
    @DisplayName("Exceção ao tentar atualizar usuário inexistente")
    void atualizarCase2() {
        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setNome("Toninho");
        usuarioNovo.setEmail("toninho@gmail.com");
        usuarioNovo.setSenha("123456");
        usuarioNovo.setCargo("Estoquista");

        when(usuarioRepository.findById(88)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> usuarioService.atualizar(88, usuarioNovo));
    }

    @Test
    @DisplayName("Excluindo usuário")
    void excluirCase1() {
        Usuario usuario = new Usuario();
        usuario.setId(1);
        usuario.setNome("Toninho");
        usuario.setEmail("toninho@gmail.com");
        usuario.setSenha("123456");
        usuario.setCargo("Estoquista");

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        doNothing().when(usuarioRepository).deleteById(1);

        usuarioService.excluir(1);

        verify(usuarioRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Exceção ao tentar excluir usuário por id inexistente")
    void excluirCase2() {
        when(usuarioRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> usuarioService.excluir(1));
    }
}