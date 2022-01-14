package data.DAOPreferenza;

import data.utils.Dao;

import java.sql.SQLException;
import java.util.List;
/**Questa classe rappresenta l'interfaccia utlizzata dalla preferenzaDAO e implementata per rispettare il facade pattern
 *
 */
public interface PreferenzaAPI extends Dao<Preferenza> {
    List<String> doRetrieveCodiciCanzoniPreferite(String username) throws SQLException;
    boolean exist(String codCanzone, String username) throws SQLException;
}
