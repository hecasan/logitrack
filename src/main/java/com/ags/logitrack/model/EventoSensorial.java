package com.ags.logitrack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class EventoSensorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tipo; // colisão, obstáculo, erro de rota
    private LocalDateTime timestamp;
    private Long roboId;

    public EventoSensorial() {
    }

    public EventoSensorial(String tipo, LocalDateTime timestamp, Long roboId) {
        this.tipo = tipo;
        this.timestamp = timestamp;
        this.roboId = roboId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getRoboId() {
        return roboId;
    }

    public void setRoboId(Long roboId) {
        this.roboId = roboId;
    }
}
