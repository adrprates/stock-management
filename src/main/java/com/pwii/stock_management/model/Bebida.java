package com.pwii.stock_management.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "bebidas")
public class Bebida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    @NotBlank
    @Size(max = 100)
    private String nome;

    @Column(name = "volume")
    @NotBlank
    @Size(max = 100)
    private String volume;

    @Column(name = "unidade_volume")
    @NotBlank
    @Size(max = 10)
    private String unidadeVolume;

    @Column(name = "tipo")
    @NotBlank
    @Size(max = 100)
    private String tipo;

    @OneToMany(mappedBy = "bebida", cascade = CascadeType.ALL)
    private List<EntradaEstoque> entradasEstoque = new ArrayList<>();

    @OneToMany(mappedBy = "bebida", cascade = CascadeType.ALL)
    private List<SaidaEstoque> saidasEstoque = new ArrayList<>();
}