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

public class RegistrazioneTest {

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    Registrazione registrazione;
    UtenteAPI utenteAPI;
    MockHttpSession session;

    @Before
    public void setUp(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        registrazione = new Registrazione();
        utenteAPI = Mockito.mock(UtenteDAO.class);
        session = new MockHttpSession();
    }
    @Test
    public void registerEmailNonRispettaFormatoTest() throws SQLException, NoSuchAlgorithmException, ServletException, IOException {
        String email = "@gmail.com"; // non corretta
        String username = "antonio_73"; //corretta
        String passwd = "Pluto1234", passwdCheck = "Pluto1234"; //corretta

        setParametersRequest(email,username,passwd,passwdCheck);

        Mockito.when(utenteAPI.isValidEmail(email)).thenReturn(false); //email non rispetta il fromato
        Mockito.when(utenteAPI.findUsers("email",email)).thenReturn(new ArrayList<>()); //email non presente nel DB
        Mockito.when(utenteAPI.isValidPasswd(passwd)).thenReturn(true); //password rispetta il formato
        Mockito.when(utenteAPI.findUsers("username",username)).thenReturn(new ArrayList<>());//username non presente nel DB
        Mockito.when(utenteAPI.isValidUsername(username)).thenReturn(true);//rispetta il formato
        registrazione.register(request,response,utenteAPI);

        assertParametersRequest(email,username,passwd,passwdCheck);
        assertEquals("Email non valida",request.getAttribute("errEmail"));
    }
    @Test
    public void registerUsernameNonRispettaFormatoTest() throws SQLException, NoSuchAlgorithmException, ServletException, IOException {
        String email = "antonio73@gmail.com"; //corretta
        String username = " "; // non corretta
        String passwd = "Pluto1234", passwdCheck = "Pluto1234"; //corretta

        setParametersRequest(email,username,passwd,passwdCheck);

        Mockito.when(utenteAPI.isValidEmail(email)).thenReturn(true); //email rispetta il fromato
        Mockito.when(utenteAPI.findUsers("email",email)).thenReturn(new ArrayList<>()); //email non presente nel DB
        Mockito.when(utenteAPI.isValidPasswd(passwd)).thenReturn(true); //password rispetta il formato
        Mockito.when(utenteAPI.findUsers("username",username)).thenReturn(new ArrayList<>());//username non presente nel DB
        Mockito.when(utenteAPI.isValidUsername(username)).thenReturn(false);//non rispetta il formato

        registrazione.register(request,response,utenteAPI);

        assertParametersRequest(email,username,passwd,passwdCheck);
        assertEquals("La username non rispetta il formato",request.getAttribute("errUsernameFormato"));
    }


    @Test
    public void registerUsernameAssociataAUtenteTest() throws SQLException, NoSuchAlgorithmException, ServletException, IOException {
        String email = "antonio73@gmail.com"; //corretta
        String username = "antonio_73"; //corretta
        String passwd = "Pluto1234", passwdCheck = "Pluto1234"; //corretta

        setParametersRequest(email,username,passwd,passwdCheck);

        Mockito.when(utenteAPI.isValidEmail(email)).thenReturn(true); //email rispetta il fromato
        Mockito.when(utenteAPI.findUsers("email",email)).thenReturn(new ArrayList<>()); //email non presente nel DB
        Mockito.when(utenteAPI.isValidPasswd(passwd)).thenReturn(true); //password rispetta il formato

        ArrayList<Utente> list = new ArrayList<>();
        list.add(new Utente());
        Mockito.when(utenteAPI.findUsers("username",username)).thenReturn(list);//username gia presente nel DB
        Mockito.when(utenteAPI.isValidUsername(username)).thenReturn(true);//rispetta il formato

        registrazione.register(request,response,utenteAPI);

        assertParametersRequest(email,username,passwd,passwdCheck);
        assertEquals("Username già presente",request.getAttribute("errUsername"));
    }


