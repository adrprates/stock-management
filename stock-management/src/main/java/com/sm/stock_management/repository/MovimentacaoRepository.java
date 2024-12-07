package com.sm.stock_management.repository;

import com.sm.stock_management.model.Movimentacao;
import com.sm.stock_management.model.Produto;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Adriano
 */

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Integer>{
    List<Movimentacao> findByDataAndProduto(LocalDate data, Produto produto);
    List<Movimentacao> findByProduto(Produto produto);
    Movimentacao findTopByOrderByDataDescHoraDesc();
}
