package com.ingressosjogos.controller;

import com.ingressosjogos.bd.DAO.IngressoDAO;
import com.ingressosjogos.bd.DAO.JogoDAO;
import com.ingressosjogos.bd.model.Sessao;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader; 
import javafx.scene.Parent;

public class Tela_jogosController implements Initializable {
    
    @FXML private Button btnCadastrarAdmin;
    @FXML private Button btnMinhasCompras;
    @FXML private Label lblNomeUsuario;
    @FXML private FlowPane painelJogos;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            if (Sessao.isAdmin) {
                lblNomeUsuario.setText("Painel Administrador");
                btnMinhasCompras.setVisible(false);
                btnMinhasCompras.setManaged(false);
                btnCadastrarAdmin.setVisible(true);
            } else {
                lblNomeUsuario.setText("Olá, " + Sessao.torcedorLogado.getNome());
                btnCadastrarAdmin.setVisible(false);
                btnCadastrarAdmin.setManaged(false);
                btnMinhasCompras.setVisible(true);
            }
            carregarJogosDoBanco();
        } catch (Exception ex) {
        }
    }

    private void carregarJogosDoBanco() throws Exception {
        painelJogos.getChildren().clear();

        try {
            // Chamada limpa ao DAO
            JogoDAO dao = new JogoDAO();
            List<String[]> jogos = dao.listarJogosDetalhados();

            for (String[] jogo : jogos) {
                VBox cartao = criarCartaoJogoDinamicamente(
                    Integer.parseInt(jogo[0]), // id
                    jogo[1],                   // casa
                    jogo[2],                   // fora
                    jogo[3],                   // estadio
                    java.sql.Timestamp.valueOf(jogo[4]), // data
                    Double.parseDouble(jogo[5]) // preco
                );
                painelJogos.getChildren().add(cartao);
            }

        } catch (SQLException e) {
            System.out.println("Erro ao buscar os jogos: " + e.getMessage());
        }
    }

    private VBox criarCartaoJogoDinamicamente(int idJogo, String casa, String fora, String estadio, Timestamp data, double preco) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(20));
        card.setPrefSize(250, 330); 
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        HBox hboxTimes = new HBox(10);
        hboxTimes.setAlignment(Pos.CENTER);
        
        Label lblCasa = new Label(casa);
        lblCasa.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        Label lblX = new Label(" X ");
        lblX.setStyle("-fx-font-weight: bold; -fx-text-fill: #888;");
        Label lblFora = new Label(fora);
        lblFora.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");
        
        hboxTimes.getChildren().addAll(lblCasa, lblX, lblFora);

        Label lblEstadio = new Label("📍 " + estadio);
        lblEstadio.setStyle("-fx-text-fill: #555; -fx-font-size: 14px;");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'às' HH:mm");
        Label lblData = new Label("📅 " + sdf.format(data));
        lblData.setStyle("-fx-text-fill: #555; -fx-font-size: 14px; -fx-background-color: #f0f0f0; -fx-padding: 5; -fx-background-radius: 5;");
        lblData.setMaxWidth(Double.MAX_VALUE);
        lblData.setAlignment(Pos.CENTER);

        HBox hboxCompra = new HBox(20);
        hboxCompra.setAlignment(Pos.CENTER_LEFT);
        
        String textoPreco = (preco > 0) ? String.format("R$ %.2f", preco) : "Esgotado";
        Label lblPreco = new Label(textoPreco);
        lblPreco.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #28a745;");
        
        Button btnComprar = new Button("Comprar");
        btnComprar.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white; -fx-background-radius: 5; -fx-font-weight: bold; -fx-cursor: hand;");
        if (preco <= 0) btnComprar.setDisable(true); 
        
        btnComprar.setOnAction(e -> {
            if (Sessao.isAdmin) {
                new Alert(Alert.AlertType.WARNING, "Administradores não podem comprar ingressos.", ButtonType.OK).show();
            } else {
                Alert modalConfirmacao = new Alert(Alert.AlertType.CONFIRMATION);
                modalConfirmacao.setTitle("Confirmar Compra");
                modalConfirmacao.setHeaderText("Você está prestes a garantir seu ingresso!");
                modalConfirmacao.setContentText("Deseja confirmar a compra para:\n\n⚽ " + casa + " X " + fora + "\n💳 Valor: " + textoPreco);

                if (modalConfirmacao.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
                    efetuarCompra(idJogo); 
                }
            }
        });

        hboxCompra.getChildren().addAll(lblPreco, btnComprar);

        if (Sessao.isAdmin) {
            HBox hboxAdmin = new HBox(10);
            hboxAdmin.setAlignment(Pos.CENTER);
            Button btnEditar = new Button("✏️ Editar");
            btnEditar.setStyle("-fx-background-color: #17a2b8; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
            btnEditar.setOnAction(e -> editarJogo(idJogo));

            Button btnExcluir = new Button("🗑️ Excluir");
            btnExcluir.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-background-radius: 5; -fx-cursor: hand;");
            btnExcluir.setOnAction(e -> excluirJogo(idJogo));

            hboxAdmin.getChildren().addAll(btnEditar, btnExcluir);
            card.getChildren().addAll(hboxTimes, lblEstadio, lblData, hboxCompra, hboxAdmin);
        } else {
            card.getChildren().addAll(hboxTimes, lblEstadio, lblData, hboxCompra);
        }

        return card;    
    }

    private void excluirJogo(int idJogo) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar Exclusão");
        alert.setHeaderText("Excluir Jogo?");
        alert.setContentText("Você tem certeza que deseja excluir este jogo? Todos os ingressos vinculados a ele serão apagados!");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            try {
                JogoDAO dao = new JogoDAO();
                dao.excluirCompleto(idJogo);
                carregarJogosDoBanco();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }
        }
    }

    private void editarJogo(int idJogo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/tela_edicao_jogo.fxml"));
            Parent root = loader.load();
            Tela_edicao_jogoController controller = loader.getController();
            controller.receberIdParaEdicao(idJogo);
            painelJogos.getScene().setRoot(root);
        } catch (IOException e) {
        }
    }
    
    @FXML
    
    private void efetuarCompra(int idJogo) {
        try {
            IngressoDAO dao = new IngressoDAO();
            int idTorcedor = Sessao.torcedorLogado.getId(); // Pega da sessão
            // Chama a lógica do DAO
            boolean sucesso = dao.comprarIngresso(idJogo, idTorcedor);
            
            if (sucesso) {
                new Alert(Alert.AlertType.INFORMATION, "✅ Compra realizada com sucesso!", ButtonType.OK).showAndWait();
                carregarJogosDoBanco(); 
            } else {
                new Alert(Alert.AlertType.ERROR, "❌ Ingressos esgotados!", ButtonType.OK).show();
            }
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Erro ao processar compra: " + e.getMessage()).show();
        }
    }

    

    @FXML
    public void abrirTelaCadastro() {
        try {
            com.ingressosjogos.App.setRoot("tela_cadastro_jogo");
        } catch (IOException e) {
            System.out.println("Erro ao abrir cadastro: " + e.getMessage());
        }
    }

    @FXML
    public void abrirMinhasCompras() { 
        try {
            com.ingressosjogos.App.setRoot("tela_minhas_compras");
        } catch (IOException e) {
            System.out.println("Erro ao abrir minhas compras: " + e.getMessage());
        }
    }

}