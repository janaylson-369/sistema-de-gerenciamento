package com.ingressosjogos.bd.model;

import java.sql.Timestamp;

public class Jogo {
    private int id;
    private Timestamp dataHora;
    private Integer idEstadio;
    private Integer idTimeCasa;
    private Integer idTimeFora;

    public Jogo() {
        
    }

    public Jogo(int id, Timestamp dataHora, Integer idEstadio, Integer idTimeCasa, Integer idTimeFora) {
        this.id = id;
        this.dataHora = dataHora;
        this.idEstadio = idEstadio;
        this.idTimeCasa = idTimeCasa;
        this.idTimeFora = idTimeFora;
    }

    public Jogo(Timestamp dataHora, Integer idEstadio, Integer idTimeCasa, Integer idTimeFora) {
        this.dataHora = dataHora;
        this.idEstadio = idEstadio;
        this.idTimeCasa = idTimeCasa;
        this.idTimeFora = idTimeFora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getDataHora() {
        return dataHora;
    }

    public void setDataHora(Timestamp dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getIdEstadio() {
        return idEstadio;
    }

    public void setIdEstadio(Integer idEstadio) {
        this.idEstadio = idEstadio;
    }

    public Integer getIdTimeCasa() {
        return idTimeCasa;
    }

    public void setIdTimeCasa(Integer idTimeCasa) {
        this.idTimeCasa = idTimeCasa;
    }

    public Integer getIdTimeFora() {
        return idTimeFora;
    }

    public void setIdTimeFora(Integer idTimeFora) {
        this.idTimeFora = idTimeFora;
    }

    
}
