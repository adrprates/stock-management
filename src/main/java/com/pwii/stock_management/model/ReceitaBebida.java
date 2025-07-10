package com.pwii.stock_management.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "receita_bebidas")
public class ReceitaBebida {

    @EmbeddedId
    private ReceitaBebidaId id = new ReceitaBebidaId();

    @ManyToOne @MapsId("idBebida")
    private Bebida bebida;

    @ManyToOne
    @MapsId("idIngrediente")
    private Ingrediente ingrediente;

    private Double quantidade;
}
