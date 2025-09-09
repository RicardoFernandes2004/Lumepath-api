package dao;


import beans.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import beans.Perfil;

public class UsuarioDAO {
    public Usuario findById(Connection c, long id) throws SQLException {
        try(PreparedStatement ps=c.prepareStatement("SELECT ID,NOME,PERFIL FROM USUARIO WHERE ID=?")){
            ps.setLong(1,id);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()) return map(rs);
            }
        }
        return null;
    }
    public List<Usuario> listAll(Connection c) throws SQLException{
        List<Usuario> out=new ArrayList<>();
        try(PreparedStatement ps=c.prepareStatement("SELECT ID,NOME,PERFIL FROM USUARIO ORDER BY NOME")){
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()) out.add(map(rs));
            }
        } return out;
    }
    public long insert(Connection c, Usuario u) throws SQLException{
        try(PreparedStatement ps=c.prepareStatement("INSERT INTO USUARIO(NOME,PERFIL) VALUES(?,?)", new String[]{"ID"})){
            ps.setString(1,u.getNome());
            ps.setString(2,u.getPerfil().name());
            ps.executeUpdate();
            try(ResultSet rs=ps.getGeneratedKeys()){
                if(rs.next()) return rs.getLong(1);
            }
        }
        return -1;
    }
    private Usuario map(ResultSet rs) throws SQLException{
        return new Usuario(rs.getLong(1), rs.getString(2), Perfil.valueOf(rs.getString(3)));
    }

    public int delete(Connection c, long id) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement("DELETE FROM USUARIO WHERE ID=?")) {
            ps.setLong(1, id);
            return ps.executeUpdate();
        }
    }
}
