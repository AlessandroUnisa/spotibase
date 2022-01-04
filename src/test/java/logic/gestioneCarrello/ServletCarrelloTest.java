package logic.gestioneCarrello;

import data.Album.Album;
import data.Album.AlbumDAO;
import data.DAOAcquisto.AcquistoAPI;
import data.DAOAcquisto.AcquistoDAO;
import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;
import static org.junit.Assert.*;
import org.junit.*;
import org.mockito.Mockito;
import org.springframework.mock.web.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;



public class ServletCarrelloTest {

    MockHttpServletResponse response;
    MockHttpServletRequest request;
    ServletCarrello servletCarrello;
    MockHttpSession session;
    CanzoneAPI canzoneAPI;
    AcquistoAPI acquistoAPI;
    TreeSet<String> codiciInCarrello;
    AlbumDAO albumDAO;

    @Before
    public void setUp(){
        servletCarrello = new ServletCarrello();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        session = new MockHttpSession();
        request.setSession(session);
        canzoneAPI = Mockito.mock(CanzoneDAO.class);
        acquistoAPI = Mockito.mock(AcquistoDAO.class);
        codiciInCarrello = new TreeSet<>();
        albumDAO = Mockito.mock(AlbumDAO.class);
    }

    @Test
    public void visualizzaCarrelloListCartNullTest() throws SQLException {
        servletCarrello.visualizzaCarrello(request,canzoneAPI,acquistoAPI,albumDAO);
        assertEquals(0, ((TreeSet<String>)session.getAttribute("listCart")).size());
    }

    @Test
    public void visualizzaCarrelloCartMaggioreZeroTest() throws SQLException {
        String codCanzone1 = "C01", codCanzone2 = "C02", codAlbum1 = "A01", codAlbum2 = "A02";
        codiciInCarrello.add(codAlbum1);
        codiciInCarrello.add(codAlbum2);
        codiciInCarrello.add(codCanzone1);
        codiciInCarrello.add(codCanzone2);

        session.setAttribute("listCart",codiciInCarrello);

        String username = "pluto";
        session.setAttribute("username",username);

        ArrayList<String> codiciInCarrelloList = new ArrayList<>(codiciInCarrello);

        ArrayList<Canzone> canzoniList = new ArrayList<>();
        Canzone canzone1 = Mockito.mock(Canzone.class);
        Mockito.when(canzone1.getCodice()).thenReturn(codCanzone1);
        Mockito.when(canzone1.getPrezzo()).thenReturn(3.0);
        canzoniList.add(canzone1);
        Canzone canzone2 = Mockito.mock(Canzone.class);
        Mockito.when(canzone2.getPrezzo()).thenReturn(3.0);
        Mockito.when(canzone2.getCodice()).thenReturn(codCanzone2);
        canzoniList.add(canzone2);
        Mockito.when(canzoneAPI.doRetrieveCanzoniByCodiciWithArtisti(codiciInCarrelloList)).thenReturn(canzoniList);

        ArrayList<Album> albumList = new ArrayList<>();
        Album album1 = Mockito.mock(Album.class);
        albumList.add(album1);
        Mockito.when(album1.getCodice()).thenReturn(codAlbum1);
        Mockito.when(album1.getPrezzo()).thenReturn(10.0);
        Album album2 = Mockito.mock(Album.class);
        albumList.add(album2);
        Mockito.when(album2.getCodice()).thenReturn(codAlbum2);
        Mockito.when(album2.getPrezzo()).thenReturn(10.0);
        Mockito.when(albumDAO.doRetrieveAlbumsByCodiciWithArtisti(codiciInCarrelloList)).thenReturn(albumList);

        ArrayList<String> canzoniAcquistate = new ArrayList<>();
        canzoniAcquistate.add(codCanzone1);
        Mockito.when(acquistoAPI.doRetrieveCodiciCanzoniAcquistate(username)).thenReturn(canzoniAcquistate);

        ArrayList<String> albumAcquistati = new ArrayList<>();
        albumAcquistati.add(codAlbum1);
        Mockito.when(albumDAO.doRetrieveCodiciAlbumAcquistati(username)).thenReturn(albumAcquistati);



        servletCarrello.visualizzaCarrello(request,canzoneAPI,acquistoAPI,albumDAO);

        assertEquals(2, codiciInCarrello.size());
        assertEquals(true, codiciInCarrello.contains(codCanzone2));
        assertEquals(false, codiciInCarrello.contains(codCanzone1));
        assertEquals(true, codiciInCarrello.contains(codAlbum2));
        assertEquals(false, codiciInCarrello.contains(codAlbum1));
        assertEquals(13.0,request.getAttribute("totale"));
        assertEquals(1,canzoniList.size());
        assertEquals(1,albumList.size());
    }

    @Test
    public void acquistoOkTest() throws SQLException{
        String codCanzone1 = "C01", codCanzone2 = "C02";
        codiciInCarrello.add(codCanzone1);
        codiciInCarrello.add(codCanzone2);
        session.setAttribute("listCart",codiciInCarrello);
        session.setAttribute("isLogged",true);
        String username = "pluto";
        session.setAttribute("username",username);
        Mockito.doNothing().when(acquistoAPI).doInsertCanzoneAcquistata(username,codCanzone1);
        Mockito.doNothing().when(acquistoAPI).doInsertCanzoneAcquistata(username,codCanzone2);
        servletCarrello.acquisto(request,acquistoAPI,albumDAO);
        assertEquals(0,codiciInCarrello.size());

    }
    @Test
    public void eliminaDalCarrelloTest(){
        String codiceDaEliminare = "C02";
        session.setAttribute("listCart",codiciInCarrello);
        request.setParameter("del",codiceDaEliminare);
        servletCarrello.eliminaDalCarrello(request);
        assertEquals(false, codiciInCarrello.contains(codiceDaEliminare));
    }


    @After
    public void tearDown(){

    }


}
