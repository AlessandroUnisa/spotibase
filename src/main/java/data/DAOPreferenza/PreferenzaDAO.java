package data.DAOPreferenza;

import data.DAOCanzone.CanzoneQuery;
import data.utils.Dao;
import data.utils.SingletonJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**Questa classe gestisce le operazioni sui dati persistenti per la classe Preferenza*/
public class PreferenzaDAO implements PreferenzaAPI {

    @Override
    /**Questo metodo ritorna la preferenza prelevata dal DB. Può essere usato per controllare se una preferenza si trova nel DB o meno
     * @param chiavi la concatenzaione del codice canzone e della username. Esempio: "C94;pluto"
     * @return un oggetto preferenza
     * */
    public Preferenza doGet(String chiave) throws SQLException {
       String chiavi[] = chiave.split(";");

        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM preferenza WHERE codiceCanzone =? && usernameUtente=?;");
        preparedStatement.setString(1,chiavi[0]);
        preparedStatement.setString(2,chiavi[1]);
        ResultSet resultSet = preparedStatement.executeQuery();
        return new Preferenza(resultSet.getString("codiceCanzone"),resultSet.getString("usernameUtente"));
    }

    @Override
    /**
     * Questo metodo salva una preferenza nel DB
     * @param preferenza la preferenza da salvare nel database. codCanzone e codUtente devono essere settati prima dell'invocazione
     * @return true se l'inserimento è andato a buonfine, false altrimenti
     * */
    public boolean doSave(Preferenza preferenza) throws SQLException {
        if(preferenza.getCodCanzone() == null || preferenza.getCodUtente() == null)
            throw new IllegalArgumentException("codCanzone o codUtente sono settati a null");

        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("INSERT INTO preferenza VALUES (?,?);");
        preparedStatement.setString(1,preferenza.getCodCanzone());
        preparedStatement.setString(2,preferenza.getCodUtente());
        return preparedStatement.executeUpdate() == 1;
    }

    @Override
    /**Questo metodo elimina una preferenza dal DB
     * @param chiavi la concatenzaione del codice canzone e della username. Esempio: "C94;pluto"
     * @return true se è stata cancellata la preferenza, false altrimenti
     * */
    public boolean doDelete(String chiave) throws SQLException {
        String chiavi[] = chiave.split(";");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("DELETE FROM preferenza WHERE codiceCanzone =? && usernameUtente=?;");
        preparedStatement.setString(1,chiavi[0]);
        preparedStatement.setString(2,chiavi[1]);
        return preparedStatement.executeUpdate() == 1;
    }

    @Override
    public List<String> doRetrieveaCodiciCanzoniPreferite(String username) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT PRE.codiceCanzone FROM preferenza PRE WHERE PRE.usernameUtente=?;");
        preparedStatement.setString(1,username);
        ArrayList<String> codici = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            codici.add(resultSet.getString("PRE.codiceCanzone"));
        return codici;
    }
}
