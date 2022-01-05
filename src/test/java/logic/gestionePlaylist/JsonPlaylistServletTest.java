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

import java.sql.SQLException;

public class JsonPlaylistServletTest {

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    ServletPlaylist servletPlaylist;
    PlaylistAPI playlistAPI;
    MockHttpSession session;
    Playlist playlist;
    UtenteAPI utenteAPI;
    CanzoneAPI canzoneAPI;
    JsonPlaylistServlet jsonPlaylistServlet;

    @Before
    public void setUp(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        servletPlaylist = new ServletPlaylist();
        playlistAPI = Mockito.mock(PlaylistDAO.class);
        session = new MockHttpSession();
        request.setSession(session);
        playlist = Mockito.mock(Playlist.class);
        utenteAPI = Mockito.mock(UtenteDAO.class);
        canzoneAPI = Mockito.mock(CanzoneDAO.class);
        jsonPlaylistServlet = new JsonPlaylistServlet();
    }

    @Test
    public void insertCanzoneGiaPresenteTest()throws SQLException {
        String username = "Pippotto";
        String codCan= "C01";
        String nomePlay= "Rap";
        JSONObject obj = new JSONObject();
        obj.put("flag","isPresent");
        setParametersRequest(codCan, nomePlay);
        session.setAttribute("username",username);
        Mockito.when(playlistAPI.isPresent(codCan,nomePlay,username,canzoneAPI,playlistAPI,utenteAPI)).thenReturn(true);
        assertEquals(obj,jsonPlaylistServlet.insertCanzone(request,canzoneAPI,playlistAPI,utenteAPI));
    }

    @Test
    public void insertCanzoneOkTest()throws SQLException{
        String username = "Pippotto";
        String codCan= "C01";
        String nomePlay= "Rap";
        JSONObject obj = new JSONObject();
        obj.put("flag",true);
        setParametersRequest(codCan, nomePlay);
        session.setAttribute("username",username);
        Mockito.when(playlistAPI.isPresent(codCan,nomePlay,username,canzoneAPI,playlistAPI,utenteAPI)).thenReturn(false);
        assertEquals(obj,jsonPlaylistServlet.insertCanzone(request,canzoneAPI,playlistAPI,utenteAPI));
    }

    @Test
    public void insertCanzoneOggettoNonTrovatoExceptionTest()throws SQLException{
        String username = "Pippotto";
        String codCan= "C01";
        String nomePlay= "Rap";
        JSONObject obj = new JSONObject();
        obj.put("flag",true);
        setParametersRequest(codCan, nomePlay);
        session.setAttribute("username",username);
        Mockito.when(playlistAPI.isPresent(codCan,nomePlay,username,canzoneAPI,playlistAPI,utenteAPI)).thenReturn(false);
        Mockito.doThrow(OggettoNonTrovatoException.class).when(playlistAPI).doInsertSong(username,nomePlay,codCan,utenteAPI,canzoneAPI,playlistAPI);
        assertThrows(OggettoNonTrovatoException.class,() -> jsonPlaylistServlet.insertCanzone(request,canzoneAPI,playlistAPI,utenteAPI));
    }


    private void setParametersRequest(String codCan, String nomePlay){

        request.setParameter("nomePlay",nomePlay);
        request.setParameter("codCan",codCan);

    }
}
