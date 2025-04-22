package com.ags.logitrack.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "robo_logistico")
public class RoboLogistico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String modelo;
    private String status;
    private Double nivelBateria;
    private String localizacao;

    // Construtores
    public RoboLogistico() {
    }

    public RoboLogistico(String nome, String modelo, String status, Double nivelBateria, String localizacao) {
        this.nome = nome;
        this.modelo = modelo;
        this.status = status;
        this.nivelBateria = nivelBateria;
        this.localizacao = localizacao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getNivelBateria() {
        return nivelBateria;
    }

    public void setNivelBateria(Double nivelBateria) {
        this.nivelBateria = nivelBateria;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }
}
