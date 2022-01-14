package data.DAOUtente;
import data.Exceptions.OggettoGiaPresenteException;
import data.Exceptions.OggettoNonTrovatoException;
import data.utils.SingletonJDBC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
public class UtenteDAOTest {


    private UtenteDAO utenteDAO;
    private Connection connection;

    @Before
    public void setUp(){
        this.connection= SingletonJDBC.getConnectionTesting();
        this.utenteDAO=new UtenteDAO(connection);
    }


    @Test
    public void doGetKeyNull(){
        String key=null;
        assertThrows(IllegalArgumentException.class,()->utenteDAO.doGet(key));
    }


    @Test
    public void doGetOggettoNonTrovato(){
        String username="Umby";
        assertThrows(OggettoNonTrovatoException.class,()->utenteDAO.doGet(username));
    }


    @Test
    public void doGetOk() throws SQLException, NoSuchAlgorithmException {
        //Inserisco un utente con la doSave, dopo lo elimino
        truncateUtente();
        Utente utente=insertUtente();
        utenteDAO.doSave(utente);
        Utente utente1=utenteDAO.doGet(utente.getUsername());
        assertEquals(utente.getUsername(),utente1.getUsername());
        truncateUtente();
    }

    @Test
    public void findUserWithFieldNull(){
        String field=null;
        String value="sakdgaksd";
        assertThrows(IllegalArgumentException.class,()->utenteDAO.findUsers(field,value));
    }

    @Test
    public void findUserWithValueNull(){
        String field="sakdgaksd";
        String value=null;
        assertThrows(IllegalArgumentException.class,()->utenteDAO.findUsers(field,value));
    }


    @Test
    public void findUserOK() throws SQLException, NoSuchAlgorithmException {
        truncateUtente();
        String field="username";
        String value="Umby";
        Utente utente=insertUtente();
        utenteDAO.doSave(utente);
        List<Utente>ListOfUtente=utenteDAO.findUsers(field,value);//Ritroviamo la lista dell'utente
        assertEquals(1,ListOfUtente.size());
        assertEquals(utente.getUsername(),ListOfUtente.get(0).getUsername());
        truncateUtente();
    }



    @Test
    public void existKeyNull(){
        String key=null;
        assertThrows(IllegalArgumentException.class,()->utenteDAO.exist(key));
    }


    @Test
    public void existOK() throws NoSuchAlgorithmException, SQLException {
        Utente utente=insertUtente();
        utenteDAO.doSave(utente);
        assertEquals(true,utenteDAO.exist(utente.getUsername()));
        truncateUtente();
    }


  /*  @Test
    public void doSaveWithUtenteNull(){
        Utente utente=null;
        assertThrows(IllegalArgumentException.class,()->utenteDAO.doSave(utente));
    }
*/

    @Test
    public void doSaveWithUsernameUtenteNull() throws NoSuchAlgorithmException {
        Utente utente=new Utente();
        utente.setUsername(null);
        utente.setEmail("adkbsfskdj@gmail.com");
        utente.setPassword("asldjasdask");
        assertThrows(IllegalArgumentException.class,()->utenteDAO.doSave(utente));
    }


    @Test
    public void doSaveWithEmailUtenteNull() throws NoSuchAlgorithmException {
        Utente utente=new Utente();
        utente.setUsername("askdbad");
        utente.setEmail(null);
        utente.setPassword("asldjasdask");
        assertThrows(IllegalArgumentException.class,()->utenteDAO.doSave(utente));
    }


    @Test
    public void doSaveOggettoGiaPresente() throws NoSuchAlgorithmException, SQLException {
        truncateUtente();
        Utente utente=insertUtente();
        utenteDAO.doSave(utente);
        assertThrows(OggettoGiaPresenteException.class,()->utenteDAO.doSave(utente));
        truncateUtente();
    }

    @Test
    public void doSaveOK() throws NoSuchAlgorithmException, SQLException {
        truncateUtente();
        Utente utente=insertUtente();
        utenteDAO.doSave(utente);
        assertEquals(true,utenteDAO.exist(utente.getUsername()));
        truncateUtente();
    }



    //DoGet parte 2

