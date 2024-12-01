package com.sm.stock_management.repository;

import com.sm.stock_management.model.Movimentacao;
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
    List<Movimentacao> findByData(Date data);
}
