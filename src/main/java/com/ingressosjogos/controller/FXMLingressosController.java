/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.ingressosjogos.controller;

import com.ingressosjogos.bd.model.Torcedor;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FXMLingressosController implements Initializable {

    @FXML private TextField campoNome;
    @FXML private TextField campoEmail;
    @FXML private TextField campoCPF;
    @FXML private Label labelMensagem;
    
    // Recuperamos a variável da caixinha de marcação do Admin!
    @FXML private CheckBox checkAdmin; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    public void salvarTorcedor(ActionEvent event) {
        // 1. VERIFICA SE É ADMINISTRADOR
        // Se a caixinha existir na tela e estiver marcada...
        if (checkAdmin != null && checkAdmin.isSelected()) {
            com.ingressosjogos.bd.model.Sessao.isAdmin = true; // Salva como Admin
            com.ingressosjogos.bd.model.Sessao.torcedorLogado = null; // Admin não tem ingresso
            
            try {
                com.ingressosjogos.App.setRoot("tela_jogos");
            } catch (IOException e) {
            }
            return; //  Para a execução aqui, não tenta salvar o admin no banco!
        }

        // 2. SE NÃO ESTIVER MARCADA, É UM TORCEDOR COMUM
        String nome = campoNome.getText().trim();
        String email = campoEmail.getText().trim();
        String cpf = campoCPF.getText().trim();

        if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty()) {
            labelMensagem.setText("Por favor, preencha todos os campos!");
            labelMensagem.setStyle("-fx-text-fill: red;");
            return;
        } 
        
        try {
            com.ingressosjogos.bd.DAO.TorcedorDAO dao = new com.ingressosjogos.bd.DAO.TorcedorDAO();
            Torcedor torcedorDoBanco = dao.logarOuCadastrar(nome, email, cpf);
            
            com.ingressosjogos.bd.model.Sessao.torcedorLogado = torcedorDoBanco;
            com.ingressosjogos.bd.model.Sessao.isAdmin = false; // Garante que é usuário comum

            com.ingressosjogos.App.setRoot("tela_jogos");
            
        } catch (Exception e) {
            labelMensagem.setText("Erro ao salvar ou abrir a tela: " + e.getMessage());
            labelMensagem.setStyle("-fx-text-fill: red;");
        }
    }
}