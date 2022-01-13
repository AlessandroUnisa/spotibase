package data.DAOUtente;

import data.utils.Dao;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.List;

/**
 * @se
 */
public interface UtenteAPI extends Dao<Utente> {

    Utente doGet(String email, String password)  throws SQLException;
    List<Utente> findUsers(String field, String value) throws SQLException;
    boolean exist(String chiave) throws SQLException;

    /**
     *
     * @param username
     * @return
     */
    boolean isValidUsername(String username);
    public boolean isValidEmail(String email);

    public boolean isValidPasswd(String passwd);

    //non documentati per IS
    List<Utente> doRetrieveAllUtenti() throws NoSuchAlgorithmException, SQLException;
    void doUpdate(Utente utente) throws SQLException;
    List<Utente> doRetrieveByUsername(String username) throws NoSuchAlgorithmException, SQLException;
    Utente fetchUtenteWithSongsAlbumArtistiPrefPlayAbbon(String username) throws SQLException;
    public boolean isAdminEmail(String email);
}
