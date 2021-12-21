package data.Genere;

import data.utils.SingletonJDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenereDAO {
    //metodi documentati per IS--------------------------------------------------------------------------------------

    //metodi NON documentati per IS--------------------------------------------------------------------------------------
    /** Ritorna la lista dei generi presenti nel db*/
    public List<String> deRetrieveNomeGeneri() throws SQLException {
        PreparedStatement ps = SingletonJDBC.getConnection().prepareStatement("SELECT DISTINCT nome FROM genere ORDER BY nome;");
        List<String> nomi = new ArrayList<>();
        ResultSet rs = ps.executeQuery();
        while (rs.next())
            nomi.add(rs.getString(1));

        return nomi;
    }
}
