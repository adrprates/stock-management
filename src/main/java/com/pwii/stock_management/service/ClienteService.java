package com.pwii.stock_management.service;

import com.pwii.stock_management.model.Cliente;

import java.util.List;

public interface ClienteService {
    List<Cliente> getAllClientes();
    void salvarCliente(Cliente cliente  );
    Cliente getClienteById(Long id);
    void deletarClienteById(Long id);
}
