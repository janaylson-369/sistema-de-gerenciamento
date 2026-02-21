package com.ingressosjogos.bd.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;


import com.ingressosjogos.bd.model.Torcedor;
import com.ingressosjogos.bd.util.ConnectionPostgres;

public class TorcedorDAO {

    
    
    public Torcedor logarOuCadastrar(String nome, String email, String cpf) throws Exception {
        // torcedor já existe pelo CPF
        String sqlBusca = "SELECT * FROM Torcedor WHERE cpf = ?";
        try (Connection c = ConnectionPostgres.getConection();
             PreparedStatement ps = c.prepareStatement(sqlBusca)) {
            ps.setString(1, cpf);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                // Se achou, retorna o torcedor do banco com o ID dele!
                return new Torcedor(rs.getInt("id"), rs.getString("nome"), rs.getString("email"), rs.getString("cpf"));
            }
        }
        
        // cadastrar
        String sqlInsert = "INSERT INTO Torcedor (nome, email, cpf) VALUES (?, ?, ?)";
        try (Connection c = ConnectionPostgres.getConection();
             PreparedStatement ps = c.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.setString(2, email);
            ps.setString(3, cpf);
            ps.executeUpdate();
            
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return new Torcedor(rs.getInt(1), nome, email, cpf);
            }
        }
        return null;
    }

    
}
