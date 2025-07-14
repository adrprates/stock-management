package com.pwii.stock_management.controller;

import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.service.BebidaService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BebidaController.class)
public class BebidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BebidaService bebidaService;

    @AfterEach
    void resetMocks() {
        reset(bebidaService);
    }

    private List<Bebida> testCreateBebidaList() {
        Bebida bebida = new Bebida();
        bebida.setId(1L);
        bebida.setNome("Bebida Teste");
        bebida.setVolume("1");
        bebida.setUnidadeVolume("L");
        bebida.setTipo("Refrigerante");
        return List.of(bebida);
    }

    @Test
    @DisplayName("GET /bebida - redireciona se não autenticado")
    void testIndexUnauthorized() throws Exception {
        mockMvc.perform(get("/bebida"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /bebida - Lista bebidas com sucesso")
    void testIndexAuthenticated() throws Exception {
        when(bebidaService.getAllBebidas()).thenReturn(testCreateBebidaList());

        mockMvc.perform(get("/bebida"))
                .andExpect(status().isOk())
                .andExpect(view().name("bebida/index"))
                .andExpect(model().attributeExists("listBebidas"))
                .andExpect(content().string(containsString("Bebida Teste")));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /bebida/criar - Exibe formulario de criacao")
    void testFormCriar() throws Exception {
        mockMvc.perform(get("/bebida/criar"))
                .andExpect(status().isOk())
                .andExpect(view().name("bebida/criar"))
                .andExpect(model().attributeExists("bebida"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /bebida/salvar - Validação falha e retorna para formulário")
    void testSalvarBebidaComErroValidacao() throws Exception {
        Bebida bebida = new Bebida(); //nome em branco gera erro de validação

        mockMvc.perform(post("/bebida/salvar")
                        .with(csrf())
                        .flashAttr("bebida", bebida))
                .andExpect(status().isOk())
                .andExpect(view().name("bebida/criar"))
                .andExpect(model().attributeHasErrors("bebida"));

        verify(bebidaService, never()).salvarBebida(any());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /bebida/salvar - Bebida válida é salva com sucesso")
    void testSalvarBebidaValida() throws Exception {
        Bebida bebida = new Bebida();
        bebida.setNome("Coca-Cola");
        bebida.setVolume("2");
        bebida.setUnidadeVolume("L");
        bebida.setTipo("Refrigerante");

        mockMvc.perform(post("/bebida/salvar")
                        .with(csrf())
                        .flashAttr("bebida", bebida))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bebida"));

        verify(bebidaService).salvarBebida(any(Bebida.class));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /bebida/editar/{id} - Exibe formulário de edição")
    void testEditarBebida() throws Exception {
        Bebida bebida = testCreateBebidaList().get(0);
        when(bebidaService.getBebidaById(1L)).thenReturn(bebida);

        mockMvc.perform(get("/bebida/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bebida/editar"))
                .andExpect(model().attributeExists("bebida"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /bebida/atualizar - Atualiza bebida com sucesso")
    void testAtualizarBebidaValida() throws Exception {
        Bebida bebida = testCreateBebidaList().get(0);

        mockMvc.perform(post("/bebida/atualizar")
                        .with(csrf())
                        .flashAttr("bebida", bebida))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bebida"));

        verify(bebidaService).salvarBebida(any(Bebida.class));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /bebida/deletar/{id} - Deleta bebida com sucesso")
    void testDeletarBebida() throws Exception {
        mockMvc.perform(get("/bebida/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bebida"));

        verify(bebidaService).deletarBebidaById(1L);
    }
}