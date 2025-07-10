package com.pwii.stock_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "estoque")
public class Estoque {

    @Id
    @Column(name = "id_bebida", nullable = false)
    private Long idBebida;

    @Column(name = "quantidade")
    private int quantidade;

    @Column(name = "descricao")
    @NotBlank
    @Size(max = 200)
    private String descricao;
}
