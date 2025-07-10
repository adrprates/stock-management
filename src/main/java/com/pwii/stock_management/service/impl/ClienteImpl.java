package com.pwii.stock_management.service.impl;

import com.pwii.stock_management.model.Cliente;
import com.pwii.stock_management.repository.ClienteRepository;
import com.pwii.stock_management.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public List<Cliente> getAllClientes () {
        return clienteRepository.findAll();
    }

    @Override
    public void salvarCliente(Cliente cliente) {
        clienteRepository.save(cliente);
    }

    @Override
    public Cliente getClienteById(Long id) {
        Optional<Cliente> optional = clienteRepository.findById(id);
        Cliente cliente = null;
        if(optional.isPresent()){
            cliente = optional.get();
        } else{
            throw new RuntimeException("Cliente não encontrado para o id " + id);
        }
        return cliente;
    }

    @Override
    public void deletarClienteById(Long id) {
        clienteRepository.deleteById(id);
    }
}
