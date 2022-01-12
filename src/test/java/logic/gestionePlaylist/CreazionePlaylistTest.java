package logic.gestionePlaylist;

import data.DAOPlaylist.Playlist;
import data.DAOPlaylist.PlaylistAPI;
import data.DAOPlaylist.PlaylistDAO;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;
import data.Exceptions.OggettoGiaPresenteException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.Assert.*;


/*category partition sui possibili input
2.3 creazione playlist
* test case:
* 1.3_01 titolo = ""
  1.3_02 titolo = "some" -> error playlist gia esistente
  1.3_03 titolo = "some2" -> note = "" -> formato note non valido
  1.3_04 titolo = "some2" -> note "qualcosa" --> ok
 */

public class CreazionePlaylistTest {

    MockHttpServletRequest request;
    MockHttpServletResponse response;
    ServletPlaylist servletPlaylist;
    PlaylistAPI playlistAPI;
    MockHttpSession session;
    Playlist playlist;
    UtenteAPI utenteAPI;

    @Before
    public void setUp(){
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        servletPlaylist = new ServletPlaylist();
        playlistAPI = Mockito.mock(PlaylistDAO.class);
        session = new MockHttpSession();
        playlist = Mockito.mock(Playlist.class);
        utenteAPI = Mockito.mock(UtenteDAO.class);
    }

    @Test
    public void creaPlaylistNullTitleTest() throws SQLException {
        String username = "utente"; //corretto
        String titolo = null; //non corretto
        String note = "note"; //corretto
        request.setSession(session);
        session.setAttribute("username",username);
        request.setParameter("titolo",titolo);
        request.setParameter("note",note);

        Mockito.when(playlistAPI.isValidTitolo(titolo)).thenReturn(false);

        assertThrows(IllegalArgumentException.class,() ->servletPlaylist.creaPlaylist(request,response,playlistAPI));
    }

    @Test
    public void creaPlaylistExistTest() throws SQLException {
        String username = "utente"; //corretto
        String titolo = "titolo"; //corretto
        String note = "note"; //corretto
        request.setSession(session);
        session.setAttribute("username",username);
        request.setParameter("titolo",titolo);
        request.setParameter("note",note);

        Mockito.when(playlistAPI.isValidTitolo(titolo)).thenReturn(true);
        Mockito.when(playlistAPI.isPresent(titolo,username,utenteAPI)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,() ->servletPlaylist.creaPlaylist(request,response,playlistAPI));
        //assertEquals(true,playlistAPI.isPresent(titolo,username,utenteAPI));
    }

    @Test
    public void creaOkTest() throws SQLException, IOException {
        String username = "utente"; //corretto
        String titolo = "titolo"; //corretto
        String note = "note"; //corretto
        request.setSession(session);
        session.setAttribute("username", username);
        request.setParameter("titolo", titolo);
        request.setParameter("note", note);

        Mockito.when(playlistAPI.isValidTitolo(titolo)).thenReturn(true);
        Mockito.when(playlistAPI.isPresent(titolo, username,utenteAPI)).thenReturn(false);
        Mockito.when(playlistAPI.isValidNota(note)).thenReturn(true);
        servletPlaylist.creaPlaylist(request,response,playlistAPI);

        assertEquals(response.getRedirectedUrl(), "../libreria");
    }
}
