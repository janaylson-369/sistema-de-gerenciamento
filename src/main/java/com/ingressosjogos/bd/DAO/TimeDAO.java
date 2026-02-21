package com.ingressosjogos.bd.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import com.ingressosjogos.bd.model.Time;
import com.ingressosjogos.bd.util.ConnectionPostgres;

public class TimeDAO {
    
    public int salvarRetornandoId(Time time) throws Exception {
        String sqlBusca = "SELECT id FROM Times WHERE nome = ?";
        
        try (Connection c = ConnectionPostgres.getConection();
             PreparedStatement psBusca = c.prepareStatement(sqlBusca)) {
            
            psBusca.setString(1, time.getNome());
            try (ResultSet rs = psBusca.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        String sqlCriarTime = "INSERT INTO Times(nome) VALUES (?)";
        int idGerado = -1;

        try (Connection c = ConnectionPostgres.getConection();
             PreparedStatement psInsert = c.prepareStatement(sqlCriarTime, Statement.RETURN_GENERATED_KEYS)) {
             
            psInsert.setString(1, time.getNome());
            psInsert.executeUpdate();
            try (ResultSet rs = psInsert.getGeneratedKeys()) {
                if (rs.next()) {
                    idGerado = rs.getInt(1);
                    time.setId(idGerado);
                }
            }
        }
        return idGerado;
    }
    
    public void atualizar(Time time) throws Exception {
    String sql = "UPDATE Times SET nome=? WHERE id=?";
    try (Connection con = ConnectionPostgres.getConection();
         PreparedStatement st = con.prepareStatement(sql)) {
        st.setString(1, time.getNome());
        st.setInt(2, time.getId());
        st.executeUpdate();
    }
}
}
