package com.pwii.stock_management.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "receita_bebidas")
public class ReceitaBebida {

    @EmbeddedId
    private ReceitaBebidaId id;

    @Column(name = "quantidade", nullable = false)
    private float quantidade;

    @Column(name = "unidade_medida", nullable = false)
    private String unidadeMedida;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idBebida")
    @JoinColumn(name = "bebida_id", nullable = false)
    private Bebida bebida;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idIngrediente")
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;
}
