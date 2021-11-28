package model.Attivazione;

import model.ConPool;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttivazioneDAO {
    /**Inserisce un attivazione*/
    public boolean doInsertAttivazione(Attivazione attivazione){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO attivazione VALUES(?,?,?,?);");
            preparedStatement.setDate(1,Date.valueOf(attivazione.getDataInizio()));
            preparedStatement.setDate(2,Date.valueOf(attivazione.getDataFine()));
            preparedStatement.setString(3,attivazione.getUtente().getUsername());
            preparedStatement.setString(4,attivazione.getAbbonamento().getNome());
            return preparedStatement.executeUpdate()==1;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public List<Attivazione> doRetrieveAttivazioniAttiveByUtente(String username){
        try (Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM attivazione ATT WHERE ATT.dataFine > ? AND ATT.usernameUtente=?;");
            preparedStatement.setDate(1,Date.valueOf(LocalDate.now()));
            preparedStatement.setString(2, username);
            ArrayList<Attivazione> list = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next())
                list.add(new AttivazioneMapper().map(resultSet));
            return list;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
