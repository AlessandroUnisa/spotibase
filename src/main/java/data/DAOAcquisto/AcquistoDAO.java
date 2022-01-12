package data.DAOAcquisto;

import data.DAOPlaylist.PlaylistAPI;
import data.Exceptions.OggettoGiaPresenteException;
import data.Exceptions.OggettoNonCancellatoException;
import data.Exceptions.OggettoNonInseritoException;
import data.Exceptions.OggettoNonTrovatoException;
import data.utils.SingletonJDBC;
import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneMapper;
import data.DAOCanzone.CanzoneQuery;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@interface Generated {
}
/** Questa classe permette  di gestire le operazioni relative ai dati persistenti degli acquisti
 *
 * @version 1.0
 * @see AcquistoAPI interfaccia della classe
 */
public class AcquistoDAO implements AcquistoAPI {
    
    private Connection connection;
    //ciao

    public AcquistoDAO(){
        this.connection = SingletonJDBC.getConnection();
    }

    public AcquistoDAO(Connection connection){

        this.connection = connection;
    }



    /**Questo metodo ritorna l'acquisto prelevato dal DB. Può essere usato per controllare se un acquisto si trova nel DB o meno
     * <p><b>pre: </b>chiave != null, chiave.contains(;) == true e l'acquisto deve esistere nel db</p>
     * @param chiave la concatenazione della username e del codice canzone. Esempio: "pluto;C94"
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando la chiave è null o non valida
     * @throws OggettoNonTrovatoException Un'eccezione che viene lanciata quando l'acquisto non è stato trovato nel db
     * @return un oggetto Acquisto
     * */
    public Acquisto doGet(String chiave) throws SQLException {
        if(chiave == null || !chiave.contains(";"))
            throw new IllegalArgumentException("la chiave è null o non valida");
        String chiavi[] = chiave.split(";");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM uc WHERE username=? && codCanzone=?");
        preparedStatement.setString(1,chiavi[0]);
        preparedStatement.setString(2,chiavi[1]);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        if(resultSet.getRow()==0)
            throw new OggettoNonTrovatoException("L'acquisto non è stato trovato: "+chiave);
        return new Acquisto(resultSet.getString("username"),resultSet.getString("codCanzone"));
    }


    /**Questo metodo salva un acquisto nel DB
     * <p><b>pre: </b>acquisto deve essere diverso da null e non esistere nel db, il codice della canzone != null e l'username != null<br>
     *    <b>post: </b>Acquisto è presente nel db</p>
     * @param acquisto l'oggetto da salvare. Prima di invocare il metodo bisogna settare la username e il codCanzone all'interno dell oggetto
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando acquisto o il codice della canzone oppure l'username dell'utente sono null o non validi
     * @throws OggettoGiaPresenteException  Un'eccezione che viene lanciata quando l'acquisto  è già presente nel db
     * @throws OggettoNonInseritoException Un'eccezione che viene lanciata quando l'acquisto non è stato inserito
     * */
    public void doSave(Acquisto acquisto) throws SQLException {

        if(acquisto == null || acquisto.getCodCanzone() == null || acquisto.getUsename() == null)
            throw new IllegalArgumentException("parametri non settati all'interno di acquisto");

        if(exist(acquisto.getUsename(), acquisto.getCodCanzone()))
            throw new OggettoGiaPresenteException("L'acquisto è gia presente");
        else{
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO uc VALUES (?,?)");
            preparedStatement.setString(1, acquisto.getUsename());
            preparedStatement.setString(2, acquisto.getCodCanzone());
            if (preparedStatement.executeUpdate()!=1)
                throw new OggettoNonInseritoException("L'acquisto non è stato inserito");
        }
    }


    /**Questo metodo cancella un acquisto dal DB
     * <p><b>pre:</b> chiave != null, chiave.contains(;) == true e l'acquisto deve esistere nel db <br>
     *    <b>post:</b> Acquisto non presente nel db</p>
     * @param chiave la concatenazione della username e del codice canzone. Esempio: "pluto;C94"
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando la chiave è null o non valida
     * @throws OggettoNonCancellatoException  Un'eccezione che viene lanciata quando l'acquisto non è stato cancellato nel db
     * @throws OggettoNonTrovatoException Un'eccezione che viene lanciata quando l'acquisto non è stato trovato nel db
     *
     *
     * */

