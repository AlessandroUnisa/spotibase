package logic.gestionePlaylist;

import data.Artista.Artista;
import data.Artista.ArtistaDAO;
import data.DAOAcquisto.AcquistoAPI;
import data.DAOAcquisto.AcquistoDAO;
import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;
import data.DAOPlaylist.Playlist;
import data.DAOPlaylist.PlaylistAPI;
import data.DAOPlaylist.PlaylistDAO;
import data.DAOUtente.Utente;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;
import data.utils.SingletonJDBC;
import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;


public class ShowPlaylistTest {

    MockHttpServletRequest request;
    PlaylistAPI playlistAPI;
    CanzoneAPI canzoneAPI;
    AcquistoAPI acquistoAPI;
    UtenteAPI utenteAPI;
    ArtistaDAO artistaDAO;
    MockHttpSession session;
    Playlist playlist;
    String nomePlaylist, username;
    Canzone canzone;
    Utente utente;
    String passwdUtente;
    ServletPlaylist servletPlaylist;

    @Before
    public void setUp(){
        Connection connection = SingletonJDBC.getConnectionTesting();
        playlistAPI = new PlaylistDAO(connection);
        canzoneAPI = new CanzoneDAO(connection);
        artistaDAO = new ArtistaDAO(connection);
        acquistoAPI = new AcquistoDAO(connection);
        utenteAPI = new UtenteDAO(connection);

        request = new MockHttpServletRequest();
        session = new MockHttpSession();
        request.setSession(session);
        servletPlaylist = new ServletPlaylist();
    }


    @Test
    public void showPlaylistIsLoggedNullTest() throws SQLException, NoSuchAlgorithmException {
        fillDB();
        request.setParameter("name", playlist.getTitolo());
        session.setAttribute("username", utente.getUsername());

        Playlist playlistRitornata = servletPlaylist.showPlaylist(request,playlistAPI,canzoneAPI,acquistoAPI,artistaDAO);


        assertEquals(false,session.getAttribute("listCart")==null);
        assertEquals(true, playlistRitornata.equals(playlist));
        System.out.println("******"+playlistRitornata.getCanzoni());
        System.out.println(playlist.getCanzoni());
        assertEquals(true ,playlistRitornata.getCanzoni().equals(playlist.getCanzoni()));
        for(Canzone canzone : playlistRitornata.getCanzoni())
            for(int i=0; i<playlist.getCanzoni().size(); i++){
                Canzone canzonePlaylist = playlist.getCanzoni().get(i);
                if(canzonePlaylist.getCodice().equals(canzone.getCodice())){
                    assertEquals(true, canzonePlaylist.getArtisti().equals(canzone.getArtisti()));
                }
            }
        clean();
    }

    @After
    public void tearDown(){}


    public void  clean() throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnectionTesting().prepareStatement("SET FOREIGN_KEY_CHECKS=0;");
        preparedStatement.executeUpdate();

        preparedStatement = SingletonJDBC.getConnectionTesting().prepareStatement("TRUNCATE TABLE playlist;");
        preparedStatement.executeUpdate();

        preparedStatement = SingletonJDBC.getConnectionTesting().prepareStatement("TRUNCATE TABLE artista;");
        preparedStatement.executeUpdate();

        preparedStatement = SingletonJDBC.getConnectionTesting().prepareStatement("TRUNCATE TABLE canzone;");
        preparedStatement.executeUpdate();

        preparedStatement = SingletonJDBC.getConnectionTesting().prepareStatement("TRUNCATE TABLE raccoglie;");
        preparedStatement.executeUpdate();

        preparedStatement = SingletonJDBC.getConnectionTesting().prepareStatement("TRUNCATE TABLE scrivere;");
        preparedStatement.executeUpdate();

        preparedStatement = SingletonJDBC.getConnectionTesting().prepareStatement("TRUNCATE TABLE utente;");
        preparedStatement.executeUpdate();
    }



    public void fillDB() throws SQLException, NoSuchAlgorithmException {
        utente = new Utente();
        username = "pppppo";
        utente.setUsername(username);
        utente.setPassword("passwd");
        passwdUtente = utente.getPassword();
        utente.setEmail("ggg@ggg.ggg");
        UtenteAPI utenteAPI = new UtenteDAO(SingletonJDBC.getConnectionTesting());
        utenteAPI.doSave(utente);

        playlist = new Playlist();
        nomePlaylist = "rockPlay";
        playlist.setTitolo(nomePlaylist);

        playlist.setUsername(username);


        playlistAPI.doSave(playlist);

        canzone = new Canzone();
        canzone.setCodice("C01");
        canzone.setAnno(2000);
        canzone.setDurata(7);
        canzone.setTitolo("okok");
        canzone.setPrezzo(5);
        canzone.setPathImg("path");
        canzone.setPathMP3("bho");
        canzoneAPI.doSave(canzone);

        playlistAPI.doInsertSong(username,nomePlaylist,"C01",utenteAPI,canzoneAPI,playlistAPI);
        ArrayList<Canzone> canzoni = new ArrayList<>();
        canzoni.add(canzone);
playlist.setCanzoni(canzoni);
        Artista artista = new Artista();
        artista.setNome("pippo");
        artista.setNomeDArte("PP");
        artista.setCodFiscale("LKKJHHG");
        artista.setPathImg("bho/PP");
        artista.setCognome("paperino");

        artistaDAO.doSave(artista);

        ArrayList<Artista> artisti = new ArrayList<>();
        artisti.add(artista);
        canzone.setArtisti(artisti);

        PreparedStatement preparedStatement = SingletonJDBC.getConnectionTesting().prepareStatement("INSERT INTO scrivere VALUES (?,?);");
        preparedStatement.setString(1,artista.getCodFiscale());
        preparedStatement.setString(2,canzone.getCodice());
        preparedStatement.executeUpdate();




    }


}
