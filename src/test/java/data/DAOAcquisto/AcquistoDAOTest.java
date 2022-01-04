package data.DAOAcquisto;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import data.DAOCanzone.Canzone;
import data.Exceptions.OggettoGiaPresenteException;
import data.Exceptions.OggettoNonInseritoException;
import data.Exceptions.OggettoNonTrovatoException;
import data.utils.SingletonJDBC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert.*;
import org.springframework.test.AssertThrows;

import static org.junit.jupiter.api.Assertions.*;

public class AcquistoDAOTest {

    AcquistoDAO acquistoDAO;
    Connection connection;



    @Before
    public void setUp(){
        connection=SingletonJDBC.getConnectionTesting();
        acquistoDAO = new AcquistoDAO(connection);
    }


    /*<!-- DoGet Acquisto DAO -->*/

    @Test
    public void doGetChiaveNull(){
        String chiave = null;
        assertThrows(IllegalArgumentException.class, ()->acquistoDAO.doGet(chiave));
    }

    @Test
    public void doGetChiaveWithoutSemiColon(){
        String chiave="ksdadasdao";
        assertThrows(IllegalArgumentException.class, ()->acquistoDAO.doGet(chiave));
    }

    @Test
    public void doGetOggettoNonTrovatoException() throws SQLException {
        String chiave="pluto;S03";
        assertThrows(OggettoNonTrovatoException.class,()->acquistoDAO.doGet(chiave));
    }


    @Test
    public void doGetOK() throws SQLException {
        truncateAcquisto();
        String chiave="pluto;S03";
        String []chiavi=chiave.split(";");
        Acquisto acquisto=Mockito.mock(Acquisto.class);
        Mockito.when(acquisto.getUsename()).thenReturn("pluto");
        Mockito.when(acquisto.getCodCanzone()).thenReturn("S03");
        acquistoDAO.doSave(acquisto);
        assertEquals(true,acquistoDAO.exist(chiavi[0],chiavi[1]));
        assertEquals(chiavi[0],acquistoDAO.doGet(chiave).getUsename());
        assertEquals(chiavi[1],acquistoDAO.doGet(chiave).getCodCanzone());
        truncateAcquisto();
    }

    /*<!-- DoGet AcquistoDAO Fine -->*/




    /*<!-- DoSave AcquistoDAO -->*/

