package logic.gestionePlaylist;

import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;
import data.DAOPlaylist.Playlist;
import data.DAOPlaylist.PlaylistAPI;
import data.DAOPlaylist.PlaylistDAO;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;
import data.Exceptions.OggettoNonTrovatoException;
import org.json.simple.JSONObject;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Random;


public class JsonPlaylistServletIntegrationTest {

    MockHttpServletRequest request;
    PlaylistAPI playlistAPI;
    UtenteAPI utenteAPI;
    String username, titoloPlaylist;
    JsonPlaylistServlet jsonPlaylistServlet;
    HttpSession session;

    @Before
    public void setUp() throws SQLException, NoSuchAlgorithmException {
        request = new MockHttpServletRequest();
        playlistAPI = new PlaylistDAO();
        utenteAPI = new UtenteDAO();
        username = utenteAPI.doRetrieveAllUtenti().get(0).getUsername();//prendo la username di un utente che sta nel db

        jsonPlaylistServlet = new JsonPlaylistServlet();

        titoloPlaylist = "playlistRock1";

        Playlist playlist = new Playlist();
        playlist.setTitolo(titoloPlaylist);
        playlist.setUsername(username);

        playlistAPI.doSave(playlist);
        session = new MockHttpSession();
        session.setAttribute("username", username);
        request.setSession(session);
    }

    @Test
    public void checkTitoloGiaPresenteTest() throws SQLException {
        request.setParameter("titolo", titoloPlaylist);

        assertEquals(true,jsonPlaylistServlet.checkTitoloGiaPresente(request,playlistAPI,utenteAPI).get("checkT"));
    }

    @Test
    public void checkTitoloNonPresenteTest() throws SQLException {
        Random random = new Random();
        request.setParameter("titolo", titoloPlaylist+random.nextInt(50));

        assertEquals(false, jsonPlaylistServlet.checkTitoloGiaPresente(request,playlistAPI,utenteAPI).get("checkT"));
    }

    @After
    public void tearDown() throws SQLException {
        playlistAPI.doDelete(titoloPlaylist+";"+username);
    }

}
