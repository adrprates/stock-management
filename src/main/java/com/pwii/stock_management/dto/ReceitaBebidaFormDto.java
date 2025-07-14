package com.pwii.stock_management.dto;

import java.util.ArrayList;
import java.util.List;

public class ReceitaBebidaFormDto {

    private Long idBebida;
    private List<ReceitaBebidaDto> ingredientes = new ArrayList<>();

    public Long getIdBebida() {
        return idBebida;
    }

    public void setIdBebida(Long idBebida) {
        this.idBebida = idBebida;
    }

    public List<ReceitaBebidaDto> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<ReceitaBebidaDto> ingredientes) {
        this.ingredientes = ingredientes;
    }
}
