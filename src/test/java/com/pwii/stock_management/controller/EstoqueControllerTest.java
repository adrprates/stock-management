package com.pwii.stock_management.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import com.pwii.stock_management.dto.EstoqueDto;
import com.pwii.stock_management.service.BebidaService;
import com.pwii.stock_management.service.EstoqueService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EstoqueController.class)
public class EstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstoqueService estoqueService;

    @MockBean
    private BebidaService bebidaService;

    private List<EstoqueDto> criarListaEstoqueDto() {
        EstoqueDto dto1 = new EstoqueDto();
        dto1.setIdBebida(1L);
        dto1.setNome("Cerveja");
        dto1.setTipo("Lata");
        dto1.setVolume("350ml");
        dto1.setQuantidade(10);
        dto1.setDescricao("Estoque de cerveja");

        EstoqueDto dto2 = new EstoqueDto();
        dto2.setIdBebida(2L);
        dto2.setNome("Vinho");
        dto2.setTipo("Garrafa");
        dto2.setVolume("750ml");
        dto2.setQuantidade(5);
        dto2.setDescricao("Estoque de vinho");

        return List.of(dto1, dto2);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /estoque - Deve retornar a página com lista de EstoqueDto")
    void testVerHomePageComDto() throws Exception {
        when(estoqueService.getAllEstoque()).thenReturn(criarListaEstoqueDto());

        mockMvc.perform(get("/estoque"))
                .andExpect(status().isOk())
                .andExpect(view().name("estoque/index"))
                .andExpect(model().attributeExists("listEstoque"))
                .andExpect(model().attribute("listEstoque", hasSize(2)))
                .andExpect(model().attribute("listEstoque", hasItem(
                        allOf(
                                hasProperty("idBebida", is(1L)),
                                hasProperty("nome", is("Cerveja")),
                                hasProperty("tipo", is("Lata")),
                                hasProperty("volume", is("350ml")),
                                hasProperty("quantidade", is(10)),
                                hasProperty("descricao", is("Estoque de cerveja"))
                        )
                )));
    }
}
