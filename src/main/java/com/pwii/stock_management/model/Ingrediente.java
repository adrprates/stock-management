package com.pwii.stock_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "ingredientes")
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    @NotBlank
    @Size(max = 150)
    private String nome;

    @Column(name = "unidade_medida", nullable = false)
    @NotBlank
    @Size(max = 150)
    private String unidadeMedida;
}