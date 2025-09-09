package service;

import beans.Material;
import beans.Movimentacao;
import beans.TipoMovimentacao;
import dao.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EstoqueService {
    private final EstoqueDAO estoqueDAO = new EstoqueDAO();
    private final MovimentacaoDAO movDAO = new MovimentacaoDAO();
    private final MaterialDAO materialDAO = new MaterialDAO();
    private final AlertaDAO alertaDAO = new AlertaDAO();


    // 1) Entrada
    public void registrarEntrada(long unidadeId, long materialId, int qtd, long usuarioId) throws SQLException {
        try(Connection c = ConnectionFactory.getConnection()){
            c.setAutoCommit(false);
            Integer saldo = estoqueDAO.getSaldo(c, unidadeId, materialId);
            if(saldo==null) saldo=0;
            int novo = saldo + qtd;
            estoqueDAO.upsertSaldo(c, unidadeId, materialId, novo);
            Movimentacao m = new Movimentacao(null, TipoMovimentacao.ENTRADA, LocalDateTime.now(), unidadeId, materialId, qtd, usuarioId, "Reposição");
            movDAO.insert(c, m);
            Material mat = materialDAO.findById(c, materialId);
            alertaDAO.fecharAlertasSeAcimaDoPonto(c, unidadeId, materialId, novo, mat.getPontoReposicao());
            c.commit();
        }
    }


    // 2) Saída
    public void registrarSaida(long unidadeId, long materialId, int qtd, long usuarioId, String motivo) throws SQLException {
        try(Connection c = ConnectionFactory.getConnection()){
            c.setAutoCommit(false);
            Integer saldo = estoqueDAO.getSaldo(c, unidadeId, materialId);
            if(saldo==null) saldo=0;
            if(qtd>saldo){
                c.rollback();
                throw new SQLException("Saldo insuficiente: saldo="+saldo+", qtd="+qtd);
            }
            int novo = saldo - qtd;
            estoqueDAO.upsertSaldo(c, unidadeId, materialId, novo);
            Movimentacao m = new Movimentacao(null, TipoMovimentacao.SAIDA, LocalDateTime.now(), unidadeId, materialId, qtd, usuarioId, motivo);
            movDAO.insert(c, m);
            Material mat = materialDAO.findById(c, materialId);
            if(novo <= mat.getPontoReposicao()){
                alertaDAO.inserir(c, unidadeId, materialId);
            }
            c.commit();
        }
    }


    // 3) Inventário (reconciliação)
    public int reconciliarInventario(long unidadeId, long materialId, int contagemFisica, long usuarioId) throws SQLException{
        try(Connection c = ConnectionFactory.getConnection()){
            c.setAutoCommit(false);
            Integer saldo = estoqueDAO.getSaldo(c, unidadeId, materialId);
            if(saldo==null) saldo=0;
            int diff = contagemFisica - saldo;
            if(diff==0){
                c.commit();
                return 0;
            }
            int novo = contagemFisica;
            estoqueDAO.upsertSaldo(c, unidadeId, materialId, novo);
            Movimentacao m = new Movimentacao(null, TipoMovimentacao.AJUSTE, LocalDateTime.now(), unidadeId, materialId, Math.abs(diff), usuarioId, "Inventário");
            movDAO.insert(c, m);
            Material mat = materialDAO.findById(c, materialId);
            if(novo <= mat.getPontoReposicao()) alertaDAO.inserir(c, unidadeId, materialId); else alertaDAO.fecharAlertasSeAcimaDoPonto(c, unidadeId, materialId, novo, mat.getPontoReposicao());
            c.commit();
            return diff;
        }
    }


    // 4) Alertas por unidade (scan atual)
    public List<String> gerarAlertasReposicao(long unidadeId) throws SQLException{
        try(Connection c = ConnectionFactory.getConnection()){
            List<String> msgs = new ArrayList<>();
            String sql = "SELECT e.MATERIAL_ID, e.SALDO, m.PONTO_REPOSICAO, m.CODIGO FROM ESTOQUE e JOIN MATERIAL m ON m.ID=e.MATERIAL_ID WHERE e.UNIDADE_ID=?";
            try(PreparedStatement ps=c.prepareStatement(sql)){
                ps.setLong(1,unidadeId);
                try(ResultSet rs=ps.executeQuery()){ while(rs.next()){
                    long materialId = rs.getLong(1);
                    int saldo = rs.getInt(2);
                    int ponto = rs.getInt(3);
                    String codigo = rs.getString(4);
                    if(saldo<=ponto){
                        msgs.add("Material "+codigo+" em ponto de reposição (saldo="+saldo+")");
                        alertaDAO.inserir(c, unidadeId, materialId);
                    }
                }}
            }
            return msgs;
        }
    }
}
