package data.DAOPreferenza;

import data.Exceptions.OggettoGiaPresenteException;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class PreferenzaDAOTest {
    
    PreferenzaDAO preferenzaDAO;
    Connection connection;
    
    @Before
    public void setUp(){
        connection=SingletonJDBC.getConnectionTesting();
        preferenzaDAO=new PreferenzaDAO(connection);
    }

    //PreferenzaDAO doGet

    @Test
    public void doGetChiaveNull() throws SQLException {
        String chiave=null;
        assertThrows(IllegalArgumentException.class,()->preferenzaDAO.doGet(chiave));
    }

    @Test
    public void doGetChiaveNotContainsSemicolon(){
        String chiave="sdakhdaksd";
        assertThrows(IllegalArgumentException.class,()->preferenzaDAO.doGet(chiave));
    }

    @Test
    public void doGetOggettoNonTrovato() throws SQLException {
        String CodiceUtente="pippo",codCanzone="C03";
        String chiave=codCanzone+";"+CodiceUtente;
        assertThrows(OggettoNonTrovatoException.class,()->preferenzaDAO.doGet(chiave));
    }


    @Test
    public void doGetOK() throws SQLException {
        //Inserisco la Preferenza nel DB
        String CodiceUtente="pippo",codCanzone="C03";
        String chiave=codCanzone+";"+CodiceUtente;
        preferenzaDAO.doSave(new Preferenza(codCanzone, CodiceUtente));
        //Ora devo prelevare la Preferenza dal Database
        Preferenza preferenza=preferenzaDAO.doGet(chiave);
        assertEquals(codCanzone,preferenza.getCodCanzone());
        assertEquals(CodiceUtente,preferenza.getCodUtente());
        truncatePreferenza();
    }

    //PreferenzaDAO doSave

    @Test
    public void doSaveWithPreferenzaNull(){
        Preferenza preferenza=null;
        assertThrows(IllegalArgumentException.class,()->preferenzaDAO.doSave(preferenza));
    }


    @Test
    public void doSaveWithPreferenzaCodCanzoneNull(){
        Preferenza preferenza=new Preferenza(null,"ciao");
        assertThrows(IllegalArgumentException.class,()->preferenzaDAO.doSave(preferenza));
    }


    @Test
    public void doSaveWithPreferenzaCodUtenteNull(){
        Preferenza preferenza=new Preferenza("ciao",null);
        assertThrows(IllegalArgumentException.class,()->preferenzaDAO.doSave(preferenza));
    }

    @Test
    public void doSaveOggettoGiaPresente() throws SQLException {
        String CodiceUtente="pippo",codCanzone="C03";
        String chiave=codCanzone+";"+CodiceUtente;
        preferenzaDAO.doSave(new Preferenza(codCanzone, CodiceUtente));
        Preferenza preferenza=Mockito.mock(Preferenza.class);
        Mockito.when(preferenza.getCodCanzone()).thenReturn(codCanzone);
        Mockito.when(preferenza.getCodUtente()).thenReturn(CodiceUtente);
        assertThrows(OggettoGiaPresenteException.class,()->preferenzaDAO.doSave(preferenza));
        truncatePreferenza();
    }

    @Test
    public void doSaveOK() throws SQLException {
        String CodiceUtente="pippo",codCanzone="C03";
        Preferenza preferenza=Mockito.mock(Preferenza.class);
        Mockito.when(preferenza.getCodCanzone()).thenReturn(codCanzone);
        Mockito.when(preferenza.getCodUtente()).thenReturn(CodiceUtente);
        preferenzaDAO.doSave(preferenza);
        assertEquals(true,preferenzaDAO.exist(preferenza.getCodCanzone(),preferenza.getCodUtente()));
        truncatePreferenza();
    }

    //PreferenzaDAO doDelete
    @Test
    public void doDeleteWithChiaveNull(){
        String key=null;
        assertThrows(IllegalArgumentException.class,()->preferenzaDAO.doDelete(key));
    }

    @Test
    public void doDeleteWithChiaveNotContainsSemicolon(){
        String key="ksdfhskdfns";
        assertThrows(IllegalArgumentException.class,()->preferenzaDAO.doDelete(key));
    }


    @Test
    public void doDeleteOggettoNonTrovato(){
        String key="askdha;asdhaks";
        assertThrows(OggettoNonTrovatoException.class,()->preferenzaDAO.doDelete(key));
    }


    @Test
    public void doDeleteOK() throws SQLException {
        String CodiceUtente="pippo",codCanzone="C03";
        String key=codCanzone+";"+CodiceUtente;
        preferenzaDAO.doSave(new Preferenza(codCanzone,CodiceUtente));//La salvo per poter verificare e cancellare
        preferenzaDAO.doDelete(key);//Elimino la preferenza
        assertEquals(false,preferenzaDAO.exist(codCanzone,CodiceUtente));
        truncatePreferenza();
    }

    //PrefrenzaDAO doRetrieveCodiciCanzoniPreferite


    @Test
    public void doRetrieveCodiciCanzoniPreferiteWithUsernameNull(){
        String key=null;
        assertThrows(IllegalArgumentException.class,()->preferenzaDAO.doRetrieveCodiciCanzoniPreferite(key));
    }

    @Test
    public void doRetrieveCodiciCanzoniPreferiteOK() throws SQLException {
        //Inserisco le preferenze all'interno del DB
        String Username="Umby";
        ArrayList<Preferenza>ListOfPreferenze=insertPreferenzeInDB();//Ritorno gli elementi salvati per confrontarli
        List<String> ListRestituita= preferenzaDAO.doRetrieveCodiciCanzoniPreferite(Username);
        assertEquals(ListOfPreferenze.size(),ListRestituita.size());
        for(int i=0; i<ListRestituita.size(); i++){
            assertEquals(true,ListRestituita.contains(ListOfPreferenze.get(i).getCodCanzone()));
        }
        truncatePreferenza();
    }

    private ArrayList<Preferenza> insertPreferenzeInDB() throws SQLException {
        ArrayList<Preferenza>ListOfPreferenza=new ArrayList<>();
        String []CodCanzone={"C01","C02","C03"};
        String []CodUtente={"Umby"};
            Preferenza preferenza=Mockito.mock(Preferenza.class);
            Mockito.when(preferenza.getCodCanzone()).thenReturn(CodCanzone[1]);
            Mockito.when(preferenza.getCodUtente()).thenReturn(CodUtente[0]);
            preferenzaDAO.doSave(preferenza);
            ListOfPreferenza.add(preferenza);
        return ListOfPreferenza;
    }

    //Exist
    @Test
    public void existWithCodCanzoneNull(){
        String codCanzone=null,codUser=null;
        assertThrows(IllegalArgumentException.class,()->preferenzaDAO.exist(codCanzone,codUser));
    }

    @Test
    public void existWithCodUserNull(){
        String codCanzone="...",codUser=null;
        assertThrows(IllegalArgumentException.class,()->preferenzaDAO.exist(codCanzone,codUser));
    }

    @Test
    public void existOK() throws SQLException {
        String CodiceUtente="pippo",codCanzone="C03";
        Preferenza preferenza=Mockito.mock(Preferenza.class);
        Mockito.when(preferenza.getCodCanzone()).thenReturn(codCanzone);
        Mockito.when(preferenza.getCodUtente()).thenReturn(CodiceUtente);
        preferenzaDAO.doSave(preferenza);
        assertEquals(true,preferenzaDAO.exist(codCanzone,CodiceUtente));
        truncatePreferenza();
    }



    @After
    public void tearDown() throws SQLException {
        truncatePreferenza();
    }

    private void truncatePreferenza() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE preferenza;");
        preparedStatement.executeUpdate();
    }


}
