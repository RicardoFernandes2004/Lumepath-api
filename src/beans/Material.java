package beans;

public class Material {
    private Long id;
    private String codigo;
    private String descricao;
    private String unidadeMedida;
    private int pontoReposicao;

    public Material(){}

    public Material(Long id,String codigo,String descricao,String unidadeMedida,int pontoReposicao){
        this.id=id; this.codigo=codigo; this.descricao=descricao; this.unidadeMedida=unidadeMedida; this.pontoReposicao=pontoReposicao;
    }

    public Long getId(){return id;}

    public void setId(Long id){this.id=id;}

    public String getCodigo(){return codigo;}

    public void setCodigo(String c){this.codigo=c;}

    public String getDescricao(){return descricao;}

    public void setDescricao(String d){this.descricao=d;}

    public String getUnidadeMedida(){return unidadeMedida;}

    public void setUnidadeMedida(String u){this.unidadeMedida=u;}

    public int getPontoReposicao(){return pontoReposicao;}

    public void setPontoReposicao(int p){this.pontoReposicao=p;}

    @Override
    public String toString(){
        return id+" - "+codigo+" ("+descricao+")";
    }
}
