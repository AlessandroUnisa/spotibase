package data.utils;

import java.sql.SQLException;

/**Interfaccia che dovrebbe essere implementata dalle classi DAO
 * */
public interface Dao<T> {

    T doGet(String chiave) throws SQLException;


    void doSave(T oggetto) throws SQLException;

    void doDelete(String chiave) throws SQLException;
}
