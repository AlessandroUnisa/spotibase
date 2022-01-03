package logic.gestionePreferenza;
import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;
import data.DAOPreferenza.Preferenza;
import data.DAOPreferenza.PreferenzaAPI;
import data.DAOPreferenza.PreferenzaDAO;
import data.DAOUtente.Utente;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;

import logic.gestioneAutenticazione.Registrazione;
import logic.gestioneCanzone.JsonCanzoneServlet;
import org.json.simple.JSONObject;
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

public class PreferenzaTest {
    JsonPreferitiServlet jsonPreferitiServlet;
    PreferenzaAPI preferenzaAPI;
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    UtenteAPI utenteAPI;
    CanzoneAPI canzoneAPI;
    MockHttpSession session;
    JSONObject obj;

    @Before
    public void setUp(){
        obj = new JSONObject();
        jsonPreferitiServlet = new JsonPreferitiServlet();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        preferenzaAPI = Mockito.mock(PreferenzaDAO.class);
        utenteAPI = Mockito.mock(UtenteDAO.class);
        canzoneAPI = Mockito.mock(CanzoneDAO.class);
        session = new MockHttpSession();
    }
    @Test
    public void JsonPreferitiSetPreferenzaAggiuntaOkTest() throws SQLException, NoSuchAlgorithmException, ServletException, IOException{
        String username = "antonio_73";
        String codCanzone= "C01";
        ArrayList<String> codCanzoni = new ArrayList<>();
        codCanzoni.add("C01");
        request.setSession(session);
        Mockito.when(preferenzaAPI.doRetrieveCodiciCanzoniPreferite(username)).thenReturn(codCanzoni);
        jsonPreferitiServlet.setPreferenzaCanzone(username,codCanzone,preferenzaAPI,obj);
        assertEquals(true,obj.get("flag"));


    }
    @Test
    public void JsonPreferitiSetPreferenzaRimossaOkTest() throws SQLException, NoSuchAlgorithmException, ServletException, IOException{
        String username = "antonio_73";

        Mockito.when(preferenzaAPI.doRetrieveCodiciCanzoniPreferite(username)).thenReturn(new ArrayList<>());
        //Mockito.when(prefe);
        jsonPreferitiServlet.setPreferenzaCanzone(username,null,preferenzaAPI,obj);
        assertEquals(true,obj.get("flag"));


    }

    private void setParametersRequest(String username,String codCanzone){
        request.setParameter("username",username);
        request.setParameter("codCanzone",codCanzone);

    }

}
