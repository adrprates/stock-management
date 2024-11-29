package com.sm.stock_management.repository;

import com.sm.stock_management.model.Categoria;
import java.util.List;

/**
 *
 * @author Adriano
 */
public interface CategoriaRepository {
    List<Categoria> findByNameContaining(String nome);
}
