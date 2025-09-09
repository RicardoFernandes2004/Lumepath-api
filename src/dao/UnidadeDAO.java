package dao;

import beans.Unidade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnidadeDAO {
    public Unidade findById(Connection c, long id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("SELECT ID,NOME FROM UNIDADE WHERE ID=?")){
            ps.setLong(1,id);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()) return new Unidade(rs.getLong(1), rs.getString(2));
            }
        } return null;
    }
    public List<Unidade> listAll(Connection c) throws SQLException {
        List<Unidade> out=new ArrayList<>();
        try (PreparedStatement ps=c.prepareStatement("SELECT ID,NOME FROM UNIDADE ORDER BY NOME")){
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()) out.add(new Unidade(rs.getLong(1), rs.getString(2)));
            }
        } return out;
    }
    public long insert(Connection c, Unidade u) throws SQLException {
        try(PreparedStatement ps=c.prepareStatement("INSERT INTO UNIDADE(NOME) VALUES(?)", new String[]{"ID"})){
            ps.setString(1,u.getNome()); ps.executeUpdate();
            try(ResultSet rs=ps.getGeneratedKeys()){
                if(rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }

    public int delete(Connection c, long id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM UNIDADE WHERE ID=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        }
    }
}
