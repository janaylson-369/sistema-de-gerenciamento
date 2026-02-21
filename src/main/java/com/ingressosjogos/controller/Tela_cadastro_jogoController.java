/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ingressosjogos.controller;

import com.ingressosjogos.bd.DAO.EstadioDAO;
import com.ingressosjogos.bd.DAO.IngressoDAO;
import com.ingressosjogos.bd.DAO.JogoDAO;
import com.ingressosjogos.bd.DAO.TimeDAO;
import com.ingressosjogos.bd.model.Estadio;
import com.ingressosjogos.bd.model.Ingresso;
import com.ingressosjogos.bd.model.Jogo;
import com.ingressosjogos.bd.model.Time;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Tela_cadastro_jogoController implements Initializable {

    @FXML
    private TextField campoNomeEstadio;
    @FXML
    private TextField campoLocalEstadio;
    @FXML
    private TextField campoCapacidadeEstadio;


    @FXML
    private TextField campoTimeCasa;
    @FXML
    private TextField campoTimeFora;


    @FXML
    private DatePicker campoDataJogo;
    @FXML
    private TextField campoHoraJogo;


    @FXML
    private TextField campoPrecoIngresso;


    @FXML
    private Label labelMensagemAdmin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
    
    @FXML
    public void voltarParaJogos(ActionEvent event) {
        try {
            com.ingressosjogos.App.setRoot("tela_jogos");
        } catch (IOException e) {
            System.out.println("Erro ao voltar para a tela de jogos: " + e.getMessage());
        }
    }

    @FXML
    public void salvarCadastroCompleto(ActionEvent event) {

        String nomeEstadio = campoNomeEstadio.getText();
        String localEstadio = campoLocalEstadio.getText();
        String capacidadeTexto = campoCapacidadeEstadio.getText();
        String timeCasa = campoTimeCasa.getText();
        String timeFora = campoTimeFora.getText();
        LocalDate data = campoDataJogo.getValue();
        String horaTexto = campoHoraJogo.getText();
        String precoTexto = campoPrecoIngresso.getText();

        if (nomeEstadio.isEmpty() || capacidadeTexto.isEmpty() || timeCasa.isEmpty() || timeFora.isEmpty() || data == null || horaTexto.isEmpty() || precoTexto.isEmpty()) {
            labelMensagemAdmin.setText("Por favor, preencha todos os campos obrigatórios.");
            labelMensagemAdmin.setStyle("-fx-text-fill: red;");
            return;
        }

        // Conversões de (Texto para Número / Data)
        int capacidade;
        double precoBase;
        Timestamp dataHoraBanco;

        try {
            capacidade = Integer.parseInt(capacidadeTexto);
            precoBase = Double.parseDouble(precoTexto.replace(",", ".")); 
            
            LocalTime hora = LocalTime.parse(horaTexto);
            LocalDateTime dataHoraCompleta = LocalDateTime.of(data, hora);
            dataHoraBanco = Timestamp.valueOf(dataHoraCompleta);
            
        } catch (NumberFormatException e) {
            labelMensagemAdmin.setText("Erro de formato: Verifique a Capacidade, Preço (use ponto) e Hora (HH:MM).");
            labelMensagemAdmin.setStyle("-fx-text-fill: red;");
            return;
        }

        // ultimo passo salvar no Banco de Dados
        try {

            Estadio estadio = new Estadio(nomeEstadio, localEstadio, capacidade);
            EstadioDAO estadioDAO = new EstadioDAO();
            int idEstadio = estadioDAO.salvarRetornandoId(estadio);


            TimeDAO timeDAO = new TimeDAO();
            int idTimeCasa = timeDAO.salvarRetornandoId(new Time(timeCasa));
            int idTimeFora = timeDAO.salvarRetornandoId(new Time(timeFora));


            Jogo jogo = new Jogo(dataHoraBanco, idEstadio, idTimeCasa, idTimeFora);
            JogoDAO jogoDAO = new JogoDAO();
            int idJogo = jogoDAO.salvarRetornandoId(jogo);


            List<Ingresso> listaIngressos = new ArrayList<>();
            for(int i = 1; i <= capacidade; i++) {
                String assento = "A" + i; 
                Ingresso ingresso = new Ingresso(precoBase, assento, "livre", idJogo);
                listaIngressos.add(ingresso);
            }
            
            // salva todos os ingressos no banco de uma vez so
            IngressoDAO ingressoDAO = new IngressoDAO();
            ingressoDAO.salvarEmLote(listaIngressos);
            labelMensagemAdmin.setText("Partida e " + capacidade + " ingressos cadastrados com sucesso!");
            labelMensagemAdmin.setStyle("-fx-text-fill: green;");

        } catch (Exception e) {
            labelMensagemAdmin.setText("Erro ao salvar no banco: " + e.getMessage());
            labelMensagemAdmin.setStyle("-fx-text-fill: red;");
        }
    }
    
    
}