package com.pwii.stock_management.repository;

import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.model.EntradaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntradaEstoqueRepository extends JpaRepository<EntradaEstoque, Long> {
    EntradaEstoque findFirstByBebidaOrderByDataEntradaDesc(Bebida bebida);
}