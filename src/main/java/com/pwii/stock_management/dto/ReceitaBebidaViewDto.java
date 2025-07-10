package com.pwii.stock_management.dto;

public class ReceitaBebidaViewDto {

    private String nomeIngrediente;
    private Double quantidade;
    private String unidadeMedida;

    public ReceitaBebidaViewDto(String nomeIngrediente, Double quantidade, String unidadeMedida) {
        this.nomeIngrediente = nomeIngrediente;
        this.quantidade = quantidade;
        this.unidadeMedida = unidadeMedida;
    }

    public void setNomeIngrediente(String nomeIngrediente) {
        this.nomeIngrediente = nomeIngrediente;
    }

    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }

    public void setUnidadeMedida(String unidadeMedida) {
        this.unidadeMedida = unidadeMedida;
    }
}