package data.DAOUtente;

import data.utils.Dao;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

public interface UtenteAPI extends Dao<Utente> {
    Utente findUser(String email, String password)  throws SQLException;

    //non documentati per IS
    List<Utente> doRetrieveAllUtenti() throws NoSuchAlgorithmException, SQLException;
    void doUpdate(Utente utente) throws SQLException;
    List<Utente> doRetrieveByUsername(String username) throws NoSuchAlgorithmException, SQLException;
    List<Utente> findUsers(String field, String value) throws SQLException;
    Utente fetchUtenteWithSongsAlbumArtistiPrefPlayAbbon(String username) throws SQLException;
}
