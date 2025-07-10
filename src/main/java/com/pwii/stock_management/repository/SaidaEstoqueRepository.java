package com.pwii.stock_management.repository;

import com.pwii.stock_management.model.Bebida;
import com.pwii.stock_management.model.SaidaEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaidaEstoqueRepository extends JpaRepository<SaidaEstoque, Long> {
    SaidaEstoque findFirstByBebidaOrderByDataSaidaDesc(Bebida bebida);
}