    public void doDelete(String chiave) throws SQLException {
        if(chiave == null || !chiave.contains(";"))
            throw new IllegalArgumentException("la chiave è null o non valida");

        String[] chiavi = chiave.split(";");

        if(exist(chiavi[0],chiavi[1])){
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM uc WHERE username=? && codCanzone=?");
            preparedStatement.setString(1,chiavi[0]);
            preparedStatement.setString(2, chiavi[1]);

            if(preparedStatement.executeUpdate()!=1)
                throw new OggettoNonCancellatoException("l'acquisto non è stato cancellato");
        }else
            throw new OggettoNonTrovatoException("Acquisto non trovato");
    }


    /**Questo metodo inserisce una canzone negli acquisti
     * <p><b>pre: </b>il codice della canzone != null e l'username != null e non deve esistere esistere la corrispondenza tra username e codice della canzone <br>
     *    <b>post: </b>deve esistere esistere la corrispondenza tra username e codice della canzone</p>
     * @param username contiene la username dell'utente che acquista una canzone
     * @param codice codice della canzone che viene acquistata dall'utente
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando acquisto o il codice della canzone oppure l'username dell'utente sono null o non validi
     * @throws OggettoGiaPresenteException  Un'eccezione che viene lanciata quando l'acquisto è già presente nel db
     * @throws OggettoNonInseritoException Un'eccezione che viene lanciata quando la canzone non viene acquistata
     * */
    public void doInsertCanzoneAcquistata(String username, String codice) throws SQLException {
        if(username == null || codice == null)
            throw new IllegalArgumentException("username o codice sono null");

        if(exist(username,codice)==true) {
            throw new OggettoGiaPresenteException("L'acquisto è gia presente");
        }else{
            PreparedStatement preparedStatement = connection.prepareStatement(CanzoneQuery.getQueryDoInsertCanzoneAcquistata());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, codice);
            if (preparedStatement.executeUpdate() != 1)
                throw new OggettoNonInseritoException("La canzone non è stata acquistata");
        }
    }


    /**Ritorna la lista delle canzoni acquistate dall utente
     * <p><b>pre: </b> username != null e deve esistere nel db</p>
     * @param username username dell'utente che ha acquistato una serie di canzoni
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando username dell'utente è null o non valido
     * @return una lista di codici delle canzoni acquistate
     * */
    public List<String> doRetrieveCodiciCanzoniAcquistate(String username) throws SQLException {
        if(username == null){
            throw new IllegalArgumentException("username è null");
        }
        PreparedStatement preparedStatement = connection.prepareStatement(CanzoneQuery.getQueryDoRetrieveCodiciCanzoniAcquistate());
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<String> codici = new ArrayList<>();
        while (resultSet.next())
            codici.add(resultSet.getString("codCanzone"));
        return codici;
    }

    /**Questo metodo consente di verificare l’acquisto all’interno del database
     * <p><b>pre: </b>codice != null e username != null</p>
     * @param username username dell'utente che ha effettuato l'acquisto
     * @param codCanzone codice della canzone acquistata ddll'utente
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando il codice della canzone oppure l'username dell'utente sono null o non validi
     * @return true se l'acquisto esiste, false altrimenti
     */
    public boolean exist(String username, String codCanzone) throws SQLException {
        if(username == null || codCanzone == null)
            throw new IllegalArgumentException("username o codCanzone sono null");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM uc WHERE username=? && codCanzone=?");
        preparedStatement.setString(1,username);
        preparedStatement.setString(2, codCanzone);
         return preparedStatement.executeQuery().next();
    }



    //non documentati per IS----------------------------------------------------------------------------------------------------

    /**Ritorna le canzoni acquistate dall'utente*/

    @Generated
    public List<Canzone> doRetrieveCanzoniAcquistate(String username) throws SQLException {
        if(username == null){
            throw new IllegalArgumentException("username è null");
        }
        PreparedStatement statement = connection.prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzoniAcquistate());
        statement.setString(1,username);
        statement.setString(2, username);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Canzone> list = new ArrayList<>();
        CanzoneMapper mapper = new CanzoneMapper();
        System.out.println(resultSet);
        while (resultSet.next())
            list.add(mapper.map(resultSet));
        return list;

    }
}
