package data.DAOAcquisto;

import data.utils.Dao;
import data.utils.SingletonJDBC;
import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneMapper;
import data.DAOCanzone.CanzoneQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AcquistoDAO implements AcquistoAPI {
    @Override
    /**Questo metodo ritorna l'acquisto prelevato dal DB. Può essere usato per controllare se un acquisto si trova nel DB o meno
     * @param chiave la concatenazione della username e del codice canzone. Esempio: "pluto;C94"
     * @return un oggetto Acquisto
     * */
    public Acquisto doGet(String chiave) throws SQLException {
        String chiavi[] = chiave.split(";");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM acquisto WHERE username=? && codCanzone=?");
        preparedStatement.setString(1,chiavi[0]);
        preparedStatement.setString(2,chiavi[1]);
        ResultSet resultSet = preparedStatement.executeQuery();
        return new Acquisto(resultSet.getString("username"),resultSet.getString("codCanzone"));
    }

    @Override
    /**Questo metodo salva un acquisto nel DB
     * @param acquisto l'oggetto da salvare. Prima di invocare il metodo bisogna settare la username e il codCanzone all'interno dell oggetto
     * @return true se il salvataggio avviene correttamente, false altrimenti
     * */
    public boolean doSave(Acquisto acquisto) throws SQLException {
        if(acquisto.getCodCanzone() == null || acquisto.getUsename() == null)
            throw new IllegalArgumentException("parametri non settati all'interno di acquisto");

       PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("INSERT INTO uc VALUES (?,?)");
       preparedStatement.setString(1,acquisto.getUsename());
       preparedStatement.setString(2,acquisto.getCodCanzone());
       return preparedStatement.executeUpdate()==1;
    }

    @Override
    /**Questo metodo cancella un acquisto dal DB
     * @param chiave la concatenazione della username e del codice canzone. Esempio: "pluto;C94"e
     * @return true se l'acquisto è stato cancellato, false altrimenti
     * */
    public boolean doDelete(String chiave) throws SQLException {
        String[] chiavi = chiave.split(";");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("DELETE FROM uc WHERE username=? && codCanzone=?");
        preparedStatement.setString(1,chiavi[0]);
        preparedStatement.setString(2, chiavi[1]);
        return preparedStatement.executeUpdate()==1;
    }

    /**Inserisce una canzone acquistata*/
    public boolean doInsertCanzoneAcquistata(String username, String codice) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoInsertCanzoneAcquistata());
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,codice);
        return preparedStatement.executeUpdate()==1;
    }

    /**Ritorna la lista delle canzoni acquistate dall utente*/
    public List<String> doRetrieveCodiciCanzoniAcquistate(String username) throws SQLException {

        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(CanzoneQuery.getQueryDoRetrieveCodiciCanzoniAcquistate());
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<String> codici = new ArrayList<>();
        while (resultSet.next())
            codici.add(resultSet.getString("codCanzone"));
        return codici;
    }

    /**Ritorna le canzoni acquistate dall'utente*/
    public List<Canzone> doRetrieveCanzoniAcquistate(String username) throws SQLException {
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
