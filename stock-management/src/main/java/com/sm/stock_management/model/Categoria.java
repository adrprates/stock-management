package com.sm.stock_management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author Adriano
 */

@Entity
@Table(name="Categorias")
public class Categoria {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    
    //atributos
    private Integer id;
    private String nome;
    
    //construtor vazio
    public Categoria(){
        
    }

    //construtor
    public Categoria(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
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
   
}