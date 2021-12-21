package data.DAOAcquisto;

import data.DAOCanzone.Canzone;
import data.utils.Dao;

import java.sql.SQLException;
import java.util.List;

public interface AcquistoAPI extends Dao<Acquisto> {
    boolean doInsertCanzoneAcquistata(String username, String codice) throws SQLException;
    List<String> doRetrieveCodiciCanzoniAcquistate(String username) throws SQLException;
    List<Canzone> doRetrieveCanzoniAcquistate(String username) throws SQLException;
}
