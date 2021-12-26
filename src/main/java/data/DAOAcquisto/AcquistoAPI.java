package data.DAOAcquisto;

import data.DAOCanzone.Canzone;
import data.utils.Dao;

import java.sql.SQLException;
import java.util.List;

public interface AcquistoAPI extends Dao<Acquisto> {
    void doInsertCanzoneAcquistata(String username, String codice) throws SQLException;
    List<String> doRetrieveCodiciCanzoniAcquistate(String username) throws SQLException;
    boolean exist(String username, String codCanzone) throws SQLException;

    //metodi non documentati per IS
    List<Canzone> doRetrieveCanzoniAcquistate(String username) throws SQLException;
}
