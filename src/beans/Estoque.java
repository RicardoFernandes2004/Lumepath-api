package beans;

public class Estoque {
    private Long id;
    private Long unidadeId;
    private Long materialId;
    private int saldo;

    public Estoque(){}
    public Estoque(Long id, Long unidadeId, Long materialId, int saldo){
        this.id=id;
        this.unidadeId=unidadeId;
        this.materialId=materialId;
        this.saldo=saldo;
    }
    public Long getId(){return id;}

    public void setId(Long id){this.id=id;}

    public Long getUnidadeId(){return unidadeId;}

    public void setUnidadeId(Long u){this.unidadeId=u;}

    public Long getMaterialId(){return materialId;}

    public void setMaterialId(Long m){this.materialId=m;}

    public int getSaldo(){return saldo;}

    public void setSaldo(int s){this.saldo=s;}

}
