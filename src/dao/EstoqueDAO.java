package dao;

import beans.Estoque;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstoqueDAO {
    public Integer getSaldo(Connection c, long unidadeId, long materialId) throws SQLException {
        try(PreparedStatement ps=c.prepareStatement("SELECT SALDO FROM ESTOQUE WHERE UNIDADE_ID=? AND MATERIAL_ID=?")){
            ps.setLong(1,unidadeId);
            ps.setLong(2,materialId);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()) return rs.getInt(1);
            }
        }
        return null; // null = não existe ainda
    }
    public void upsertSaldo(Connection c,long unidadeId,long materialId,int novoSaldo) throws SQLException{
// Oracle MERGE para UPSERT
        String sql = "MERGE INTO ESTOQUE e USING (SELECT ? AS UNIDADE_ID, ? AS MATERIAL_ID FROM dual) s " +
                "ON (e.UNIDADE_ID = s.UNIDADE_ID AND e.MATERIAL_ID = s.MATERIAL_ID) " +
                "WHEN MATCHED THEN UPDATE SET e.SALDO = ? " +
                "WHEN NOT MATCHED THEN INSERT (UNIDADE_ID, MATERIAL_ID, SALDO) VALUES (s.UNIDADE_ID, s.MATERIAL_ID, ? )";
        try(PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,unidadeId);
            ps.setLong(2,materialId);
            ps.setInt(3,novoSaldo);
            ps.setInt(4,novoSaldo);
            ps.executeUpdate();
        }
    }
    public List<Estoque> listByUnidade(Connection c, long unidadeId) throws SQLException{
        List<Estoque> out=new ArrayList<>();
        try(PreparedStatement ps=c.prepareStatement("SELECT ID,UNIDADE_ID,MATERIAL_ID,SALDO FROM ESTOQUE WHERE UNIDADE_ID=? ORDER BY MATERIAL_ID")){
            ps.setLong(1,unidadeId);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()) out.add(new Estoque(rs.getLong(1),rs.getLong(2),rs.getLong(3),rs.getInt(4)));
            }
        } return out;
    }
}
