package dao;

import beans.Alerta;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {
    public void inserir(Connection c, long unidadeId, long materialId) throws SQLException {
        try(PreparedStatement ps=c.prepareStatement("INSERT INTO ALERTA(UNIDADE_ID,MATERIAL_ID,GERADO_EM,LIDO) VALUES(?,?,SYSTIMESTAMP,0)")){
            ps.setLong(1,unidadeId);
            ps.setLong(2,materialId);
            ps.executeUpdate();
        }
    }
    public List<Alerta> listarAbertosPorUnidade(Connection c, long unidadeId) throws SQLException{
        List<Alerta> out=new ArrayList<>();
        try(PreparedStatement ps=c.prepareStatement("SELECT ID,UNIDADE_ID,MATERIAL_ID,GERADO_EM,LIDO FROM ALERTA WHERE UNIDADE_ID=? AND LIDO=0 ORDER BY GERADO_EM DESC")){
            ps.setLong(1,unidadeId);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                Alerta a=new Alerta(rs.getLong(1),rs.getLong(2),rs.getLong(3), rs.getTimestamp(4).toLocalDateTime(), rs.getInt(5)==1);
                out.add(a);
            }}
        } return out;
    }
    public void fecharAlertasSeAcimaDoPonto(Connection c, long unidadeId, long materialId, int saldoAtual, int pontoReposicao) throws SQLException{
        if(saldoAtual>pontoReposicao){
            try(PreparedStatement ps=c.prepareStatement("UPDATE ALERTA SET LIDO=1 WHERE UNIDADE_ID=? AND MATERIAL_ID=? AND LIDO=0")){
                ps.setLong(1,unidadeId);
                ps.setLong(2,materialId);
                ps.executeUpdate();
            }
        }
    }
}
