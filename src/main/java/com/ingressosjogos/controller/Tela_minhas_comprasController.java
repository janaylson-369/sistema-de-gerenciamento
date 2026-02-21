/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ingressosjogos.controller;

import com.ingressosjogos.bd.DAO.IngressoDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class Tela_minhas_comprasController implements Initializable {

    @FXML
    private FlowPane painelIngressos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        carregarMeusIngressos();
    }

    private void carregarMeusIngressos() {
        painelIngressos.getChildren().clear();
        int idTorcedor = com.ingressosjogos.bd.model.Sessao.torcedorLogado.getId(); // 

        try {
            IngressoDAO dao = new IngressoDAO();
            List<String[]> ingressos = dao.listarIngressosDetalhadosPorTorcedor(idTorcedor);

            for (String[] dado : ingressos) {
                // Usamos os dados vindos do DAO para montar o bilhete
                VBox bilhete = criarBilheteVisual(
                    dado[0], // Confronto
                    dado[1], // Estádio
                    Timestamp.valueOf(dado[2]), // Data
                    dado[3], // Assento
                    Double.parseDouble(dado[4]) // Preço
                );
                painelIngressos.getChildren().add(bilhete);
            }

            if (painelIngressos.getChildren().isEmpty()) {
                Label vazio = new Label("Você ainda não comprou ingressos 😢");
                vazio.setStyle("-fx-font-size: 18px; -fx-text-fill: #888;");
                painelIngressos.getChildren().add(vazio);
            }
        } catch (Exception e) {
        }
    }
    private VBox criarBilheteVisual(String confronto, String estadio, Timestamp data, String assento, double preco) {
        VBox ticket = new VBox(10);
        ticket.setPadding(new Insets(15));
        ticket.setPrefSize(350, 180);
        // Visual de "Cupom" com borda tracejada
        ticket.setStyle("-fx-background-color: #fff9e6; -fx-border-color: #ff8c00; -fx-border-width: 2; -fx-border-style: dashed; -fx-background-radius: 10; -fx-border-radius: 10;");

        Label lblConfronto = new Label("⚽ " + confronto);
        lblConfronto.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #333;");

        Label lblEstadio = new Label("📍 Estádio: " + estadio);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
        Label lblData = new Label("📅 " + sdf.format(data));

        HBox rodape = new HBox(20);
        rodape.setAlignment(Pos.CENTER_LEFT);
        rodape.setStyle("-fx-padding: 10 0 0 0; -fx-border-color: #ccc; -fx-border-width: 1 0 0 0; -fx-border-style: dashed;");
        
        Label lblAssento = new Label("💺 Assento: " + assento);
        lblAssento.setStyle("-fx-font-weight: bold; -fx-text-fill: #d9534f;");
        
        Label lblPreco = new Label(String.format("💳 Pago: R$ %.2f", preco));
        lblPreco.setStyle("-fx-font-weight: bold; -fx-text-fill: #28a745;");

        rodape.getChildren().addAll(lblAssento, lblPreco);

        ticket.getChildren().addAll(lblConfronto, lblEstadio, lblData, rodape);
        return ticket;
    }

    @FXML
    public void voltarParaJogos(javafx.event.ActionEvent event) {
        try {
            com.ingressosjogos.App.setRoot("tela_jogos");
        } catch (IOException e) {
        }
    }
}