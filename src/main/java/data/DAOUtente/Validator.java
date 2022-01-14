package data.DAOUtente;

import java.util.regex.Pattern;
/**Questa classe permette di validare l'email e password di un utente tramite le regex
 *
 */
public class Validator {
    private static final Pattern MAIL_ADMIN = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@spotibase.it");
    private static final Pattern MAIL_USER = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    private static final Pattern PASSWD = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$");

    public boolean isAdminEmail(String email){
        return MAIL_ADMIN.matcher(email).matches();
    }

    public boolean isValidEmail(String email){
        return MAIL_USER.matcher(email).matches();
    }

    public boolean isValidPasswd(String passwd){
        return PASSWD.matcher(passwd).matches();
    }
}
