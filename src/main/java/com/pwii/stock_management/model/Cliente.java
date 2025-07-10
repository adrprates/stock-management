package com.pwii.stock_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    @NotBlank
    @Size(max = 150)
    private String nome;

    @Column(name = "cpf", nullable = false)
    @NotBlank
    @Size(max = 14)
    private String cpf;

    @Column(name = "telefone", nullable = false)
    @NotBlank
    @Size(max = 20)
    private String telefone;
}
