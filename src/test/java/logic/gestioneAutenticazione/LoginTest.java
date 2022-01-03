package logic.gestioneAutenticazione;


import data.DAOUtente.Utente;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.*;
import org.springframework.core.*;
import static org.junit.Assert.*;
import javax.servlet.ServletException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoginTest {
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    Login log;
    UtenteAPI utenteAPI;
    Utente utente;
    MockHttpSession session;

    @Before
    public void setUp(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        log = new Login();
        utenteAPI = Mockito.mock(UtenteDAO.class);
        utente = Mockito.mock(Utente.class);

        session = new MockHttpSession();
    }

    @Test
    public void loginUtenteEmailNonRispettaFormatoTest() throws SQLException, NoSuchAlgorithmException, ServletException, IOException{
        String email = "antonio73@gmaiom"; // non corretta
        String passwd = "Pluto1234"; // corretta
        setParametersRequest(email,passwd);
        Mockito.when(utenteAPI.doGet(email,passwd)).thenReturn(utente);
        log.login(request,response,utenteAPI);
        assertEquals("Credenziali errate",request.getAttribute("errCredenziali"));
    }

    @Test
    public void loginUtenteEmailNonAssociataNessunUtenteTest() throws SQLException, NoSuchAlgorithmException, ServletException, IOException{
        String email = "antonio73@gmail.com"; //corretta
        String passwd = "Pluto1234"; //corretta
        setParametersRequest(email,passwd);
        Mockito.when(utenteAPI.doGet(email,passwd)).thenReturn(utente);//email non presente nel DB
        Mockito.when(utente.getEmail()).thenReturn(email);
        log.login(request,response,utenteAPI);
        assertEquals("Credenziali errate",request.getAttribute("errCredenziali"));
    }

    @Test
    public void loginUtentePasswdNonAssociataNessunUtenteTest() throws SQLException, NoSuchAlgorithmException, ServletException, IOException{
        String email = "antonio73@gmail.com"; //corretta
        String passwd = "Pluto1234"; //corretta
        setParametersRequest(email,passwd);

        Utente utente = Mockito.mock(Utente.class);
        Mockito.when(utenteAPI.doGet(email,passwd)).thenReturn(utente);
        Mockito.when(utente.getPassword()).thenReturn(passwd); //passwd non presente nel DB

        log.login(request,response,utenteAPI);
        assertEquals("Credenziali errate",request.getAttribute("errCredenziali"));
    }

    @Test
    public void loginUtenteOkTest()throws SQLException, NoSuchAlgorithmException, ServletException, IOException {
        String email = "antonio73@gmail.com"; //corretta
        String passwd = "Pluto1234"; //corretta
        String username = "ciao";

        setParametersRequest(email,passwd);
        Utente utente2 = new Utente();
        utente2.setPassword(passwd);

        Mockito.when(utenteAPI.doGet(email,utente2.getPassword())).thenReturn(utente); //username presente nel DB

        Mockito.when(utente.getUsername()).thenReturn(username);
        Mockito.when(utenteAPI.isAdminEmail(email)).thenReturn(false);
        request.setSession(session);

        log.login(request,response,utenteAPI);

        assertEquals(true,session.getAttribute("isLogged"));
        assertEquals(username, session.getAttribute("username"));
        assertEquals(response.getRedirectedUrl(),"./index.html");
    }

    private void setParametersRequest(String email, String passwd){
        request.setParameter("email",email);
        request.setParameter("passwd", passwd);
    }

    //verifica se nella request sono stati inseriti email, username e passwd corrette
    private void assertParametersRequest(String email, String passwd){
        assertEquals(email,request.getAttribute("email"));
        assertEquals(passwd,request.getAttribute("passwd"));
    }




}