    @Test
    public void doGetWithEmailNull(){
        String email=null, password="skdhasddas";
        assertThrows(IllegalArgumentException.class,()->utenteDAO.doGet(email,password));
    }


    @Test
    public void doGetWithPasswdNull(){
        String email="asdksadbasd", password=null;
        assertThrows(IllegalArgumentException.class,()->utenteDAO.doGet(email,password));
    }

    @Test
    public void doGetWithEmailAndPasswordOggettoNonTrovato() throws NoSuchAlgorithmException {
        String email="umberto.dellamonica@gmail.com", password="ciao2";
        Utente utente=insertUtente();
        assertThrows(OggettoNonTrovatoException.class,()->utenteDAO.doGet(email,password));
    }

    @Test
    public void doGetOKWithEmailAndPassword() throws NoSuchAlgorithmException, SQLException {
        Utente utente=insertUtente();
        utenteDAO.doSave(utente);
        Utente utente1=utenteDAO.doGet(utente.getEmail(),utente.getPassword());
        assertEquals(utente.getUsername(),utente1.getUsername());
        assertEquals(utente.getEmail(),utente1.getEmail());
        truncateUtente();
    }

    @Test
    public void doDeleteWithUsernameNull(){
        assertThrows(IllegalArgumentException.class,()->utenteDAO.doDelete(null));
    }

    @Test
    public void doDeleteOggettoNonTrovatoException(){
        assertThrows(OggettoNonTrovatoException.class,()->utenteDAO.doDelete("Umby"));
    }

    @Test
    public void doDeleteOK() throws NoSuchAlgorithmException, SQLException {
        Utente utente=insertUtente();
        utenteDAO.doSave(utente);
        utenteDAO.doDelete(utente.getUsername());
        assertEquals(false,utenteDAO.exist(utente.getUsername()));
        truncateUtente();
    }

/*va fatto test black box sui metodi isValid, guardando la category partition*/
    @Test
    public void isValidUsernameEmpty(){assertEquals(false,utenteDAO.isValidUsername(" "));}
    @Test
    public void isValidUsernameOK(){assertEquals(true,utenteDAO.isValidUsername("Umberto"));}



    @Test
    public void isValidEmailEmpty(){assertEquals(false, utenteDAO.isValidEmail(" "));}
    @Test
    public void isValidEmailEmptyPrefix(){assertEquals(false, utenteDAO.isValidEmail("@gmail.com"));}
    @Test
    public void isValidEmailPrefix(){assertEquals(false, utenteDAO.isValidEmail("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa@gmail.com"));}
    @Test
    public void isValidEmailEmptyPostfix(){assertEquals(false, utenteDAO.isValidEmail("marioRossi@"));}
    @Test
    public void isValidEmailEmptyPostfixLenght(){assertEquals(false, utenteDAO.isValidEmail("antonio73@aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.com"));}
    @Test
    public void isValidEmailNoChiocciola(){assertEquals(false, utenteDAO.isValidEmail("antonio73gmail.com"));}



    @Test
    public void isValidPasswdNoLetteraMaiuscola(){assertEquals(false,utenteDAO.isValidPasswd("dddd9dddd"));}
    @Test
    public void isValidPasswdOK(){assertEquals(true,utenteDAO.isValidPasswd("umBerto9"));}
    @Test
    public void isValidPasswdNoNumero(){assertEquals(false,utenteDAO.isValidPasswd("umBerto"));}
    @Test
    public void isValidPasswdNoLenght(){assertEquals(false,utenteDAO.isValidPasswd("pippo"));}
    @Test
    public void isValidPasswdNoMinuscola(){assertEquals(false,utenteDAO.isValidPasswd("PLUTO"));}

    private Utente insertUtente() throws NoSuchAlgorithmException {
        Utente user=new Utente();
        user.setUsername("Umby");
        user.setEmail("umberto.dellaMonica@gmail.com");
        user.setPassword("ciao2");
        return user;
    }

    @After
    public void tearDown() throws SQLException {
        truncateUtente();
    }

    private void truncateUtente() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE utente;");
        preparedStatement.executeUpdate();
    }


}
