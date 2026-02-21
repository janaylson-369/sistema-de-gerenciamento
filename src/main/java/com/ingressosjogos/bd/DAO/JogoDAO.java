package com.ingressosjogos.bd.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import com.ingressosjogos.bd.model.Jogo;
import com.ingressosjogos.bd.util.ConnectionPostgres;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JogoDAO {
    
    
    
    public int salvarRetornandoId(Jogo jogo) throws Exception {
        String sql = "INSERT INTO Jogo(data_hora, id_estadio, id_time_casa, id_time_fora) VALUES (?, ?, ?, ?)";
        int idGerado = -1;

        try (Connection c = ConnectionPostgres.getConection();
             PreparedStatement st = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             
            st.setTimestamp(1, jogo.getDataHora());
            st.setInt(2, jogo.getIdEstadio());
            st.setInt(3, jogo.getIdTimeCasa());
            st.setInt(4, jogo.getIdTimeFora());
            st.executeUpdate();
            
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    jogo.setId(idGerado);
                }
            }
        }
        return idGerado;
    }


    public void excluirCompleto(int idJogo) throws Exception {
        // 1. Verifica se tem algum ingresso VENDIDO para este jogo
        String sqlVerifica = "SELECT COUNT(*) FROM Ingresso WHERE id_jogo = ? AND status = 'vendido'";

        try (Connection c = ConnectionPostgres.getConection();
             PreparedStatement psBusca = c.prepareStatement(sqlVerifica)) {
             
            psBusca.setInt(1, idJogo);
            try (ResultSet rs = psBusca.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    // Se a contagem for maior que 0, lança um erro para o JavaFX mostrar na tela!
                    throw new Exception("Operação bloqueada: Existem ingressos já vendidos para este jogo. O jogo não pode ser excluído.");
                }
            }
        }

        //  Se passou da verificação acima (ninguém comprou), podemos apagar os ingressos 'livres' e o jogo com segurança
        String sqlIngressos = "DELETE FROM Ingresso WHERE id_jogo=?";
        String sqlJogo = "DELETE FROM Jogo WHERE id=?";

        Connection c = ConnectionPostgres.getConection();
        try {
            c.setAutoCommit(false); 

            try (PreparedStatement ps1 = c.prepareStatement(sqlIngressos)) {
                ps1.setInt(1, idJogo);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = c.prepareStatement(sqlJogo)) {
                ps2.setInt(1, idJogo);
                ps2.executeUpdate();
            }

            c.commit(); 
        } catch (SQLException e) {
            c.rollback(); 
            throw e;
        } finally {
            c.setAutoCommit(true);
            c.close();
        }
    }
    
    public java.util.Map<String, Object> buscarJogoCompleto(int idJogo) throws Exception {
        String sql = "SELECT j.id, j.data_hora, " +
                     "e.id AS id_estadio, e.nome AS estadio, e.localizacao, e.capacidade, " +
                     "tc.id AS id_time_casa, tc.nome AS time_casa, " +
                     "tf.id AS id_time_fora, tf.nome AS time_fora, " +
                     "(SELECT preco FROM Ingresso WHERE id_jogo = j.id LIMIT 1) AS preco " +
                     "FROM Jogo j " +
                     "JOIN Estadio e ON j.id_estadio = e.id " +
                     "JOIN Times tc ON j.id_time_casa = tc.id " +
                     "JOIN Times tf ON j.id_time_fora = tf.id " +
                     "WHERE j.id = ?";

        try (Connection conn = com.ingressosjogos.bd.util.ConnectionPostgres.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idJogo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                java.util.Map<String, Object> dados = new java.util.HashMap<>();
                dados.put("id_estadio", rs.getInt("id_estadio"));
                dados.put("estadio", rs.getString("estadio"));
                dados.put("localizacao", rs.getString("localizacao"));
                dados.put("capacidade", rs.getInt("capacidade"));
                dados.put("id_time_casa", rs.getInt("id_time_casa"));
                dados.put("time_casa", rs.getString("time_casa"));
                dados.put("id_time_fora", rs.getInt("id_time_fora"));
                dados.put("time_fora", rs.getString("time_fora"));
                dados.put("data_hora", rs.getTimestamp("data_hora"));
                dados.put("preco", rs.getDouble("preco"));
                return dados;
            }
        }
        return null;
    }
    
    public List<String[]> listarJogosDetalhados() throws Exception {
        List<String[]> lista = new ArrayList<>();
        // O SQL foi movido do Controller para cá
        String sql = "SELECT j.id, j.data_hora, e.nome AS estadio, tc.nome AS time_casa, tf.nome AS time_fora, " +
                     "(SELECT MIN(preco) FROM Ingresso WHERE id_jogo = j.id AND status = 'livre') AS preco_min " +
                     "FROM Jogo j " +
                     "JOIN Estadio e ON j.id_estadio = e.id " +
                     "JOIN Times tc ON j.id_time_casa = tc.id " +
                     "JOIN Times tf ON j.id_time_fora = tf.id " +
                     "ORDER BY j.data_hora ASC";

        try (Connection conn = ConnectionPostgres.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String[] dados = {
                    String.valueOf(rs.getInt("id")),
                    rs.getString("time_casa"),
                    rs.getString("time_fora"),
                    rs.getString("estadio"),
                    rs.getTimestamp("data_hora").toString(),
                    String.valueOf(rs.getDouble("preco_min"))
                };
                lista.add(dados);
            }
        }
        return lista;
    }
    
    
    public void atualizar(Jogo jogo) throws Exception {
        String sql = "UPDATE Jogo SET data_hora=? WHERE id=?";
        try (Connection con = ConnectionPostgres.getConection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setTimestamp(1, jogo.getDataHora());
            st.setInt(2, jogo.getId());
            st.executeUpdate();
        }
    }
}
