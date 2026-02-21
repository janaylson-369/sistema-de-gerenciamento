package com.ingressosjogos.bd.model;

public class Estadio {
    private int id;
    private String nome;
    private String localizacao;
    private int capacidade;

    public Estadio() {

    }

    public Estadio(int id, String nome, String localizacao, int capacidade) {
        this.id = id;
        this.nome = nome;
        this.localizacao = localizacao;
        this.capacidade = capacidade;
    }

    public Estadio(String nome, String localizacao, int capacidade) {
        this.nome = nome;
        this.localizacao = localizacao;
        this.capacidade = capacidade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }
    
    

}
