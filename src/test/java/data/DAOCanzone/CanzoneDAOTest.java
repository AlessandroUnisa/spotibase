package data.DAOCanzone;


import data.Album.Album;
import data.Album.AlbumDAO;
import data.Artista.Artista;
import data.Artista.ArtistaDAO;
import data.Exceptions.OggettoGiaPresenteException;
import data.Exceptions.OggettoNonInseritoException;
import data.Exceptions.OggettoNonTrovatoException;
import data.utils.SingletonJDBC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CanzoneDAOTest {

    CanzoneDAO canzoneDAO;
    ArtistaDAO artistaDAO;
    AlbumDAO albumDAO;
    Connection connection;


    @Before
    public void setUp(){
        connection=SingletonJDBC.getConnectionTesting();
        canzoneDAO=new CanzoneDAO(connection);
        albumDAO=Mockito.mock(AlbumDAO.class);
        artistaDAO=Mockito.mock(ArtistaDAO.class);
    }

    //CanzoneDAO exist

    @Test
    public void existWithCodiceNull(){
        String cod=null;
        assertThrows(IllegalArgumentException.class,()->canzoneDAO.exist(cod));
    }

    @Test
    public void existOK() throws SQLException {
        Album album=insertAlbum();
        Canzone canzone=insertCanzone();
        canzone.setAlbum(album);
        canzoneDAO.doSave(canzone);
        assertEquals(true,canzoneDAO.exist(canzone.getCodice()));
        truncateCanzone();
        truncateAlbum();
    }

    //CanzoneDAO doGet

    @Test
    public void doGetWithChiaveNull(){
        String chiave=null;
        assertThrows(IllegalArgumentException.class,()->canzoneDAO.doGet(chiave));
    }

    @Test
    public void doGetOggettoNonTrovatoException(){
        String codCanzone="C02";
        assertThrows(OggettoNonTrovatoException.class,()->canzoneDAO.doGet(codCanzone));
    }

    @Test
    public void doGetOK() throws SQLException {
        Album album=insertAlbum();
        Canzone canzone=insertCanzone();
        canzone.setAlbum(album);
        canzoneDAO.doSave(canzone);
        Canzone canzone1=canzoneDAO.doGet("C01");
        canzone1.setAlbum(album);
        //Verficare che il pojo sia uguale
        assertEquals(canzone.getCodice(),canzone1.getCodice());
        assertEquals(canzone.getTitolo(),canzone1.getTitolo());
        assertEquals(canzone.getPrezzo(),canzone1.getPrezzo());
        assertEquals(canzone.getAnno(),canzone1.getAnno());
        assertEquals(canzone.getDurata(),canzone1.getDurata());
        assertEquals(canzone.getAlbum().getCodice(),canzone1.getAlbum().getCodice());
        truncateAlbum();
        truncateCanzone();
    }

    //CanzoneDAO doSave
    @Test
    public void doSaveWithCanzoneNull(){
        Canzone can=null;
        assertThrows(IllegalArgumentException.class,()->canzoneDAO.doSave(can));
    }


    @Test
    public void doSaveWithTitoloNull(){
        Canzone can=new Canzone();
        can.setTitolo(null);
        assertThrows(IllegalArgumentException.class,()->canzoneDAO.doSave(can));
    }

    @Test
    public void doSaveWithCodiceNull(){
        Canzone can=new Canzone();
        can.setCodice(null);
        can.setTitolo("Ciao");
        assertThrows(IllegalArgumentException.class,()->canzoneDAO.doSave(can));
    }

    @Test
    public void doSaveOggettoGiaPresente() throws SQLException {
        truncateCanzone();
        truncateAlbum();
        Album album=insertAlbum();
        Canzone canzone=insertCanzone();
        canzone.setAlbum(album);
        canzoneDAO.doSave(canzone);
        assertThrows(OggettoGiaPresenteException.class,()->canzoneDAO.doSave(canzone));
        truncateCanzone();
        truncateAlbum();
    }

    @Test
    public void doSaveOK() throws SQLException {
        truncateAlbum();
        truncateCanzone();
        Album album=insertAlbum();
        Canzone canzone=insertCanzone();
        canzone.setAlbum(album);
        assertEquals(false,canzoneDAO.exist(canzone.getCodice()));
        canzoneDAO.doSave(canzone);
        assertEquals(true,canzoneDAO.exist(canzone.getCodice()));
        truncateAlbum();
        truncateCanzone();
    }


    //CanzoneDAO doDelete

    @Test
    public void doDeleteWithCodCanzoneNull(){
        String cod=null;
        assertThrows(IllegalArgumentException.class,()->canzoneDAO.doDelete(cod));
    }


    @Test
    public void doDeleteOggettoNonTrovato(){
        String cod="C01";
        assertThrows(OggettoNonTrovatoException.class,()->canzoneDAO.doDelete(cod));
    }

    @Test
    public void doDeleteOK() throws SQLException {
        truncateCanzone();
        truncateAlbum();
        Album album=insertAlbum();
        Canzone canzone=insertCanzone();
        canzone.setAlbum(album);
        canzoneDAO.doSave(canzone);
        assertEquals(true,canzoneDAO.exist(canzone.getCodice()));//Restituisce true
        canzoneDAO.doDelete(canzone.getCodice());
        assertEquals(false,canzoneDAO.exist(canzone.getCodice()));//Restituisce false poichè non ci deve più essere all'interno del db
        truncateCanzone();truncateAlbum();
    }







    //CanzoneDao doRetrieveCanzoneWithArtisti
    @Test
    public void doRetrieveCanzoneWithArtistiWithCodCanzoneNull(){
        String cod=null;
        assertThrows(IllegalArgumentException.class,()->canzoneDAO.doRetrieveCanzoneWithArtisti(cod));
    }

    @Test
    public void doRetrieveCanzoneWithArtistiOggettoNonTrovato(){
        String cod="C01";
        assertThrows(OggettoNonTrovatoException.class,()->canzoneDAO.doRetrieveCanzoneWithArtisti(cod));
    }




    private Artista insertArtista() {
        Artista art=Mockito.mock(Artista.class);
        Mockito.when(art.getCodFiscale()).thenReturn("CIAO1");
        Mockito.when(art.getNome()).thenReturn("Carlo");
        Mockito.when(art.getCognome()).thenReturn("C");
        Mockito.when(art.getNomeDArte()).thenReturn("C.L");
        Mockito.when(art.getPathImg()).thenReturn("...");
        return art;
    }

    private Album insertAlbum()throws SQLException{
        Album album=Mockito.mock(Album.class);
        Mockito.when(album.getCodice()).thenReturn("AL2");
        Mockito.when(album.getAnno()).thenReturn(2000);
        Mockito.when(album.getPrezzo()).thenReturn(1.59);
        Mockito.when(album.getNumCanzoni()).thenReturn(1);
        Mockito.when(album.getNome()).thenReturn("Ciao");
        Mockito.when(album.getDescrizione()).thenReturn("...");
        return album;
    }

    private Canzone insertCanzone() throws SQLException {
        Canzone canzone=new Canzone();
        canzone.setArtisti(new ArrayList<>());
        canzone.setAnno(2000);
        canzone.setCodice("C01");
        canzone.setDurata(2.0);
        canzone.setPrezzo(1.3);
        canzone.setTitolo("Ciao Come Va");
        canzone.setPathImg("....");
        canzone.setPathMP3("....");
        return canzone;
    }



    @After
    public void tearDown() throws SQLException {
        truncateCanzone();
        truncateAlbum();
        truncateArtista();
        truncateArtistaScrivereCanzoni();
    }

    private void truncateArtistaScrivereCanzoni() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE scrivere;");
        preparedStatement.executeUpdate();
    }

    private void truncateArtista() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE artista;");
        preparedStatement.executeUpdate();
    }

    private void truncateAlbum() throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE album;");
        preparedStatement.executeUpdate();
    }

    private void truncateCanzone() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE canzone;");
        preparedStatement.executeUpdate();
    }

    private void InsertDatiArtistaScriveCanzoni() throws SQLException {
        PreparedStatement preparedStatement= connection.prepareStatement("INSERT INTO scrivere VALUES (?,?)");
        preparedStatement.setString(1,"CIAO1");
        preparedStatement.setString(2,"C01");
        if(preparedStatement.executeUpdate()!=1){
            throw new OggettoNonInseritoException("Canzone e Artisti non inseriti");
        }
    }




}