    @Test
    public void doSaveWithNull(){
        Acquisto acquisto=null;
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.doSave(acquisto));
    }

    @Test
    public void doSaveWithGetUsernameNull(){
        Acquisto acquisto=Mockito.mock(Acquisto.class);
        Mockito.when(acquisto.getUsename()).thenReturn(null);
        Mockito.when(acquisto.getCodCanzone()).thenReturn("Ciao");
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.doSave(acquisto));
    }

    @Test
    public void doSaveWithGetCodCanzoneNull(){
        Acquisto acquisto=Mockito.mock(Acquisto.class);
        Mockito.when(acquisto.getCodCanzone()).thenReturn(null);
        Mockito.when(acquisto.getUsename()).thenReturn("Ciao");
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.doSave(acquisto));
    }


    @Test
    public void doSaveWithOggettoGiaPresente() throws SQLException {
        truncateAcquisto();
        Acquisto acquisto=Mockito.mock(Acquisto.class);
        Mockito.when(acquisto.getUsename()).thenReturn("pluto");
        Mockito.when(acquisto.getCodCanzone()).thenReturn("S03");
        acquistoDAO.doSave(acquisto);
        assertThrows(OggettoGiaPresenteException.class,()->acquistoDAO.doSave(acquisto));
        truncateAcquisto();
    }




    @Test
    public void doSaveOk() throws SQLException {
        Acquisto acquisto = Mockito.mock(Acquisto.class);
        String codCanzone = "S03", username = "pluto";
        Mockito.when(acquisto.getUsename()).thenReturn(username);
        Mockito.when(acquisto.getCodCanzone()).thenReturn(codCanzone);
        acquistoDAO.doSave(acquisto);
        assertEquals(true, acquistoDAO.exist(username,codCanzone));
        truncateAcquisto();
    }

    /*<!-- DoSave AcquistoDAO FINE -->*/



    /*<!-- DoDelete AcquistoDAO -->*/
    @Test
    public void doDeleteWithChiaveNull(){
        String chiave= null;
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.doDelete(chiave));
    }

    @Test
    public void doDeleteWithChiaveNotContainsSemiColon(){
        String chiave= "asjdfsdfbhbdfsvd";
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.doDelete(chiave));
    }

    @Test
    public void doDeleteWithChiaveOggettoNonTrovato(){
        String username="...",codCanzone="....";
        String chiave=username+";"+codCanzone;
        assertThrows(OggettoNonTrovatoException.class,()->acquistoDAO.doDelete(chiave));
    }


    @Test
    public void doDeleteOk() throws SQLException {
        String username="pluto",codCanzone="S03",semicolon=";";
        String chiave=username+semicolon+codCanzone;
        Acquisto acquisto=Mockito.mock(Acquisto.class);
        Mockito.when(acquisto.getCodCanzone()).thenReturn(codCanzone);
        Mockito.when(acquisto.getUsename()).thenReturn(username);
        acquistoDAO.doSave(acquisto);
        assertEquals(true,acquistoDAO.exist(username,codCanzone));
        acquistoDAO.doDelete(chiave);
        assertEquals(false,acquistoDAO.exist(username,codCanzone));
    }

    /*<!-- DoDelete AcquistoDAO fine -->*/


    /*<!-- doInsertCanzoneAcquistata AcquistoDAO -->*/
    @Test
    public void doInsertCanzoneAcquistataWithUsernameNull(){
        String username= null;
        String codice="S02";
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.doInsertCanzoneAcquistata(username,codice));
    }


    @Test
    public void doInsertCanzoneAcquistataWithCodiceNull(){
        String username= "...";
        String codice=null;
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.doInsertCanzoneAcquistata(username,codice));
    }


    @Test
    public void doInsertCanzoneAcquistataOggettoGiaPresente() throws SQLException {
        String username="pluto",codice="S03";
        Acquisto acquisto=Mockito.mock(Acquisto.class);
        Mockito.when(acquisto.getCodCanzone()).thenReturn(codice);
        Mockito.when(acquisto.getUsename()).thenReturn(username);
        acquistoDAO.doSave(acquisto);
        assertThrows(OggettoGiaPresenteException.class,()->acquistoDAO.doInsertCanzoneAcquistata(username,codice));
        truncateAcquisto();
    }

    @Test
    public void doInsertCanzoneAcquistataOK() throws SQLException {
        truncateAcquisto();
        String username="pluto",codice="S06";
        assertEquals(false,acquistoDAO.exist(username,codice));
        acquistoDAO.doInsertCanzoneAcquistata(username,codice);
        assertEquals(true,acquistoDAO.exist(username,codice));
    }
    /*<!-- doInsertCanzoneAcquistata AcquistoDAO FINE-->*/

    /*<!-- doRetrieveCodiciCanzoniAcquistate AcquistoDAO -->*/
    @Test
    public void doRetrieveCodiciCanzoniAcquistateWithUsernameNull(){
        String username=null;
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.doRetrieveCodiciCanzoniAcquistate(username));
    }


    @Test
    public void doRetrieveCodiciCanzoniAcquistateOK() throws SQLException {
        //Inserisco le canzoni all'interno del Db
        truncateAcquisto();
        String username="pippo";
        List<Acquisto>ListaAcquisti=insertAcquisto();
        List<String>ListAcquisto=acquistoDAO.doRetrieveCodiciCanzoniAcquistate(username);
        assertEquals(ListaAcquisti.size(),ListAcquisto.size());
        for(int i=0; i<ListAcquisto.size(); i++){
            assertEquals(ListaAcquisti.get(i).getCodCanzone(),ListAcquisto.get(i));
        }
        truncateAcquisto();
    }



    private List<Acquisto> insertAcquisto() throws SQLException {
        String []CodCanzone={"C01","C02","C03"};
        ArrayList<Acquisto>List=new ArrayList<>();
        String username="pippo";
        for(int i=0; i<CodCanzone.length; i++){
            Acquisto acquisto=Mockito.mock(Acquisto.class);
            Mockito.when(acquisto.getCodCanzone()).thenReturn(CodCanzone[i]);
            Mockito.when(acquisto.getUsename()).thenReturn(username);
            acquistoDAO.doSave(acquisto);
            List.add(acquisto);
        }
        return List;
    }
    /*<!-- doRetrieveCodiciCanzoniAcquistate acquistoDAO FINE -->*/


    /*<!-- exist acquistoDAO -->*/
    @Test
    public void existWithUsernameNull(){
        String username=null;
        String cod=".";
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.exist(username,cod));
    }

    @Test
    public void existWithcodCanzoneNull(){
        String username="..";
        String cod=null;
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.exist(username,cod));
    }


    @Test
    public void existOK() throws SQLException {
        String username="pluto",cod="S03";
        Acquisto acquisto=Mockito.mock(Acquisto.class);
        Mockito.when(acquisto.getUsename()).thenReturn(username);
        Mockito.when(acquisto.getCodCanzone()).thenReturn(cod);
        acquistoDAO.doSave(acquisto);
        assertEquals(true,acquistoDAO.exist(username,cod));
        truncateAcquisto();
    }

    /*<!-- exist acquistoDAO FINE -->*/

    @After
    public void tearDown() throws SQLException {
        truncateAcquisto();
    }

    private void truncateAcquisto() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE uc;");
        preparedStatement.executeUpdate();
    }
}