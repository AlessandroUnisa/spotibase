package data.Attivazione;

import data.utils.SingletonJDBC;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttivazioneDAO {
    //metodi documentati per IS----------------------------------------------------------------------------------------------

    //metodi NON documentati per IS----------------------------------------------------------------------------------------------

    /**Inserisce un attivazione*/
    public boolean doInsertAttivazione(Attivazione attivazione) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("INSERT INTO attivazione VALUES(?,?,?,?);");
        preparedStatement.setDate(1,Date.valueOf(attivazione.getDataInizio()));
        preparedStatement.setDate(2,Date.valueOf(attivazione.getDataFine()));
        preparedStatement.setString(3,attivazione.getUtente().getUsername());
        preparedStatement.setString(4,attivazione.getAbbonamento().getNome());
        return preparedStatement.executeUpdate()==1;
    }

    public List<Attivazione> doRetrieveAttivazioniAttiveByUtente(String username) throws SQLException {

        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM attivazione ATT WHERE ATT.dataFine > ? AND ATT.usernameUtente=?;");
        preparedStatement.setDate(1,Date.valueOf(LocalDate.now()));
        preparedStatement.setString(2, username);
        ArrayList<Attivazione> list = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next())
            list.add(new AttivazioneMapper().map(resultSet));
        return list;

    }
}