    @Test
    public void registerPasswordNonRispettaFormatoTest() throws SQLException, NoSuchAlgorithmException, ServletException, IOException {
        String email = "antonio73@gmail.com"; //corretta
        String username = "antonio_73"; //corretta
        String passwd = "pluto1234"; //non rispetta il formato
        String passwdCheck = "Pluto1234"; //corretta

        setParametersRequest(email,username,passwd,passwdCheck);

        Mockito.when(utenteAPI.isValidEmail(email)).thenReturn(true); //email rispetta il fromato
        Mockito.when(utenteAPI.findUsers("email",email)).thenReturn(new ArrayList<>()); //email non presente nel DB
        Mockito.when(utenteAPI.isValidPasswd(passwd)).thenReturn(false); //password NON rispetta il formato
        Mockito.when(utenteAPI.findUsers("username",username)).thenReturn(new ArrayList<>());//username non presente nel DB
        Mockito.when(utenteAPI.isValidUsername(username)).thenReturn(true);//rispetta il formato


        registrazione.register(request,response,utenteAPI);

        assertParametersRequest(email,username,passwd,passwdCheck);
        assertEquals("Password non valida",request.getAttribute("errPasswd"));
    }

    @Test
    public void registerPasswordNonCoincidonoTest() throws SQLException, ServletException, NoSuchAlgorithmException, IOException {
        String email = "antonio73@gmail.com"; //corretta
        String username = "antonio_73"; //corretta
        String passwd = "Pluto1234"; //corretta
        String passwdCheck = "Pluto12345"; //non coincidono

        setParametersRequest(email,username,passwd,passwdCheck);

        Mockito.when(utenteAPI.isValidEmail(email)).thenReturn(true); //email rispetta il fromato
        Mockito.when(utenteAPI.findUsers("email",email)).thenReturn(new ArrayList<>()); //email non presente nel DB
        Mockito.when(utenteAPI.isValidPasswd(passwd)).thenReturn(true); //password rispetta il formato
        Mockito.when(utenteAPI.findUsers("username",username)).thenReturn(new ArrayList<>());//username non presente nel DB
        Mockito.when(utenteAPI.isValidUsername(username)).thenReturn(true);//rispetta il formato

        registrazione.register(request,response,utenteAPI);

        assertParametersRequest(email,username,passwd,passwdCheck);
        assertEquals("Le password non coincidono",request.getAttribute("errPasswdNE"));
    }

    @Test
    public void registerInserimentoUtente() throws SQLException, NoSuchAlgorithmException, ServletException, IOException {
        String email = "antonio73@gmail.com"; //tutto corretto
        String username = "antonio_73";
        String passwd = "Pluto1234";
        String passwdCheck = "Pluto1234";

        setParametersRequest(email,username,passwd,passwdCheck);

        Mockito.when(utenteAPI.isValidEmail(email)).thenReturn(true); //email rispetta il fromato
        Mockito.when(utenteAPI.findUsers("email",email)).thenReturn(new ArrayList<>()); //email non presente nel DB
        Mockito.when(utenteAPI.isValidPasswd(passwd)).thenReturn(true); //password rispetta il formato
        Mockito.when(utenteAPI.findUsers("username",username)).thenReturn(new ArrayList<>());//username non presente nel DB
        Mockito.when(utenteAPI.isValidUsername(username)).thenReturn(true);//rispetta il formato

        request.setSession(session);

        registrazione.register(request,response,utenteAPI);

        assertEquals(session.getAttribute("username"),username);
        assertEquals(session.getAttribute("isLogged"),true);
        assertEquals(response.getRedirectedUrl(),"./index.html");
    }



    @After
    public void tearDown(){

    }


    private void setParametersRequest(String email, String username, String passwd, String passwdCheck){
        request.setParameter("email",email);
        request.setParameter("username", username);
        request.setParameter("passwd", passwd);
        request.setParameter("passwdCheck", passwdCheck);
    }

    //verifica se nella request sono stati inseriti email, username e passwd corrette
    //serve se flag>0 in register
    private void assertParametersRequest(String email, String username, String passwd, String passwdCheck){
        assertEquals(email,request.getAttribute("email"));
        assertEquals(username,request.getAttribute("username"));
        assertEquals(passwd,request.getAttribute("passwd"));
        assertEquals(passwdCheck,request.getAttribute("passwdCheck"));
    }
}
