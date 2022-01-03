package data.DAOAcquisto;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
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
        assertThrows(OggettoNonTrovatoException.class,()->acquistoDAO.exist(acquistoDAO.doGet(chiave).getUsename(),acquistoDAO.doGet(chiave).getCodCanzone()));
    }


    @Test
    public void doGetOK() throws SQLException {
        String chiave="pluto;S03";
        String []chiavi=chiave.split(";");
        assertEquals(true,acquistoDAO.exist(chiavi[0],chiavi[1]));
        assertEquals(chiavi[0],acquistoDAO.doGet(chiave).getUsename());
        assertEquals(chiavi[1],acquistoDAO.doGet(chiave).getCodCanzone());
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
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.doSave(acquisto));
    }

    @Test
    public void doSaveWithGetCodCanzoneNull(){
        Acquisto acquisto=Mockito.mock(Acquisto.class);
        Mockito.when(acquisto.getCodCanzone()).thenReturn(null);
        assertThrows(IllegalArgumentException.class,()->acquistoDAO.doSave(acquisto));
    }


    @Test
    public void doSaveWithOggettoGiaPresente(){
        Acquisto acquisto=Mockito.mock(Acquisto.class);
        Mockito.when(acquisto.getUsename()).thenReturn("pluto");
        Mockito.when(acquisto.getCodCanzone()).thenReturn("S03");
        assertThrows(OggettoGiaPresenteException.class,()->acquistoDAO.doSave(acquisto));
    }




    @Test
    public void doSaveOk() throws SQLException {
        Acquisto acquisto = Mockito.mock(Acquisto.class);
        String codCanzone = "S03", username = "pluto";
        Mockito.when(acquisto.getUsename()).thenReturn(username);
        Mockito.when(acquisto.getCodCanzone()).thenReturn(codCanzone);
        acquistoDAO.doSave(acquisto);
        assertEquals(true, acquistoDAO.exist(username,codCanzone));
        tearDown();
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
    public void doInsertCanzoneAcquistataOggettoGiaPresente(){
        String username="pluto",codice="S03";
        assertThrows(OggettoGiaPresenteException.class,()->acquistoDAO.doInsertCanzoneAcquistata(username,codice));
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
        //Prevedo una lista statica per poter verificare i suoi contenuti al suo interno e lo faccio per lo username pluto
        String username="pluto";
        List<String>codici=new ArrayList<>();
        codici.add("S03");
        codici.add("S06");
        //Lista contenente questi dati
        List<String>codiciRetrieve=acquistoDAO.doRetrieveCodiciCanzoniAcquistate(username);
        System.out.println(codici.toString());
        System.out.println(codiciRetrieve.toString());
        assertEquals(codici.size(),codiciRetrieve.size());//Size uguale
        int size=codici.size();
        for(int i=0; i<size; i++)
        assertEquals(codici.get(i),codiciRetrieve.get(i));
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
        assertEquals(true,acquistoDAO.exist(username,cod));
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