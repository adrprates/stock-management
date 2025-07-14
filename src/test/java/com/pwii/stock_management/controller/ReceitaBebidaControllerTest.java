package com.pwii.stock_management.controller;

import com.pwii.stock_management.dto.ReceitaBebidaDto;
import com.pwii.stock_management.dto.ReceitaBebidaFormDto;
import com.pwii.stock_management.dto.ReceitaBebidaViewDto;
import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.model.Ingrediente;
import com.pwii.stock_management.service.BebidaService;
import com.pwii.stock_management.service.IngredienteService;
import com.pwii.stock_management.service.ReceitaBebidaService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(ReceitaBebidaController.class)
class ReceitaBebidaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReceitaBebidaService receitaBebidaService;

    @MockBean
    private BebidaService bebidaService;

    @MockBean
    private IngredienteService ingredienteService;

    private List<ReceitaBebidaViewDto> criarReceitasView() {
        Bebida bebida = new Bebida();
        bebida.setId(1L);
        bebida.setNome("Caipirinha");
        bebida.setTipo("Coquetel");
        bebida.setVolume("300ml");
        bebida.setUnidadeVolume("ml");

        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setId(2L);
        ingrediente.setNome("Açúcar");

        ReceitaBebidaViewDto dto = new ReceitaBebidaViewDto();
        dto.setIdBebida(1L);
        dto.setIdIngrediente(2L);
        dto.setQuantidade(50f);
        dto.setUnidade_medida("ml");
        dto.setBebida(bebida);
        dto.setIngrediente(ingrediente);

        return List.of(dto);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /receitas-bebidas - Deve mostrar a página de listagem agrupada")
    void testVerHomePage() throws Exception {
        when(receitaBebidaService.getAllReceitaBebidas()).thenReturn(criarReceitasView());

        mockMvc.perform(get("/receitas-bebidas"))
                .andExpect(status().isOk())
                .andExpect(view().name("receitas-bebidas/index"))
                .andExpect(model().attributeExists("receitasAgrupadas"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /receitas-bebidas/criar - Deve mostrar formulário de criação")
    void testCriarForm() throws Exception {
        when(bebidaService.getAllBebidas()).thenReturn(List.of());
        when(ingredienteService.getAllIngredientes()).thenReturn(List.of());

        mockMvc.perform(get("/receitas-bebidas/criar"))
                .andExpect(status().isOk())
                .andExpect(view().name("receitas-bebidas/criar"))
                .andExpect(model().attributeExists("form"))
                .andExpect(model().attributeExists("bebidas"))
                .andExpect(model().attributeExists("ingredientes"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /receitas-bebidas/salvar - Deve salvar e redirecionar")
    void testSalvarReceita() throws Exception {
        mockMvc.perform(post("/receitas-bebidas/salvar")
                        .with(csrf())
                        .param("idBebida", "1")
                        .param("ingredientes[0].idIngrediente", "2")
                        .param("ingredientes[0].quantidade", "50")
                        .param("ingredientes[0].unidadeMedida", "ml"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/receitas-bebidas"));

        verify(receitaBebidaService).salvarReceitas(org.mockito.ArgumentMatchers.any(ReceitaBebidaFormDto.class));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /receitas-bebidas/deletar/{idBebida} - Deve deletar e redirecionar")
    void testDeletarReceita() throws Exception {
        mockMvc.perform(get("/receitas-bebidas/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/receitas-bebidas"));

        verify(receitaBebidaService).deletarReceitaByIdBebida(1L);
    }
}