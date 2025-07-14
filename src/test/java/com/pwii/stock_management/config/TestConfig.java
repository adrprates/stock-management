package com.pwii.stock_management.config;

import com.pwii.stock_management.service.*;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public BebidaService bebidaService() {
        return Mockito.mock(BebidaService.class);
    }

    @Bean
    public ClienteService clienteService() {
        return Mockito.mock(ClienteService.class);
    }

    @Bean
    public EntradaEstoqueService entradaEstoqueService() {
        return Mockito.mock(EntradaEstoqueService.class);
    }

    @Bean
    public EstoqueService estoqueService() {
        return Mockito.mock(EstoqueService.class);
    }

    @Bean
    public IngredienteService ingredienteService() {
        return Mockito.mock(IngredienteService.class);
    }

    @Bean
    public ReceitaBebidaService receitaBebidaService() {
        return Mockito.mock(ReceitaBebidaService.class);
    }

    @Bean
    public SaidaEstoqueService saidaEstoqueService() {
        return Mockito.mock(SaidaEstoqueService.class);
    }

    @Bean
    public IUserService usuarioService() {
        return Mockito.mock(IUserService.class);
    }
}