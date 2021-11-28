package model.Genere;


public class Genere {
    private String nome;
    private String codArtista;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodArtista() {
        return codArtista;
    }

    public void setCodArtista(String artista) {
        this.codArtista = artista;
    }

    @Override
    public String toString() {
        return "Genere{" +
                "nome='" + nome + '\'' +
                ", codArtista='" + codArtista + '\'' +
                '}';
    }
}
