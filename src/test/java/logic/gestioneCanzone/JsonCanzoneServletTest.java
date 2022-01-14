package logic.gestioneCanzone;

import data.Artista.Artista;
import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;
import data.DAOPreferenza.PreferenzaAPI;
import data.DAOPreferenza.PreferenzaDAO;
import data.DAOUtente.Utente;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;
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

public class JsonCanzoneServletTest {

    JsonCanzoneServlet jsonCanzoneServlet;
    MockHttpServletRequest request;
    MockHttpServletResponse response;
    MockHttpSession session;
    ArrayList<String> codCanzoni;
    ArrayList<Artista> artisti;
    PreferenzaAPI preferenzaAPI;
    CanzoneAPI canzoneAPI;
    Canzone canzone;
    String code;

    @Before
    public void setUp() throws SQLException {
        jsonCanzoneServlet = new JsonCanzoneServlet();

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();

        preferenzaAPI = Mockito.mock(PreferenzaDAO.class);
        canzoneAPI = Mockito.mock(CanzoneDAO.class);
        canzone = Mockito.mock(Canzone.class);
        code = "C03";


        codCanzoni = new ArrayList<>();
        codCanzoni.add("C03");

        artisti = new ArrayList<>();
        Artista artista = Mockito.mock(Artista.class);
        Mockito.when(artista.getNomeDArte()).thenReturn("nomeArteArtista");
        artisti.add(artista);

        request.setSession(session);
        request.setParameter("cod",code);
        Mockito.when(canzone.getCodice()).thenReturn(code);
        Mockito.when(canzone.getPathMP3()).thenReturn("path/mp3/canzone");
        Mockito.when(canzone.getArtisti()).thenReturn(artisti);
        Mockito.when(canzone.getTitolo()).thenReturn("titoloCanzone");
        Mockito.when(canzoneAPI.doRetrieveCanzoneWithArtisti(code)).thenReturn(canzone);

    }

    @Test
    public void getCanzoneUsernameNotNull() throws SQLException, ServletException, IOException {
        String username = "pluto";
        session.setAttribute("username",username);

        Mockito.when(preferenzaAPI.doRetrieveCodiciCanzoniPreferite(username)).thenReturn(codCanzoni);

        JSONObject objectJson = jsonCanzoneServlet.getCanzone(request,canzoneAPI,preferenzaAPI);
        assertEquals(objectJson.get("isLogged"),true);
        assertEquals(objectJson.get("pref"),true);
    }

    @Test
    public void getCanzoneUsernameNull() throws SQLException {
        String username = null;
        session.setAttribute("username",null);
        JSONObject objectJson = jsonCanzoneServlet.getCanzone(request,canzoneAPI,preferenzaAPI);
        this.assertObjectJson(objectJson);
    }

    private void assertObjectJson(JSONObject objectJson){
        assertEquals(objectJson.get("url"),"path/mp3/canzone");
        assertEquals(objectJson.get("titolo"),"titoloCanzone");
        assertEquals(objectJson.get("artista"),"nomeArteArtista");
    }

    @After
    public void tearDown(){

    }

}
