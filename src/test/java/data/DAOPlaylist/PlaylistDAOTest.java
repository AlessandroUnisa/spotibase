package data.DAOPlaylist;

import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;
import data.DAOUtente.Utente;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;
import data.Exceptions.OggettoGiaPresenteException;
import data.Exceptions.OggettoNonTrovatoException;
import data.utils.SingletonJDBC;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PlaylistDAOTest {

    Connection connection;
    PlaylistDAO playlistDAO;
    UtenteAPI utenteAPI;
    CanzoneAPI canzoneAPI;


    @Before
    public void setUp(){
        connection= SingletonJDBC.getConnectionTesting();
        playlistDAO = new PlaylistDAO(connection);
        utenteAPI = Mockito.mock(UtenteDAO.class);
        canzoneAPI = Mockito.mock(CanzoneDAO.class);
    }

    @Test
    public void doRetrievePlaylistByUtenteOkTest() throws SQLException{
        truncatePlaylist();
        String username = "username"; //parametro di doRetrieve
        String titolo1 = "titolo1", nota1 = "nota1";

        Playlist p1 = new Playlist();
        p1.setNote(nota1);
        p1.setTitolo(titolo1);
        p1.setUsername(username);

        playlistDAO.doSave(p1);

        List<Playlist> listaPlaylist = new ArrayList<>();

        listaPlaylist.add(p1);

        List<Utente> listaUtente = new ArrayList<>();
        Utente utente = Mockito.mock(Utente.class);
        Mockito.when(utente.getUsername()).thenReturn(username);

        listaUtente.add(utente);

        Mockito.when(utenteAPI.findUsers("username", utente.getUsername())).thenReturn(listaUtente);

        List<Playlist> playlistRetrieved = playlistDAO.doRetrievePlaylistByUtente(username, utenteAPI);

        assertEquals(listaPlaylist.size(),playlistRetrieved.size());

        for(int i = 0; i < listaPlaylist.size(); i++)
            assertEquals(true,listaPlaylist.contains(playlistRetrieved.get(i)));
    }

    @Test
    public void doInsertSongUserExistTest() throws SQLException{
        String username = "username";
        String titolo = "titolo";
        String codCanzone = "C01";
        Mockito.when(utenteAPI.exist(username)).thenReturn(false);
        assertThrows(OggettoNonTrovatoException.class, () -> playlistDAO.doInsertSong(username,titolo,codCanzone, utenteAPI, canzoneAPI,playlistDAO));
    }

    @Test
    public void doInsertSongCanzoneExistTest() throws SQLException{
        String username = "username";
        String titolo = "titolo";
        String codCanzone = "C01";
        Mockito.when(canzoneAPI.exist(codCanzone)).thenReturn(false);
        assertThrows(OggettoNonTrovatoException.class, () -> playlistDAO.doInsertSong(username,titolo,codCanzone, utenteAPI, canzoneAPI,playlistDAO));
    }

    @Test
    public void doInsertSongPlaylistExistTest() throws SQLException{
        String username = "username";
        String titolo = "titolo";
        String codCanzone = "C01";

        assertThrows(OggettoNonTrovatoException.class, () -> playlistDAO.doInsertSong(username,titolo,codCanzone, utenteAPI, canzoneAPI,playlistDAO));
    }

    @Test
    public void doInsertSongPlaylistOkTest() throws SQLException {
        truncatePlaylist();
        String codCanzone = "C01";
        String titoloPlay = "titolo";
        String username = "username";

        Playlist playlist = new Playlist();
        playlist.setTitolo(titoloPlay);
        playlist.setUsername(username);

        playlistDAO.doSave(playlist);
        Mockito.when(utenteAPI.exist(username)).thenReturn(true);
        Mockito.when(canzoneAPI.exist(codCanzone)).thenReturn(true);

        playlistDAO.doInsertSong(username,titoloPlay,codCanzone,utenteAPI,canzoneAPI,playlistDAO);
        assertEquals(true,playlistDAO.isPresent(codCanzone,titoloPlay,username,canzoneAPI,playlistDAO,utenteAPI));
        truncatePlaylist();
    }


    @Test
    public void isPresentUserExistTest() throws SQLException{
        String username = "username";
        String titolo = "titolo";
        String codCanzone = "C01";
        Mockito.when(utenteAPI.exist(username)).thenReturn(false);
        assertThrows(OggettoNonTrovatoException.class, () -> playlistDAO.isPresent(codCanzone,titolo,username, canzoneAPI,playlistDAO, utenteAPI));
    }

    @Test
    public void isPresentCanzoneExistTest() throws SQLException{
        String username = "username";
        String titolo = "titolo";
        String codCanzone = "C01";
        Mockito.when(canzoneAPI.exist(codCanzone)).thenReturn(false);
        assertThrows(OggettoNonTrovatoException.class, () -> playlistDAO.isPresent(codCanzone,titolo,username, canzoneAPI,playlistDAO, utenteAPI));
    }

    @Test
    public void isPresentPlaylistExistTest() throws SQLException{
        String username = "username";
        String titolo = "titolo";
        String codCanzone = "C01";

        assertThrows(OggettoNonTrovatoException.class, () -> playlistDAO.isPresent(codCanzone,titolo,username, canzoneAPI,playlistDAO, utenteAPI));
    }

    @Test
    public void isPresentOkTest() throws SQLException{ //is present con 3 parametri
        truncatePlaylist();
        //creo la playlist --> salvo nel db
        //creo una canzone --> salvo nel db
        //aggiungo la canzone alla playlist --> salvo in playlist la canzone con doInsert
        //chiamo isPresent con 3 parametri e verifico la correttezza

        String username = "username";
        String titoloPlay = "titolo";
        String nota = "note";
        String codCanzone = "C01";
        String titoloCanzone = "titoloCanzone";

        Playlist playlist = new Playlist();
        playlist.setUsername(username);
        playlist.setTitolo(titoloPlay);
        playlist.setNote(nota);
        playlistDAO.doSave(playlist);

        Mockito.when(utenteAPI.exist(username)).thenReturn(true);
        Mockito.when(canzoneAPI.exist(codCanzone)).thenReturn(true);

        playlistDAO.doInsertSong(username,titoloPlay,codCanzone,utenteAPI,canzoneAPI,playlistDAO);
        assertEquals(true, playlistDAO.isPresent(codCanzone,titoloPlay,username,canzoneAPI,playlistDAO,utenteAPI));
        truncatePlaylist();
    }

    @Test
    public void isPresentUserOtherExistTest() throws SQLException{ //is present con 2 parametri

        String username = "username";
        String titolo = "titolo";
        String codCanzone = "C01";
        Mockito.when(canzoneAPI.exist(codCanzone)).thenReturn(false);
        assertThrows(OggettoNonTrovatoException.class, () -> playlistDAO.isPresent(titolo,username, utenteAPI));
    }

    @Test
    public void isPresentUserOtherExistOkTest() throws SQLException{ //is present con 2 parametri
        truncatePlaylist();
        //inserisco la canzone in playlist
        String username = "username";
        String titolo = "titolo";
        Playlist playlist = new Playlist();
        playlist.setTitolo(titolo);
        playlist.setUsername(username);
        playlistDAO.doSave(playlist);

        Mockito.when(utenteAPI.exist(username)).thenReturn(true);
        //chiamo isPresent con 2 parametri
        assertEquals(true,playlistDAO.isPresent(titolo,username,utenteAPI));
        truncatePlaylist();
    }

    @Test
    public void doRetrieveNumPlaylistOfUtenteExistTest() throws SQLException{
        String username = "username";
        Mockito.when(utenteAPI.exist(username)).thenReturn(false);
        assertThrows(OggettoNonTrovatoException.class, () -> playlistDAO.doRetrieveNumPlaylistOfUtente(username, utenteAPI));
    }

    @Test
    public void doRetrieveNumPlaylistOfUtenteOkTest() throws SQLException{
        truncatePlaylist();
        String username = "username";
        String titolo1 = "titolo1", nota1 ="nota1";
        String titolo2 = "titolo2", nota2 ="nota2";

        List<Playlist> playlists = new ArrayList<>();
        Playlist playlist1 = new Playlist();
        playlist1.setUsername(username);
        playlist1.setTitolo(titolo1);
        playlist1.setNote(nota1);

        Playlist playlist2 = new Playlist();
        playlist2.setUsername(username);
        playlist2.setTitolo(titolo2);
        playlist2.setNote(nota2);

        playlists.add(playlist1);
        playlists.add(playlist2);

        playlistDAO.doSave(playlist1);
        playlistDAO.doSave(playlist2);

        Mockito.when(utenteAPI.exist(username)).thenReturn(true);

        assertEquals(playlists.size(),playlistDAO.doRetrieveNumPlaylistOfUtente(username,utenteAPI));
        truncatePlaylist();
    }

    @Test
    public void doInsertSongParametersNullTest() throws SQLException{
        assertThrows(IllegalArgumentException.class,() -> playlistDAO.doInsertSong(null,"a","b", utenteAPI, canzoneAPI,playlistDAO));
        assertThrows(IllegalArgumentException.class,() -> playlistDAO.doInsertSong("username",null,"b", utenteAPI, canzoneAPI,playlistDAO));
        assertThrows(IllegalArgumentException.class,() -> playlistDAO.doInsertSong("username","a",null, utenteAPI, canzoneAPI,playlistDAO));
    }

    @Test
    public void doRetrievePlaylistByUtenteNullParametersTest() throws SQLException{
        assertThrows(IllegalArgumentException.class,() -> playlistDAO.doRetrievePlaylistByUtente(null, utenteAPI));
    }

    @Test
    public void isPresentNullParametersTest() throws SQLException{
        assertThrows(IllegalArgumentException.class,() -> playlistDAO.isPresent(null,"titolo","username", canzoneAPI,playlistDAO, utenteAPI));
        assertThrows(IllegalArgumentException.class,() -> playlistDAO.isPresent("c01",null,"username", canzoneAPI,playlistDAO, utenteAPI));
        assertThrows(IllegalArgumentException.class,() -> playlistDAO.isPresent("c01","titolo",null, canzoneAPI,playlistDAO, utenteAPI));
    }

    @Test
    public void isPresentNullParameterTest() throws SQLException{
        assertThrows(IllegalArgumentException.class,() -> playlistDAO.isPresent(null,"username", utenteAPI));
        assertThrows(IllegalArgumentException.class,() -> playlistDAO.isPresent("titolo",null, utenteAPI));
    }

    @Test
    public void doRetrieveNumPlaylistOfUtenteNullParameterTest() throws SQLException{
        assertThrows(IllegalArgumentException.class,() -> playlistDAO.doRetrieveNumPlaylistOfUtente(null,utenteAPI));
    }

    @Test
    public void isValidTitoloNullTest() throws SQLException{
        assertEquals(false, playlistDAO.isValidTitolo(""));
    }

    @Test
    public void isValidTitoloOkTest() throws SQLException{
        assertEquals(true, playlistDAO.isValidTitolo("titolo"));
    }


    @Test
    public void isValidNotaNullTest() throws SQLException{
        assertEquals(true, playlistDAO.isValidNota(null));
    }

    @Test
    public void isValidNotaOkTest() throws SQLException{
        assertEquals(true, playlistDAO.isValidNota("nota"));
    }

    //fine metodi di Silvio

    @Test
    public void doGetWithChiaveNullAndNotContainsSemicolon(){
        assertThrows(IllegalArgumentException.class,()->playlistDAO.doGet(null));
        assertThrows(IllegalArgumentException.class,()->playlistDAO.doGet("sakdhasdk"));
    }

    @Test
    public void doGetOggettoNonTrovatoException(){
        String chiave="askdasd;asdgkd";
        assertThrows(OggettoNonTrovatoException.class,()->playlistDAO.doGet(chiave));
    }

    @Test
    public void doGetOK() throws SQLException {
        truncatePlaylist();
        Playlist playlist=insertPlayList();
        playlistDAO.doSave(playlist);
        Playlist playlist1=playlistDAO.doGet(playlist.getTitolo()+";"+playlist.getUsername());
        assertEquals(playlist.getTitolo(),playlist1.getTitolo());
        assertEquals(playlist.getNote(),playlist1.getNote());
        assertEquals(playlist.getUsername(),playlist1.getUsername());
        truncatePlaylist();
    }

    @Test
    public void doSaveWithPlayListNull(){
        assertThrows(IllegalArgumentException.class,()->playlistDAO.doSave(null));
    }

    @Test
    public void doSavePlayListWithTitoloNull(){
        Playlist playlist=new Playlist();
        playlist.setTitolo(null);
        assertThrows(IllegalArgumentException.class,()->playlistDAO.doSave(playlist));
    }

    @Test
    public void doSavePlayListWithUsernameNull(){
        Playlist playlist=new Playlist();
        playlist.setTitolo("Ciao");
        playlist.setUsername(null);
        assertThrows(IllegalArgumentException.class,()->playlistDAO.doSave(playlist));
    }

    @Test
    public void doSavePlayListOggettoGiaPresente() throws SQLException {
        Playlist playlist=new Playlist();
        playlist.setTitolo("Ciao");
        playlist.setUsername("Umby");
        playlist.setNote("asddasda");
        playlistDAO.doSave(playlist);
        assertThrows(OggettoGiaPresenteException.class,()->playlistDAO.doSave(playlist));
        truncatePlaylist();
    }

    @Test
    public void doSavePlayListOK() throws SQLException {
        truncatePlaylist();
        Playlist playlist=new Playlist();
        playlist.setTitolo("Ciao");
        playlist.setUsername("Umby");
        playlist.setNote("asddasda");
        playlistDAO.doSave(playlist);

        Mockito.when(utenteAPI.exist(playlist.getUsername())).thenReturn(true);
        assertEquals(true,playlistDAO.isPresent(playlist.getTitolo(),playlist.getUsername(),utenteAPI));
        truncatePlaylist();
    }

    @Test
    public void doDeleteWithParameterNullOrNotContainsSemicolon(){
        assertThrows(IllegalArgumentException.class,()->playlistDAO.doDelete(null));
        assertThrows(IllegalArgumentException.class,()->playlistDAO.doDelete("asdasdas"));
    }

    @Test
    public void doDeleteOggettoNonTrovato(){
        assertThrows(OggettoNonTrovatoException.class,()->playlistDAO.doDelete("Ciao;Umby"));
    }

    @Test
    public void doDeleteOK() throws SQLException {
        truncatePlaylist();
        Playlist playlist=new Playlist();
        playlist.setTitolo("Ciao");
        playlist.setUsername("Umby");
        playlist.setNote("asddasda");
        playlistDAO.doSave(playlist);
        Mockito.when(utenteAPI.exist(playlist.getUsername())).thenReturn(true);
        playlistDAO.doDelete(playlist.getTitolo()+";"+playlist.getUsername());
        assertEquals(false,playlistDAO.isPresent(playlist.getTitolo(),playlist.getUsername(),utenteAPI));
    }

    private Playlist insertPlayList() {
        Playlist playlist=new Playlist();
        playlist.setNote("sdibscsdc");
        playlist.setTitolo("Ciao");
        playlist.setUsername("Umby");
        return playlist;
    }

    private void truncatePlaylist() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("DELETE from playlist ;");
        preparedStatement.executeUpdate();
    }
}

