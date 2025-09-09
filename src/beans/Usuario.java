package beans;

public class Usuario {

    private Long id;
    private String nome;
    private Perfil perfil;

    public Usuario() {}

    public Usuario(Long id,String nome,Perfil perfil){
        this.id=id; this.nome=nome; this.perfil=perfil;
    }
    public Long getId(){return id;}

    public void setId(Long id){this.id=id;}

    public String getNome(){return nome;}

    public void setNome(String n){this.nome=n;}

    public Perfil getPerfil(){return perfil;}

    public void setPerfil(Perfil p){this.perfil=p;}

    @Override
    public String toString(){
        return id+" - "+nome+" ("+perfil+")";
    }
}
