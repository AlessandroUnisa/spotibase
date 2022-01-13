package data.DAOPreferenza;

import data.Exceptions.OggettoGiaPresenteException;
import data.Exceptions.OggettoNonCancellatoException;
import data.Exceptions.OggettoNonInseritoException;
import data.Exceptions.OggettoNonTrovatoException;
import data.utils.SingletonJDBC;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**Questa classe gestisce le operazioni sui dati persistenti per la classe Preferenza
 * @version 1.0
 * @see PreferenzaAPI interfaccia della classe
 */

public class PreferenzaDAO implements PreferenzaAPI {

    Connection connection;
    public PreferenzaDAO(){
        this.connection = SingletonJDBC.getConnection();
    }
    public PreferenzaDAO(Connection connection) {
        this.connection = connection;
    }
    
    


    /**Questo metodo ritorna la preferenza prelevata dal DB. Può essere usato per controllare se una preferenza si trova nel DB o meno
     * <p><b>pre:</b> chiave != null, chiave.contains(;) == true e la preferenza deve esistere nel db</p>
     * @param chiave la concatenzaione del codice canzone e della username. Esempio: "C94;pluto"
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando la chiave è null o non valida
     * @throws OggettoNonTrovatoException Un'eccezione che viene lanciata quando la preferenza non è stata trovata nel db
     * @return un oggetto preferenza
     * */
    public Preferenza doGet(String chiave) throws SQLException {
        if(chiave == null || !chiave.contains(";"))
            throw new IllegalArgumentException("chiave è null");
        String chiavi[] = chiave.split(";");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM preferenza WHERE codiceCanzone =? && usernameUtente=?;");
        preparedStatement.setString(1,chiavi[0]);
        preparedStatement.setString(2,chiavi[1]);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        if(resultSet.getRow() == 0)
            throw new OggettoNonTrovatoException("preferenza non trovata nel db");
        return new Preferenza(resultSet.getString("codiceCanzone"),resultSet.getString("usernameUtente"));
    }

    /**Questo metodo consente di verificare se la preferenza è all’interno del database
     * <p><b>pre: </b>codice != null e username != null</p>
     * @param codCanzone codice della canzone alla quale è stata espressa una preferenza dall'utente
     * @param username username dell'utente che ha espresso la preferenza
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando il codice della canzone oppure l'username dell'utente sono null o non validi
     * @return true se la preferenza esiste, false altrimenti
     */
    public boolean exist(String codCanzone, String username) throws SQLException {
        if(codCanzone == null || username == null)
            throw new IllegalArgumentException("codCanzone o username sono null");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM preferenza WHERE codiceCanzone =? && usernameUtente=?;");
        preparedStatement.setString(1,codCanzone);
        preparedStatement.setString(2, username);
        return preparedStatement.executeQuery().next();
    }


    /**
     * Questo metodo salva una preferenza nel DB
     * <p><b>pre: </b>il codice della canzone e username dell'utente diversi da null e la preferenza non deve stare nel db <br>
     *    <b>post: </b> preferenza salvata nel db</p>
     * @param preferenza la preferenza da salvare nel database. codCanzone e codUtente devono essere settati prima dell'invocazione
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando la preferenza o il codice della canzone oppure l'username dell'utente sono null o non validi
     * @throws OggettoGiaPresenteException  Un'eccezione che viene lanciata quando la preferenza  è già presente nel db
     * @throws OggettoNonInseritoException Un'eccezione che viene lanciata quando la preferenza non è stata inserita
     * */
    public void doSave(Preferenza preferenza) throws SQLException {
        if(preferenza == null || preferenza.getCodCanzone() == null || preferenza.getCodUtente() == null)
            throw new IllegalArgumentException("codCanzone o codUtente sono settati a null");

        if(exist(preferenza.getCodCanzone(), preferenza.getCodUtente()))
            throw new OggettoGiaPresenteException("La preferenza è gia presente");
        else{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO preferenza VALUES (?,?);");
            preparedStatement.setString(1, preferenza.getCodCanzone());
            preparedStatement.setString(2, preferenza.getCodUtente());
            if (preparedStatement.executeUpdate() != 1)
                throw new OggettoNonInseritoException("La preferenza non è stata inserita");
        }
    }

    /**Questo metodo elimina una preferenza dal DB
     * <p><b>pre: </b> chiave != null, chiave.contains(;) == true e la preferenza presente nel db<br>
     *    <b>post: </b>preferenza eliminata dal db</p>
     * @param chiave la concatenzaione del codice canzone e della username. Esempio: "C94;pluto"
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando la chiave è null o non valida
     * @throws OggettoNonCancellatoException  Un'eccezione che viene lanciata quando la preferenza non è stata cancellata nel db
     * @throws OggettoNonTrovatoException Un'eccezione che viene lanciata quando la preferenza non è stata trovata nel db
     * */
    public void doDelete(String chiave) throws SQLException {
        if(chiave == null || !chiave.contains(";"))
            throw new IllegalArgumentException("la chiave è null o non valida");
        String chiavi[] = chiave.split(";");
        if(exist(chiavi[0],chiavi[1])){
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM preferenza WHERE codiceCanzone =? && usernameUtente=?;");
            preparedStatement.setString(1,chiavi[0]);
            preparedStatement.setString(2,chiavi[1]);
            if(preparedStatement.executeUpdate()!=1)
                throw new OggettoNonCancellatoException("La preferenza non è stata cancellata");
        }else throw new OggettoNonTrovatoException("Preferenza non trovata");
    }


    /**Questo metodo consente di prelevare i codici delle canzoni preferite dall’utente grazie al suo username
     * <p><b>pre: </b>username != null e deve esistere nel db</p>
     * @param username username dell'utente dal quale prenderemo le preferenze
     * @return lista di codici delle canzoni
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando l'username è null o non valido
     * */
    public List<String> doRetrieveCodiciCanzoniPreferite(String username) throws SQLException {
        if(username == null )
            throw new IllegalArgumentException("username è null");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT PRE.codiceCanzone FROM preferenza PRE WHERE PRE.usernameUtente=?;");
        preparedStatement.setString(1,username);
        ArrayList<String> codici = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            codici.add(resultSet.getString("PRE.codiceCanzone"));
        return codici;
    }
}
