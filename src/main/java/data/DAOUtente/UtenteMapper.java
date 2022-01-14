package data.DAOUtente;

import java.sql.ResultSet;
import java.sql.SQLException;
/**Questa classe prelevare i campi dal db e creare l'oggetto utente
 *
 */
public class UtenteMapper {
    public Utente map(ResultSet rs) throws SQLException {
        Utente utente = new Utente();
        utente.setUsername(rs.getString("UTE.username"));
        utente.setEmail(rs.getString("UTE.email"));
        return utente;
    }
}
