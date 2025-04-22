package com.ags.logitrack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "evento_sensorial")
public class EventoSensorial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "robo_id")
    private RoboLogistico robo;

    private String tipoSensor;
    private String leitura;
    private LocalDateTime dataHora;
    private String localizacao;
    private String status;

    // Construtores
    public EventoSensorial() {
    }

    public EventoSensorial(RoboLogistico robo, String tipoSensor, String leitura,
                           LocalDateTime dataHora, String localizacao, String status) {
        this.robo = robo;
        this.tipoSensor = tipoSensor;
        this.leitura = leitura;
        this.dataHora = dataHora;
        this.localizacao = localizacao;
        this.status = status;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoboLogistico getRobo() {
        return robo;
    }

    public void setRobo(RoboLogistico robo) {
        this.robo = robo;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(String tipoSensor) {
        this.tipoSensor = tipoSensor;
    }

    public String getLeitura() {
        return leitura;
    }

    public void setLeitura(String leitura) {
        this.leitura = leitura;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
