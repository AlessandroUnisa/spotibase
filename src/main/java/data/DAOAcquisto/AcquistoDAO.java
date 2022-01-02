package data.DAOAcquisto;

import data.Exceptions.OggettoGiaPresenteException;
import data.Exceptions.OggettoNonCancellatoException;
import data.Exceptions.OggettoNonInseritoException;
import data.Exceptions.OggettoNonTrovatoException;
import data.utils.SingletonJDBC;
import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneMapper;
import data.DAOCanzone.CanzoneQuery;

import javax.annotation.processing.Generated;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;



public class AcquistoDAO implements AcquistoAPI {
    @Override
    /**Questo metodo ritorna l'acquisto prelevato dal DB. Può essere usato per controllare se un acquisto si trova nel DB o meno
     * @param chiave la concatenazione della username e del codice canzone. Esempio: "pluto;C94"
     * @return un oggetto Acquisto
     * */
    public Acquisto doGet(String chiave) throws SQLException {
        if(chiave == null || !chiave.contains(";"))
            throw new IllegalArgumentException("la chiave è null o non valida");
        String chiavi[] = chiave.split(";");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM acquisto WHERE username=? && codCanzone=?");
        preparedStatement.setString(1,chiavi[0]);
        preparedStatement.setString(2,chiavi[1]);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.getRow()==0)
            throw new OggettoNonTrovatoException("L'acquisto non è stato trovato: "+chiave);
        return new Acquisto(resultSet.getString("username"),resultSet.getString("codCanzone"));
    }

    @Override
    /**Questo metodo salva un acquisto nel DB
     * @param acquisto l'oggetto da salvare. Prima di invocare il metodo bisogna settare la username e il codCanzone all'interno dell oggetto
     * @return true se il salvataggio avviene correttamente, false altrimenti
     * */
    public void doSave(Acquisto acquisto) throws SQLException {
        if(acquisto == null || acquisto.getCodCanzone() == null || acquisto.getUsename() == null)
            throw new IllegalArgumentException("parametri non settati all'interno di acquisto");

        if(exist(acquisto.getUsename(), acquisto.getCodCanzone()))
            throw new OggettoGiaPresenteException("L'acquisto è gia presente");
        else{
            PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("INSERT INTO uc VALUES (?,?)");
            preparedStatement.setString(1, acquisto.getUsename());
            preparedStatement.setString(2, acquisto.getCodCanzone());
            if (preparedStatement.executeUpdate() != 1)
                throw new OggettoNonInseritoException("L'acquisto non è stato inserito");
        }
    }

    @Override
    /**Questo metodo cancella un acquisto dal DB
     * @param chiave la concatenazione della username e del codice canzone. Esempio: "pluto;C94"e
     * @return void
     * */
    public void doDelete(String chiave) throws SQLException {
        if(chiave == null || !chiave.contains(";"))
            throw new IllegalArgumentException("la chiave è null o non valida");

        String[] chiavi = chiave.split(";");

        if(exist(chiavi[0],chiavi[1])){
            PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("DELETE FROM uc WHERE username=? && codCanzone=?");
            preparedStatement.setString(1,chiavi[0]);
            preparedStatement.setString(2, chiavi[1]);

            if(preparedStatement.executeUpdate()!=1)
                throw new OggettoNonCancellatoException("l'acquisto non è stato cancellato");
        }else throw new OggettoNonTrovatoException("Acquisto non trovato");
    }

    @Override
    /**Questo metodo inserisce una canzone negli acquisti
     * @param chiave la concatenazione della username e del codice canzone. Esempio: "pluto;C94"e
     * @return void
     * */
    public void doInsertCanzoneAcquistata(String username, String codice) throws SQLException {
        if(username == null || codice == null)
            throw new IllegalArgumentException("username o codice sono null");

        if(exist(username,codice))
            throw new OggettoGiaPresenteException("L'acquisto è gia presente");
        else{
            PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoInsertCanzoneAcquistata());
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, codice);
            if (preparedStatement.executeUpdate() != 1)
                throw new OggettoNonInseritoException("La canzone non è stata acquistata");
        }
    }

    /**Ritorna la lista delle canzoni acquistate dall utente*/
    public List<String> doRetrieveCodiciCanzoniAcquistate(String username) throws SQLException {
        if(username == null){
            throw new IllegalArgumentException("username è null");
        }
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveCodiciCanzoniAcquistate());
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<String> codici = new ArrayList<>();
        while (resultSet.next())
            codici.add(resultSet.getString("codCanzone"));
        return codici;
    }

    @Override
    public boolean exist(String username, String codCanzone) throws SQLException {
        if(username == null || codCanzone == null)
            throw new IllegalArgumentException("username o codCanzone sono null");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM acquisto WHERE username=? && codCanzone=?");
        preparedStatement.setString(1,username);
        preparedStatement.setString(2, codCanzone);
         return preparedStatement.executeQuery().next();
    }



    //non documentati per IS----------------------------------------------------------------------------------------------------

    /**Ritorna le canzoni acquistate dall'utente*/

    public List<Canzone> doRetrieveCanzoniAcquistate(String username) throws SQLException {
        if(username == null){
            throw new IllegalArgumentException("username è null");
        }
        PreparedStatement statement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveCanzoniAcquistate());
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
