package service;

import beans.Movimentacao;
import beans.TipoMovimentacao;
import dao.ConnectionFactory;
import dao.EstoqueDAO;
import dao.MaterialDAO;
import dao.MovimentacaoDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class RelatorioService {
    private final EstoqueDAO estoqueDAO = new EstoqueDAO();
    private final MovimentacaoDAO movDAO = new MovimentacaoDAO();
    private final MaterialDAO materialDAO = new MaterialDAO();


    public String saldoPorUnidade(long unidadeId) throws SQLException {
        try(Connection c = ConnectionFactory.getConnection()){
            StringBuilder sb=new StringBuilder("Saldo atual\n");
            String sql = "SELECT e.MATERIAL_ID, m.CODIGO, m.DESCRICAO, e.SALDO, m.UNIDADE_MEDIDA FROM ESTOQUE e JOIN MATERIAL m ON m.ID=e.MATERIAL_ID WHERE e.UNIDADE_ID=? ORDER BY m.CODIGO";
            try(PreparedStatement ps=c.prepareStatement(sql)){
                ps.setLong(1,unidadeId);
                try(ResultSet rs=ps.executeQuery()){
                    while(rs.next()) sb.append(String.format("%s - %s: %d %s\n", rs.getString(2), rs.getString(3), rs.getInt(4), rs.getString(5)));
                }
            }
            return sb.toString();
        }
    }


    public String consumoPorPeriodo(long unidadeId, long materialId, LocalDate ini, LocalDate fim) throws SQLException{
        try(Connection c = ConnectionFactory.getConnection()){
            var lista = movDAO.listByPeriodo(c, unidadeId, materialId, ini, fim);
            var mat = materialDAO.findById(c, materialId);
            StringBuilder sb=new StringBuilder("Consumo "+mat.getCodigo()+" ("+ini+" a "+fim+")\n");
            for(Movimentacao m: lista) if(m.getTipo()== TipoMovimentacao.SAIDA) sb.append(m.getDataHora()+" - "+m.getQuantidade()+"\n");
            return sb.toString();
        }
    }
}
