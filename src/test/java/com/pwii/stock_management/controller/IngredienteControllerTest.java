package com.pwii.stock_management.controller;

import com.pwii.stock_management.model.Ingrediente;
import com.pwii.stock_management.service.IngredienteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(IngredienteController.class)
public class IngredienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngredienteService ingredienteService;

    @AfterEach
    void tearDown() {
        reset(ingredienteService);
    }

    private Ingrediente ingredienteExemplo() {
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setId(1L);
        ingrediente.setNome("Açúcar");
        ingrediente.setUnidadeMedida("g");
        return ingrediente;
    }

    @Test
    @WithMockUser
    @DisplayName("GET /ingrediente - Lista de ingredientes")
    void testListarIngredientes() throws Exception {
        when(ingredienteService.getAllIngredientes()).thenReturn(List.of(ingredienteExemplo()));

        mockMvc.perform(get("/ingrediente"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingrediente/index"))
                .andExpect(model().attributeExists("listIngredientes"))
                .andExpect(content().string(containsString("Açúcar")));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /ingrediente/criar - Formulário de criação")
    void testFormularioCriarIngrediente() throws Exception {
        mockMvc.perform(get("/ingrediente/criar"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingrediente/criar"))
                .andExpect(model().attributeExists("ingrediente"))
                .andExpect(content().string(containsString("Cadastrar Ingrediente")));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /ingrediente/salvar - Ingrediente válido")
    void testSalvarIngredienteValido() throws Exception {
        mockMvc.perform(post("/ingrediente/salvar")
                        .with(csrf())
                        .param("nome", "Sal")
                        .param("unidadeMedida", "g"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ingrediente"));

        verify(ingredienteService).salvarIngrediente(any(Ingrediente.class));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /ingrediente/salvar - Ingrediente inválido")
    void testSalvarIngredienteInvalido() throws Exception {
        mockMvc.perform(post("/ingrediente/salvar")
                        .with(csrf())
                        .param("nome", "")  //inválido
                        .param("unidadeMedida", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("ingrediente/criar"))
                .andExpect(model().attributeHasErrors("ingrediente"));

        verify(ingredienteService, never()).salvarIngrediente(any());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /ingrediente/editar/{id} - Exibe formulário de edição")
    void testEditarIngrediente() throws Exception {
        when(ingredienteService.getIngredienteById(1L)).thenReturn(ingredienteExemplo());

        mockMvc.perform(get("/ingrediente/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ingrediente/editar"))
                .andExpect(model().attributeExists("ingrediente"))
                .andExpect(model().attribute("ingrediente", hasProperty("nome", is("Açúcar"))));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /ingrediente/atualizar - Ingrediente válido")
    void testAtualizarIngredienteValido() throws Exception {
        mockMvc.perform(post("/ingrediente/atualizar")
                        .with(csrf())
                        .param("id", "1")
                        .param("nome", "Mel")
                        .param("unidadeMedida", "ml"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ingrediente"));

        verify(ingredienteService).salvarIngrediente(any(Ingrediente.class));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /ingrediente/atualizar - Ingrediente inválido")
    void testAtualizarIngredienteInvalido() throws Exception {
        mockMvc.perform(post("/ingrediente/atualizar")
                        .with(csrf())
                        .param("id", "1")
                        .param("nome", "")
                        .param("unidadeMedida", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("ingrediente/editar"))
                .andExpect(model().attributeHasErrors("ingrediente"));

        verify(ingredienteService, never()).salvarIngrediente(any());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /ingrediente/deletar/{id} - Deletar ingrediente")
    void testDeletarIngrediente() throws Exception {
        mockMvc.perform(get("/ingrediente/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/ingrediente"));

        verify(ingredienteService).deletarIngredienteById(1L);
    }
}