package com.ingressosjogos.bd.model;

public class Ingresso {
    private int id;
    private double preco;
    private String assento;
    private String status;
    private Integer idJogo;
    private Integer idTorcedor;

    public Ingresso(double preco, String assento, String status, Integer idJogo) {
        this.preco = preco;
        this.assento = assento;
        this.status = status;
        this.idJogo = idJogo;
        this.idTorcedor = null; // Torcedor fica nulo até a venda
    }

    public Ingresso(int id, double preco, String assento, String status, Integer idJogo, Integer idTorcedor) {
        this.id = id;
        this.preco = preco;
        this.assento = assento;
        this.status = status;
        this.idJogo = idJogo;
        this.idTorcedor = idTorcedor;
    }

    public Ingresso(double preco, String assento, String status, Integer idJogo, Integer idTorcedor) {
        this.preco = preco;
        this.assento = assento;
        this.status = status;
        this.idJogo = idJogo;
        this.idTorcedor = idTorcedor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getAssento() {
        return assento;
    }

    public void setAssento(String assento) {
        this.assento = assento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdJogo() {
        return idJogo;
    }

    public void setIdJogo(Integer idJogo) {
        this.idJogo = idJogo;
    }

    public Integer getIdTorcedor() {
        return idTorcedor;
    }

    public void setIdTorcedor(Integer idTorcedor) {
        this.idTorcedor = idTorcedor;
    }

    
}
