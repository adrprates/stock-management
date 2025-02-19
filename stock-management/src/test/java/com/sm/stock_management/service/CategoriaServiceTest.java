package com.sm.stock_management.service;

import com.sm.stock_management.model.Categoria;
import com.sm.stock_management.repository.CategoriaRepository;
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

class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Adicionar: adicionando categoria")
    void adicionarCase1() {
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria");

        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria categoriaResultado = categoriaService.adicionar(categoria);

        verify(categoriaRepository).save(categoria);
        assertNotNull(categoriaResultado);
        assertEquals("Categoria", categoriaResultado.getNome());
    }

    @Test
    @DisplayName("Adicionar: exceção ao tentar adicionar uma categoria nula")
    void adicionarCase2() {
        assertThrows(NullPointerException.class, () -> categoriaService.adicionar(null));
    }

    @Test
    @DisplayName("Buscar por id: buscando categoria pelo id")
    void buscarPorIdCase1() {
        Categoria categoria1 = new Categoria();
        categoria1.setId(1);
        categoria1.setNome("Categoria Um");

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria1));

        Categoria categoriaResultado = categoriaService.buscarPorId(1);

        assertNotNull(categoriaResultado);
        assertEquals(1, categoriaResultado.getId());
        assertEquals("Categoria Um", categoriaResultado.getNome());
    }

    @Test
    @DisplayName("Buscar por id: exceção ao tentar buscar categoria por id inexistente")
    void buscarPorIdCase2() {
        when(categoriaRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> categoriaService.buscarPorId(99));
    }

    @Test
    @DisplayName("Buscar todas: listagem de todas as categorias")
    void buscarTodasCase1() {
        Categoria categoria1 = new Categoria();
        categoria1.setId(1);
        categoria1.setNome("Categoria Um");

        Categoria categoria2 = new Categoria();
        categoria2.setId(2);
        categoria2.setNome("Categoria Dois");

        List<Categoria> categorias = Arrays.asList(categoria1, categoria2);

        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> categoriasResultado = categoriaService.buscarTodas();

        assertEquals(2, categoriasResultado.size());
    }

    @Test
    @DisplayName("Buscar todas: listagem vazia de categorias")
    void buscarTodasCase2() {
        when(categoriaRepository.findAll()).thenReturn(List.of());

        List<Categoria> categoriasResultado = categoriaService.buscarTodas();

        assertTrue(categoriasResultado.isEmpty());
    }

    @Test
    @DisplayName("Busca por nome: busca retornando duas categorias pelo nome")
    void buscarPorNomeCase1() {
        Categoria categoria1 = new Categoria();
        categoria1.setId(1);
        categoria1.setNome("Categoria Um");

        Categoria categoria2 = new Categoria();
        categoria2.setId(2);
        categoria2.setNome("Categoria Dois");

        List<Categoria> categorias = Arrays.asList(categoria1, categoria2);

        when(categoriaRepository.findByNomeContaining("Categoria")).thenReturn(categorias);

        List<Categoria> categoriasResultado = categoriaService.buscarPorNome("Categoria");

        assertEquals(2, categoriasResultado.size());
    }

    @Test
    @DisplayName("Busca por nome: busca retornando nenhuma categoria pelo nome")
    void buscarPorNomeCase2() {
        when(categoriaRepository.findByNomeContaining("ZXY")).thenReturn(List.of());

        List<Categoria> categoriasResultado = categoriaService.buscarPorNome("ZXY");

        assertTrue(categoriasResultado.isEmpty());
    }

    @Test
    @DisplayName("Atualizar: atualizando categoria")
    void atualizarCase1() {
        Categoria categoriaAntiga = new Categoria();
        categoriaAntiga.setId(1);
        categoriaAntiga.setNome("Antiga");

        Categoria categoriaNova = new Categoria();
        categoriaNova.setNome("Nova");

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoriaAntiga));
        when(categoriaRepository.save(categoriaAntiga)).thenReturn(categoriaAntiga);

        Categoria categoriaResultado = categoriaService.atualizar(1, categoriaNova);

        assertEquals(categoriaNova.getNome(), categoriaResultado.getNome());
    }

    @Test
    @DisplayName("Atualizar: exceção ao tentar atualizar categoria inexistente")
    void atualizarCase2() {
        Categoria categoria = new Categoria();
        categoria.setNome("Categoria");

        when(categoriaRepository.findById(8)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> categoriaService.atualizar(8, categoria));
    }

    @Test
    @DisplayName("Excluir: excluindo categoria")
    void excluirCase1() {
        Categoria categoria = new Categoria();
        categoria.setId(1);
        categoria.setNome("Categoria Um");

        when(categoriaRepository.findById(1)).thenReturn(Optional.of(categoria));
        doNothing().when(categoriaRepository).deleteById(1);

        categoriaService.excluir(1);

        verify(categoriaRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Excluir: exceção ao tentar excluir categoria por id inexistente")
    void excluirCase2() {
        when(categoriaRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> categoriaService.excluir(1));
    }
}