package com.ags.logitrack.repository;

import com.ags.logitrack.model.EventoSensorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoSensorialRepository extends JpaRepository<EventoSensorial, Long> {
}
