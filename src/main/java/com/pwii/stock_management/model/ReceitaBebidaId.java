package com.pwii.stock_management.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.List;

@Embeddable
public class ReceitaBebidaId implements Serializable {

    private Long idBebida;
    private Long idIngrediente;

    public ReceitaBebidaId() {}

    public ReceitaBebidaId(Long idBebida, Long idIngrediente) {
        this.idBebida = idBebida;
        this.idIngrediente = idIngrediente;
    }

}