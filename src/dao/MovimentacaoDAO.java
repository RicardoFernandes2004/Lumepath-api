package dao;

import beans.Movimentacao;
import beans.TipoMovimentacao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO {
    public void insert(Connection c, Movimentacao m) throws SQLException{
        try(PreparedStatement ps=c.prepareStatement("INSERT INTO MOVIMENTACAO(TIPO,DATA_HORA,UNIDADE_ID,MATERIAL_ID,QUANTIDADE,USUARIO_ID,MOTIVO) VALUES(?,?,?,?,?,?,?)")){
            ps.setString(1, m.getTipo().name());
            ps.setTimestamp(2, Timestamp.valueOf(m.getDataHora()));
            ps.setLong(3, m.getUnidadeId());
            ps.setLong(4, m.getMaterialId());
            ps.setInt(5, m.getQuantidade());
            ps.setLong(6, m.getUsuarioId());
            ps.setString(7, m.getMotivo());
            ps.executeUpdate();
        }
    }
    public List<Movimentacao> listByPeriodo(Connection c, long unidadeId, long materialId, LocalDate inicio, LocalDate fim) throws SQLException {
        List<Movimentacao> out=new ArrayList<>();
        String sql="SELECT ID,TIPO,DATA_HORA,UNIDADE_ID,MATERIAL_ID,QUANTIDADE,USUARIO_ID,MOTIVO FROM MOVIMENTACAO WHERE UNIDADE_ID=? AND MATERIAL_ID=? AND DATA_HORA BETWEEN ? AND ? ORDER BY DATA_HORA";
        try(PreparedStatement ps=c.prepareStatement(sql)){
            ps.setLong(1,unidadeId);
            ps.setLong(2,materialId);
            ps.setTimestamp(3, Timestamp.valueOf(inicio.atStartOfDay()));
            ps.setTimestamp(4, Timestamp.valueOf(fim.plusDays(1).atStartOfDay().minusSeconds(1)));
            try(ResultSet rs=ps.executeQuery()){ while(rs.next()){
                Movimentacao m=new Movimentacao();
                m.setId(rs.getLong(1));
                m.setTipo(TipoMovimentacao.valueOf(rs.getString(2)));
                m.setDataHora(rs.getTimestamp(3).toLocalDateTime());
                m.setUnidadeId(rs.getLong(4));
                m.setMaterialId(rs.getLong(5));
                m.setQuantidade(rs.getInt(6));
                m.setUsuarioId(rs.getLong(7));
                m.setMotivo(rs.getString(8));
                out.add(m);
            }}
        } return out;
    }
}
