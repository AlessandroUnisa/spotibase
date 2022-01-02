package data.DAOPreferenza;

import data.Exceptions.OggettoGiaPresenteException;
import data.Exceptions.OggettoNonCancellatoException;
import data.Exceptions.OggettoNonInseritoException;
import data.Exceptions.OggettoNonTrovatoException;
import data.utils.SingletonJDBC;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
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
        if(chiave == null || !chiave.contains(";"))
            throw new IllegalArgumentException("chiave è null");
        String chiavi[] = chiave.split(";");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM preferenza WHERE codiceCanzone =? && usernameUtente=?;");
        preparedStatement.setString(1,chiavi[0]);
        preparedStatement.setString(2,chiavi[1]);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        if(resultSet.getRow() == 0)
            throw new OggettoNonTrovatoException("preferenza non trovata nel db");
        return new Preferenza(resultSet.getString("codiceCanzone"),resultSet.getString("usernameUtente"));
    }

    @Override
    public boolean exist(String codCanzone, String username) throws SQLException {
        if(codCanzone == null || username == null)
            throw new IllegalArgumentException("codCanzone o username sono null");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM preferenza WHERE codiceCanzone =? && usernameUtente=?;");
        preparedStatement.setString(1,codCanzone);
        preparedStatement.setString(2, username);
        return preparedStatement.executeQuery().next();
    }

    @Override
    /**
     * Questo metodo salva una preferenza nel DB
     * @param preferenza la preferenza da salvare nel database. codCanzone e codUtente devono essere settati prima dell'invocazione
     * @return void
     * */
    public void doSave(Preferenza preferenza) throws SQLException {
        if(preferenza == null || preferenza.getCodCanzone() == null || preferenza.getCodUtente() == null)
            throw new IllegalArgumentException("codCanzone o codUtente sono settati a null");

        if(exist(preferenza.getCodCanzone(), preferenza.getCodUtente()))
            throw new OggettoGiaPresenteException("La preferenza è gia presente");
        else{
            PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("INSERT INTO preferenza VALUES (?,?);");
            preparedStatement.setString(1, preferenza.getCodCanzone());
            preparedStatement.setString(2, preferenza.getCodUtente());
            if (preparedStatement.executeUpdate() != 1)
                throw new OggettoNonInseritoException("La preferenza non è stata inserita");
        }
    }

    @Override
    /**Questo metodo elimina una preferenza dal DB
     * @param chiavi la concatenzaione del codice canzone e della username. Esempio: "C94;pluto"
     * @return void
     * */
    public void doDelete(String chiave) throws SQLException {
        if(chiave == null || !chiave.contains(";"))
            throw new IllegalArgumentException("la chiave è null o non valida");
        String chiavi[] = chiave.split(";");
        if(exist(chiavi[0],chiavi[1])){
            PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("DELETE FROM preferenza WHERE codiceCanzone =? && usernameUtente=?;");
            preparedStatement.setString(1,chiavi[0]);
            preparedStatement.setString(2,chiavi[1]);
            if(preparedStatement.executeUpdate()!=1)
                throw new OggettoNonCancellatoException("La preferenza non è stata cancellata");
        }else throw new OggettoNonTrovatoException("Preferenza non trovata");
    }

    @Override
    public List<String> doRetrieveCodiciCanzoniPreferite(String username) throws SQLException {
        if(username == null )
            throw new IllegalArgumentException("username è null");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT PRE.codiceCanzone FROM preferenza PRE WHERE PRE.usernameUtente=?;");
        preparedStatement.setString(1,username);
        ArrayList<String> codici = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            codici.add(resultSet.getString("PRE.codiceCanzone"));
        return codici;
    }
}
