package com.pwii.stock_management.controller;

import com.pwii.stock_management.model.Cliente;
import com.pwii.stock_management.service.ClienteService;
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

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @AfterEach
    void resetMocks() {
        reset(clienteService);
    }

    private Cliente clienteExemplo() {
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome("João da Silva");
        cliente.setCpf("12345678900");
        cliente.setTelefone("(66) 99876-4321");
        return cliente;
    }

    @Test
    @DisplayName("GET /cliente - Redireciona se não estiver autenticado")
    void testIndexNotAuthenticated() throws Exception {
        mockMvc.perform(get("/cliente"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /cliente - Lista clientes")
    void testIndex() throws Exception {
        when(clienteService.getAllClientes()).thenReturn(List.of(clienteExemplo()));

        mockMvc.perform(get("/cliente"))
                .andExpect(status().isOk())
                .andExpect(view().name("cliente/index"))
                .andExpect(model().attributeExists("listClientes"))
                .andExpect(content().string(containsString("João da Silva")));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /cliente/criar - Exibe formulário de criação")
    void testCriarForm() throws Exception {
        mockMvc.perform(get("/cliente/criar"))
                .andExpect(status().isOk())
                .andExpect(view().name("cliente/criar"))
                .andExpect(model().attributeExists("cliente"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /cliente/salvar - Falha de validação retorna para o formulário")
    void testSalvarErroValidacao() throws Exception {
        Cliente cliente = new Cliente(); //inválido (campos obrigatórios ausentes)

        mockMvc.perform(post("/cliente/salvar")
                        .with(csrf())
                        .flashAttr("cliente", cliente))
                .andExpect(status().isOk())
                .andExpect(view().name("cliente/criar"))
                .andExpect(model().attributeHasErrors("cliente"));

        verify(clienteService, never()).salvarCliente(any());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /cliente/salvar - Cliente salvo com sucesso")
    void testSalvarCliente() throws Exception {
        Cliente cliente = clienteExemplo();

        mockMvc.perform(post("/cliente/salvar")
                        .with(csrf())
                        .flashAttr("cliente", cliente))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cliente"));

        verify(clienteService).salvarCliente(any(Cliente.class));
    }

    @Test
    @WithMockUser
    @DisplayName("GET /cliente/editar/{id} - Exibe formulário de edição")
    void testEditarCliente() throws Exception {
        when(clienteService.getClienteById(1L)).thenReturn(clienteExemplo());

        mockMvc.perform(get("/cliente/editar/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("cliente/editar"))
                .andExpect(model().attributeExists("cliente"))
                .andExpect(model().attribute("cliente", hasProperty("nome", is("João da Silva"))));

    }

    @Test
    @WithMockUser
    @DisplayName("POST /cliente/atualizar - Falha de validação retorna para o formulário de edição")
    void testAtualizarErroValidacao() throws Exception {
        Cliente cliente = new Cliente(); // inválido

        mockMvc.perform(post("/cliente/atualizar")
                        .with(csrf())
                        .flashAttr("cliente", cliente))
                .andExpect(status().isOk())
                .andExpect(view().name("cliente/editar"))
                .andExpect(model().attributeHasErrors("cliente"));

        verify(clienteService, never()).salvarCliente(any());
    }

    @Test
    @WithMockUser
    @DisplayName("POST /cliente/atualizar - Atualização bem-sucedida")
    void testAtualizarCliente() throws Exception {
        Cliente cliente = clienteExemplo();

        mockMvc.perform(post("/cliente/atualizar")
                        .with(csrf())
                        .flashAttr("cliente", cliente))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cliente"));

        verify(clienteService).salvarCliente(any());
    }

    @Test
    @WithMockUser
    @DisplayName("GET /cliente/deletar/{id} - Cliente deletado")
    void testDeletarCliente() throws Exception {
        mockMvc.perform(get("/cliente/deletar/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cliente"));

        verify(clienteService).deletarClienteById(1L);
    }
}