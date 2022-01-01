package data.DAOAcquisto;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AcquistoDAOTest {


    AcquistoDAO acquistoDAO;

    @Before
    public void setUp(){
      acquistoDAO = new AcquistoDAO();
    }

    @Test
    public void doGetChiaveNonValidaTest(){
        String chiave1 = null;
        String chiave2 = "plutoPaperino";



        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> acquistoDAO.doGet(chiave1)),
                () -> assertThrows(IllegalArgumentException.class, () -> acquistoDAO.doGet(chiave2))
        );
    }



    @After
    public void tearDown(){
        acquistoDAO = null;
    }

    private List<Acquisto> fillAcquisti(){
        String username = "Pluto", codCanzone = "C";
        ArrayList<Acquisto> list = new ArrayList<>();
        for(int i=0; i<20; i++)
            list.add(new Acquisto(username+i,codCanzone+i));
        return list;
    }
}
