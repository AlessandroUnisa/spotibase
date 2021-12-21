package data.utils;

import java.sql.SQLException;

/**Interfaccia che dovrebbe essere implementata dalle classi DAO
 * */
public interface Dao<T> {

    T doGet(String chiave) throws SQLException;


    boolean doSave(T oggetto) throws SQLException;

    boolean doDelete(String chiave) throws SQLException;
}
