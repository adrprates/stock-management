package com.sm.stock_management.repository;

import com.sm.stock_management.model.Movimentacao;
import com.sm.stock_management.model.Produto;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Adriano
 */

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer>{
    List<Movimentacao> findByDataAndProduto(Date data, Produto produto);
    List<Movimentacao> findByProduto(Produto produto);
}
