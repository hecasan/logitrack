package com.ags.logitrack.repository;

import com.ags.logitrack.model.EntregaSimulada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntregaSimuladaRepository extends JpaRepository<EntregaSimulada, Long> {
}
