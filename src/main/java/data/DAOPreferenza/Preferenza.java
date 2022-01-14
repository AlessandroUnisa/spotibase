package data.DAOPreferenza;
/**Questa classe rappresenta il bean della preferenza
 *
 */
public class Preferenza {
    private String codCanzone, codUtente;

    public Preferenza(String codCanzone, String codUtente){
        this.codCanzone = codCanzone;
        this.codUtente = codUtente;
    }

    public String getCodCanzone(){
        return this.codCanzone;
    }

    public String getCodUtente(){
        return this.codUtente;
    }

}
