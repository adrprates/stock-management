package com.pwii.stock_management.repository;

import com.pwii.stock_management.model.ReceitaBebida;
import com.pwii.stock_management.model.ReceitaBebidaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceitaBebidaRepository extends JpaRepository<ReceitaBebida, ReceitaBebidaId> {
    void deleteById_IdBebida(Long idBebida);
    boolean existsByIdIdBebida(Long idBebida);
    boolean existsByIdIdIngrediente(Long idIngrediente);
    List<ReceitaBebida> findById_IdBebida(Long idBebida);
}
