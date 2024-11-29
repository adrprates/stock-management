package com.sm.stock_management.repository;

import com.sm.stock_management.model.Categoria;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Adriano
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    List<Categoria> findByNameContaining(String nome);
}
