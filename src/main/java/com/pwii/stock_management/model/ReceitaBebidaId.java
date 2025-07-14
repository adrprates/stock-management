package com.pwii.stock_management.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReceitaBebidaId implements Serializable {

    @Column(name = "bebida_id")
    private Long idBebida;

    @Column(name = "ingrediente_id")
    private Long idIngrediente;

    public ReceitaBebidaId() {}

    public Long getIdBebida() {
        return idBebida;
    }

    public void setIdBebida(Long idBebida) {
        this.idBebida = idBebida;
    }

    public Long getIdIngrediente() {
        return idIngrediente;
    }

    public void setIdIngrediente(Long idIngrediente) {
        this.idIngrediente = idIngrediente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReceitaBebidaId that = (ReceitaBebidaId) o;
        return Objects.equals(idBebida, that.idBebida) &&
                Objects.equals(idIngrediente, that.idIngrediente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBebida, idIngrediente);
    }
}