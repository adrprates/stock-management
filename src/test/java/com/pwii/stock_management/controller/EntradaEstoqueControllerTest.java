package com.pwii.stock_management.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDateTime;
import java.util.List;

import com.pwii.stock_management.dto.EntradaEstoqueDto;
import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.model.EntradaEstoque;
import com.pwii.stock_management.service.BebidaService;
import com.pwii.stock_management.service.EntradaEstoqueService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EntradaEstoqueController.class)
public class EntradaEstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntradaEstoqueService entradaEstoqueService;

    @MockBean
    private BebidaService bebidaService;

    private List<EntradaEstoqueDto> criarListaEntradaEstoqueDto() {
        EntradaEstoqueDto dto1 = new EntradaEstoqueDto();
        dto1.setId(1L);
        dto1.setIdBebida(1L);
        dto1.setNome("Cerveja");
        dto1.setTipo("Lata");
        dto1.setVolume("350ml");
        dto1.setQuantidade(20);
        dto1.setDataEntrada(LocalDateTime.now().minusDays(1));
        dto1.setUltimaMovimentacao(true);

        EntradaEstoqueDto dto2 = new EntradaEstoqueDto();
        dto2.setId(2L);
        dto2.setIdBebida(2L);
        dto2.setNome("Vinho");
        dto2.setTipo("Garrafa");
        dto2.setVolume("750ml");
        dto2.setQuantidade(10);
        dto2.setDataEntrada(LocalDateTime.now().minusDays(2));
        dto2.setUltimaMovimentacao(false);

        return List.of(dto1, dto2);
    }

    private List<Bebida> criarListaBebidas() {
        Bebida b1 = new Bebida();
        b1.setId(1L);
        b1.setNome("Cerveja");
        b1.setTipo("Lata");
        b1.setVolume("350ml");

        Bebida b2 = new Bebida();
        b2.setId(2L);
        b2.setNome("Vinho");
        b2.setTipo("Garrafa");
        b2.setVolume("750ml");

        return List.of(b1, b2);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /entradas-estoque - Deve mostrar a lista de entradas")
    void testVerHomePage() throws Exception {
        when(entradaEstoqueService.getAllEntradasEstoque()).thenReturn(criarListaEntradaEstoqueDto());

        mockMvc.perform(get("/entradas-estoque"))
                .andExpect(status().isOk())
                .andExpect(view().name("entradas-estoque/index"))
                .andExpect(model().attributeExists("listEntrada"))
                .andExpect(model().attribute("listEntrada", hasSize(2)))
                .andExpect(model().attribute("listEntrada", hasItem(
                        allOf(
                                hasProperty("id", is(1L)),
                                hasProperty("nome", is("Cerveja")),
                                hasProperty("ultimaMovimentacao", is(true))
                        )
                )));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /entradas-estoque/criar - Deve preparar formulário de criação")
    void testCriarEntrada() throws Exception {
        when(bebidaService.getAllBebidas()).thenReturn(criarListaBebidas());

        mockMvc.perform(get("/entradas-estoque/criar"))
                .andExpect(status().isOk())
                .andExpect(view().name("entradas-estoque/criar"))
                .andExpect(model().attributeExists("entradaEstoque"))
                .andExpect(model().attributeExists("bebidas"))
                .andExpect(model().attribute("bebidas", hasSize(2)));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /entradas-estoque/salvar - Deve salvar nova entrada e redirecionar")
    void testSalvarEntrada() throws Exception {
        doNothing().when(entradaEstoqueService).salvarEntradaEstoque(any(EntradaEstoque.class));

        mockMvc.perform(post("/entradas-estoque/salvar")
                        .with(csrf())
                        .param("quantidade", "15")
                        //uso do parâmetro nested "bebida.id para relacionar a bebida no form"
                        .param("bebida.id", "1")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/entradas-estoque"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /entradas-estoque/deletar/{id} - Deve deletar entrada e redirecionar")
    void testDeletarEntrada() throws Exception {
        doNothing().when(entradaEstoqueService).deletarEntradaById(1L);

        mockMvc.perform(get("/entradas-estoque/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/entradas-estoque"));
    }
}
