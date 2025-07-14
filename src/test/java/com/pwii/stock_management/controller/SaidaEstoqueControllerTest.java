package com.pwii.stock_management.controller;

import com.pwii.stock_management.dto.SaidaEstoqueDto;
import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.model.Cliente;
import com.pwii.stock_management.service.BebidaService;
import com.pwii.stock_management.service.ClienteService;
import com.pwii.stock_management.service.SaidaEstoqueService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SaidaEstoqueController.class)
public class SaidaEstoqueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaidaEstoqueService saidaEstoqueService;

    @MockBean
    private BebidaService bebidaService;

    @MockBean
    private ClienteService clienteService;

    private List<SaidaEstoqueDto> criarListaSaidasEstoqueDto() {
        SaidaEstoqueDto dto = new SaidaEstoqueDto();
        dto.setId(1L);
        dto.setIdBebida(1L);
        dto.setNome("Vinho");
        dto.setTipo("Garrafa");
        dto.setVolume("750ml");
        dto.setQuantidade(3);
        dto.setIdCliente(2L);
        dto.setNomeCliente("Maria");
        dto.setCpfCliente("123.456.789-00");
        dto.setDataSaida(LocalDateTime.now());
        dto.setUltimaMovimentacao(true);

        return List.of(dto);
    }

    private List<Bebida> criarListaBebidas() {
        Bebida b1 = new Bebida();
        b1.setId(1L);
        b1.setNome("Vinho");
        b1.setTipo("Garrafa");
        b1.setVolume("750ml");
        return List.of(b1);
    }

    private List<Cliente> criarListaClientes() {
        Cliente c1 = new Cliente();
        c1.setId(2L);
        c1.setNome("Maria");
        c1.setCpf("123.456.789-00");
        return List.of(c1);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /saidas-estoque - Deve exibir lista de saídas")
    void testVerHomePage() throws Exception {
        when(saidaEstoqueService.getAllSaidasEstoque()).thenReturn(criarListaSaidasEstoqueDto());

        mockMvc.perform(get("/saidas-estoque"))
                .andExpect(status().isOk())
                .andExpect(view().name("saidas-estoque/index"))
                .andExpect(model().attributeExists("listSaida"))
                .andExpect(model().attribute("listSaida", hasSize(1)))
                .andExpect(model().attribute("listSaida", hasItem(
                        allOf(
                                hasProperty("nome", is("Vinho")),
                                hasProperty("nomeCliente", is("Maria")),
                                hasProperty("quantidade", is(3))
                        )
                )));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /saidas-estoque/criar - Deve exibir formulário de criação")
    void testCriarSaida() throws Exception {
        when(bebidaService.getAllBebidas()).thenReturn(criarListaBebidas());
        when(clienteService.getAllClientes()).thenReturn(criarListaClientes());

        mockMvc.perform(get("/saidas-estoque/criar"))
                .andExpect(status().isOk())
                .andExpect(view().name("saidas-estoque/criar"))
                .andExpect(model().attributeExists("saidaEstoque"))
                .andExpect(model().attributeExists("bebidas"))
                .andExpect(model().attributeExists("clientes"))
                .andExpect(model().attribute("bebidas", hasSize(1)))
                .andExpect(model().attribute("clientes", hasSize(1)));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /saidas-estoque/salvar - Deve salvar saída e redirecionar")
    void testSalvarSaida() throws Exception {
        doNothing().when(saidaEstoqueService).salvarSaidaEstoque(any());

        mockMvc.perform(post("/saidas-estoque/salvar")
                        .with(csrf())
                        .param("quantidade", "5")
                        .param("bebida.id", "1")
                        .param("cliente.id", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/saidas-estoque"));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /saidas-estoque/deletar/{id} - Deve excluir e redirecionar")
    void testDeletarSaida() throws Exception {
        doNothing().when(saidaEstoqueService).deletarSaidaById(1L);

        mockMvc.perform(get("/saidas-estoque/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/saidas-estoque"));
    }
}
