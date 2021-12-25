package data.DAOPreferenza;

import data.utils.Dao;

import java.sql.SQLException;
import java.util.List;

public interface PreferenzaAPI extends Dao<Preferenza> {
    List<String> doRetrieveCodiciCanzoniPreferite(String username) throws SQLException;
}
