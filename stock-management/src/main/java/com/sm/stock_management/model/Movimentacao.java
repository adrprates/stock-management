package com.sm.stock_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * @author Adriano
 */

@Entity
@Table(name="Movimentacoes")
public class Movimentacao {
    
    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "produto_id")
    private Produto produto;
    
    private int quantidade;
    private String tipo;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;
    
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hora;
    
    public Movimentacao() {
        this.data = LocalDate.now();
        this.hora = LocalTime.now(); 
    }

    //construtor
    public Movimentacao(Integer id, Produto produto, int quantidade, String tipo, LocalDate data, LocalTime hora) {
        this.id = id;
        this.produto = produto;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.data = data;
        this.hora = hora;
    }

    //getters e setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
}