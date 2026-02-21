/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author janailson
 */
package com.ingressosjogos.controller;

import com.ingressosjogos.bd.DAO.*;
import com.ingressosjogos.bd.model.*;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import com.ingressosjogos.bd.DAO.JogoDAO;
import com.ingressosjogos.bd.DAO.IngressoDAO;
import java.io.IOException;

public class Tela_edicao_jogoController implements Initializable {

    @FXML private TextField campoNomeEstadio, campoLocalEstadio, campoCapacidadeEstadio;
    @FXML private TextField campoTimeCasa, campoTimeFora, campoHoraJogo, campoPrecoIngresso;
    @FXML private DatePicker campoDataJogo;
    @FXML private Label labelMensagemAdmin;

    private int idJogoEdicao, idEstadioEdicao, idTimeCasaEdicao, idTimeForaEdicao;

    @Override
    public void initialize(URL url, ResourceBundle rb) {}

    public void receberIdParaEdicao(int idJogo) {
        this.idJogoEdicao = idJogo;
        carregarDadosNaTela();
    }

  
    private void carregarDadosNaTela() {
        try {
            JogoDAO dao = new JogoDAO();
            java.util.Map<String, Object> dados = dao.buscarJogoCompleto(idJogoEdicao);

            if (dados != null) {
                // 1. GUARDA OS IDS: Essencial para o botão salvar funcionar!
                idEstadioEdicao = (int) dados.get("id_estadio");
                idTimeCasaEdicao = (int) dados.get("id_time_casa");
                idTimeForaEdicao = (int) dados.get("id_time_fora");

                // 2. PREENCHE OS CAMPOS: Coloca os textos nas caixinhas
                campoNomeEstadio.setText((String) dados.get("estadio"));
                campoLocalEstadio.setText((String) dados.get("localizacao"));
                campoCapacidadeEstadio.setText(String.valueOf(dados.get("capacidade")));
                campoTimeCasa.setText((String) dados.get("time_casa"));
                campoTimeFora.setText((String) dados.get("time_fora"));
                campoPrecoIngresso.setText(String.valueOf(dados.get("preco")));

                // 3. TRATA DATA E HORA: Converte do SQL para o JavaFX
                Timestamp ts = (Timestamp) dados.get("data_hora");
                campoDataJogo.setValue(ts.toLocalDateTime().toLocalDate());
                campoHoraJogo.setText(ts.toLocalDateTime().toLocalTime().toString());
            }
        } catch (Exception e) {
            labelMensagemAdmin.setText("Erro ao carregar dados: " + e.getMessage());
        }
    }

    @FXML
    public void salvarEdicao(ActionEvent event) {
        try {
            // Conversão de dados
            LocalDate data = campoDataJogo.getValue();
            LocalTime hora = LocalTime.parse(campoHoraJogo.getText());
            Timestamp dataHoraSQL = Timestamp.valueOf(LocalDateTime.of(data, hora));
            double preco = Double.parseDouble(campoPrecoIngresso.getText().replace(",", "."));
            int capacidade = Integer.parseInt(campoCapacidadeEstadio.getText());

            // Instancia os DAOs
            EstadioDAO estadioDAO = new EstadioDAO();
            TimeDAO timeDAO = new TimeDAO();
            JogoDAO jogoDAO = new JogoDAO();
            IngressoDAO ingressoDAO = new IngressoDAO();

            // Executa as atualizações via DAO 
            estadioDAO.atualizar(new Estadio(idEstadioEdicao, campoNomeEstadio.getText(), campoLocalEstadio.getText(), capacidade));
            timeDAO.atualizar(new Time(idTimeCasaEdicao, campoTimeCasa.getText()));
            timeDAO.atualizar(new Time(idTimeForaEdicao, campoTimeFora.getText()));
            jogoDAO.atualizar(new Jogo(idJogoEdicao, dataHoraSQL, idEstadioEdicao, idTimeCasaEdicao, idTimeForaEdicao));
            
            // Atualiza apenas os ingressos não vendidos
            ingressoDAO.atualizarPrecoLote(idJogoEdicao, preco);

            labelMensagemAdmin.setText("✅ Jogo e ingressos livres atualizados!");
            labelMensagemAdmin.setStyle("-fx-text-fill: green;");

        } catch (Exception e) {
            labelMensagemAdmin.setText("Erro ao salvar alterações: " + e.getMessage());
            labelMensagemAdmin.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    public void voltarParaJogos(ActionEvent event) {
        try {
            com.ingressosjogos.App.setRoot("tela_jogos");
        } catch (IOException e) {
        }
    }
}