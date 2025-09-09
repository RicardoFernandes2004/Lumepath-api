package beans;

import java.time.LocalDateTime;

public class Alerta {
    private Long id;
    private Long unidadeId;
    private Long materialId;
    private LocalDateTime geradoEm;
    private boolean lido;

    public Alerta(){}
    public Alerta(Long id, Long unidadeId, Long materialId, LocalDateTime geradoEm, boolean lido) {
        this.id=id;
        this.unidadeId=unidadeId;
        this.materialId=materialId;
        this.geradoEm=geradoEm;
        this.lido=lido;
    }

    public Long getId(){return id;}

    public void setId(Long id){this.id=id;}

    public Long getUnidadeId(){return unidadeId;}

    public void setUnidadeId(Long u){this.unidadeId=u;}

    public Long getMaterialId(){return materialId;}

    public void setMaterialId(Long m){this.materialId=m;}

    public LocalDateTime getGeradoEm(){return geradoEm;}

    public void setGeradoEm(LocalDateTime g){this.geradoEm=g;}

    public boolean isLido(){return lido;}

    public void setLido(boolean l){this.lido=l;}
}
