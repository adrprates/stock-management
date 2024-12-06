package com.sm.stock_management.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

/**
 *
 * @author Adriano
 */

@Entity
@Table(name="Produtos")
public class Produto {
    
    //atributos
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    private String nome;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id")  
    private Categoria categoria;
    
    private int quantidade;
    private double preco;
    private String descricao;
    
    @OneToMany(mappedBy = "produto", cascade = CascadeType.REMOVE)
    private List<Movimentacao> movimentacoes;
    
    //construtor vazio
    public Produto(){
        
    }
    
    //construtor
    public Produto(Integer id, String nome, Categoria categoria, int quantidade, double preco, String descricao) {
        this.id = id;
        this.nome = nome;
        this.categoria = categoria;
        this.quantidade = quantidade;
        this.preco = preco;
        this.descricao = descricao;
    }
    
    //getters e setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}