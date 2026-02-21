package com.ingressosjogos.bd.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.ingressosjogos.bd.model.Ingresso;
import com.ingressosjogos.bd.util.ConnectionPostgres;

public class IngressoDAO {
    
    public List<String[]> listarIngressosDetalhadosPorTorcedor(int idTorcedor) throws Exception {
        List<String[]> lista = new ArrayList<>();
        // O SQL que estava no seu Controller agora mora aqui! 
        String sql = "SELECT i.assento, i.preco, j.data_hora, e.nome AS estadio, tc.nome AS time_casa, tf.nome AS time_fora " +
                     "FROM Ingresso i " +
                     "JOIN Jogo j ON i.id_jogo = j.id " +
                     "JOIN Estadio e ON j.id_estadio = e.id " +
                     "JOIN Times tc ON j.id_time_casa = tc.id " +
                     "JOIN Times tf ON j.id_time_fora = tf.id " +
                     "WHERE i.id_torcedor = ? AND i.status = 'vendido' " +
                     "ORDER BY j.data_hora ASC";

        try (Connection conn = ConnectionPostgres.getConection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idTorcedor);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Criamos um array de strings com os dados para a tela ler
                String[] dados = {
                    rs.getString("time_casa") + " X " + rs.getString("time_fora"), // Confronto
                    rs.getString("estadio"),                                     // Estádio
                    rs.getTimestamp("data_hora").toString(),                     // Data
                    rs.getString("assento"),                                     // Assento
                    String.valueOf(rs.getDouble("preco"))                        // Preço
                };
                lista.add(dados);
            }
        }
        return lista;
    }
    
    
    
    public void salvarEmLote(List<Ingresso> ingressos) throws Exception {
        String sql = "INSERT INTO Ingresso (preco, assento, status, id_jogo, id_torcedor) VALUES (?, ?, ?, ?, ?)";

        try (Connection c = ConnectionPostgres.getConection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            
            c.setAutoCommit(false);

            for (Ingresso ingresso : ingressos) {
                ps.setDouble(1, ingresso.getPreco());
                ps.setString(2, ingresso.getAssento());
                ps.setString(3, ingresso.getStatus());
                ps.setInt(4, ingresso.getIdJogo());
                ps.setNull(5, java.sql.Types.INTEGER); // torcedor começa nulo
                
                // Adiciona na "caixa" em vez de mandar direto pro banco
                ps.addBatch(); 
            }

            // Executa a caixa inteira de uma vez e confirma no banco!
            ps.executeBatch();
            c.commit();
        }
    }

    
    
    public void atualizarPrecoLote(int idJogo, double novoPreco) throws Exception {
        String sql = "UPDATE Ingresso SET preco=? WHERE id_jogo=? AND status='livre'";
        try (Connection con = ConnectionPostgres.getConection();
             PreparedStatement st = con.prepareStatement(sql)) {
            st.setDouble(1, novoPreco);
            st.setInt(2, idJogo);
            st.executeUpdate();
        }
    }
    

    
    
    public boolean comprarIngresso(int idJogo, int idTorcedor) throws Exception {
        String sql = "UPDATE Ingresso SET status = 'vendido', id_torcedor = ? " +
                     "WHERE id_ingresso IN (SELECT id_ingresso FROM Ingresso WHERE id_jogo = ? AND status = 'livre' LIMIT 1)";
        
        try (Connection c = ConnectionPostgres.getConection();
             PreparedStatement ps = c.prepareStatement(sql)) {
             
            ps.setInt(1, idTorcedor);
            ps.setInt(2, idJogo);
            
            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0; // Retorna true se conseguiu comprar, false se esgotou
        }
    }
}
