package com.pwii.stock_management.repository;

import com.pwii.stock_management.model.Bebida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BebidaRepository extends JpaRepository<Bebida, Long> {
}


