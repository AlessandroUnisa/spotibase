package data.DAOAcquisto;

public class Acquisto {
    private String usename, codCanzone;

    public Acquisto(String usename, String codCanzone){
        this.codCanzone = codCanzone;
        this.usename = usename;
    }

    public String getCodCanzone(){
        return this.codCanzone;
    }

    public String getUsename(){
        return this.usename;
    }
}
