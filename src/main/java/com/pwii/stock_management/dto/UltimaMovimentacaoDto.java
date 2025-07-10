package com.pwii.stock_management.dto;

public class UltimaMovimentacaoDto {

    private String tipo;
    private Long id;

    public UltimaMovimentacaoDto(String tipo, Long id) {
        this.tipo = tipo;
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}