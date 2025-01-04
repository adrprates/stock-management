package com.sm.stock_management.repository;

import com.sm.stock_management.model.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Adriano
 */

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    List<Usuario> findByNomeContaining(String nome);
    boolean existsByEmail(String email);
    Usuario findByEmail(String email);
}
