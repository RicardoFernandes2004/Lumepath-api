package dao;

import beans.Material;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MaterialDAO {
    public Material findById(Connection c, long id) throws SQLException {
        try (PreparedStatement ps=c.prepareStatement("SELECT ID,CODIGO,DESCRICAO,UNIDADE_MEDIDA,PONTO_REPOSICAO FROM MATERIAL WHERE ID=?")){
            ps.setLong(1,id);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()) return map(rs);
            }
        }
        return null;
    }
    public Material findByCodigo(Connection c, String codigo) throws SQLException {
        try (PreparedStatement ps=c.prepareStatement("SELECT ID,CODIGO,DESCRICAO,UNIDADE_MEDIDA,PONTO_REPOSICAO FROM MATERIAL WHERE CODIGO=?")){
            ps.setString(1,codigo);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()) return map(rs);
            }
        }
        return null;
    }
    public List<Material> listAll(Connection c) throws SQLException {
        List<Material> out=new ArrayList<>();
        try(PreparedStatement ps=c.prepareStatement("SELECT ID,CODIGO,DESCRICAO,UNIDADE_MEDIDA,PONTO_REPOSICAO FROM MATERIAL ORDER BY CODIGO")){
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()) out.add(map(rs));
            }
        } return out;
    }
    public long insert(Connection c, Material m) throws SQLException {
        try(PreparedStatement ps=c.prepareStatement("INSERT INTO MATERIAL(CODIGO,DESCRICAO,UNIDADE_MEDIDA,PONTO_REPOSICAO) VALUES(?,?,?,?)", new String[]{"ID"})){
            ps.setString(1,m.getCodigo());
            ps.setString(2,m.getDescricao());
            ps.setString(3,m.getUnidadeMedida());
            ps.setInt(4,m.getPontoReposicao());
            ps.executeUpdate();
            try(ResultSet rs=ps.getGeneratedKeys()){
                if(rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }
    public void updatePontoReposicao(Connection c,long id,int novo) throws SQLException{
        try(PreparedStatement ps=c.prepareStatement("UPDATE MATERIAL SET PONTO_REPOSICAO=? WHERE ID=?")){
            ps.setInt(1,novo);
            ps.setLong(2,id);
            ps.executeUpdate();
        }
    }
    private Material map(ResultSet rs) throws SQLException{
        return new Material(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
    }

    public int delete(Connection c, long id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM MATERIAL WHERE ID=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        }
    }
}
