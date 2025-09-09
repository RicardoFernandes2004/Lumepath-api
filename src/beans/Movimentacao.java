package beans;

import java.time.LocalDateTime;

public class Movimentacao {
    private Long id;
    private TipoMovimentacao tipo;
    private LocalDateTime dataHora;
    private Long unidadeId;
    private Long materialId;
    private int quantidade;
    private Long usuarioId;
    private String motivo;

    public Movimentacao(){}
    public Movimentacao(Long id, TipoMovimentacao tipo, LocalDateTime dataHora, Long unidadeId, Long materialId, int quantidade, Long usuarioId, String motivo){
        this.id=id;
        this.tipo=tipo;
        this.dataHora=dataHora;
        this.unidadeId=unidadeId;
        this.materialId=materialId;
        this.quantidade=quantidade;
        this.usuarioId=usuarioId;
        this.motivo=motivo;
    }

    public Long getId(){return id;}

    public void setId(Long id){this.id=id;}

    public TipoMovimentacao getTipo(){return tipo;}

    public void setTipo(TipoMovimentacao t){this.tipo=t;}

    public LocalDateTime getDataHora(){return dataHora;}

    public void setDataHora(LocalDateTime d){this.dataHora=d;}

    public Long getUnidadeId(){return unidadeId;}

    public void setUnidadeId(Long u){this.unidadeId=u;}

    public Long getMaterialId(){return materialId;}

    public void setMaterialId(Long m){this.materialId=m;}

    public int getQuantidade(){return quantidade;}

    public void setQuantidade(int q){this.quantidade=q;}

    public Long getUsuarioId(){return usuarioId;}

    public void setUsuarioId(Long u){this.usuarioId=u;}

    public String getMotivo(){return motivo;}

    public void setMotivo(String m){this.motivo=m;}
}
