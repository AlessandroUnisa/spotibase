package data.DAOAcquisto;

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

import static org.junit.jupiter.api.Assertions.*;

public class AcquistoDAOTest {

    AcquistoDAO acquistoDAO;
    Connection connection;

    @Before
    public void setUp(){
        acquistoDAO = new AcquistoDAO();
        connection = SingletonJDBC.getConnectionTesting();
    }

    @Test
    public void doGetChiaveNull(){
        String chiave = null;
        assertThrows(IllegalArgumentException.class, ()->acquistoDAO.doGet(chiave));
    }

    @Test
    public void doSaveOk() throws SQLException {
        Acquisto acquisto = Mockito.mock(Acquisto.class);
        String codCanzone = "C01", username = "pluto";
        Mockito.when(acquisto.getUsename()).thenReturn(username);
        Mockito.when(acquisto.getCodCanzone()).thenReturn(codCanzone);

        acquistoDAO.doSave(acquisto);

        assertEquals(true, acquistoDAO.exist(username,codCanzone));
    }

    @After
    public void tearDown() throws SQLException {
        truncateAcquisto();
    }

    private void truncateAcquisto() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("TRUNCATE TABLE uc;");
        preparedStatement.executeUpdate();
    }
}
