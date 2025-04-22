package com.ags.logitrack.repository;

import com.ags.logitrack.model.RoboLogistico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoboLogisticoRepository extends JpaRepository<RoboLogistico, Long> {
    // Métodos personalizados podem ser adicionados aqui, se necessário
}
