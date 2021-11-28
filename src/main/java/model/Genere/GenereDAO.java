package model.Genere;

import model.ConPool;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GenereDAO {

    /** Ritorna la lista dei generi presenti nel db*/
    public List<String> deRetrieveNomeGeneri(){
        try(Connection conn = ConPool.getConnection()){
            PreparedStatement ps = conn.prepareStatement("SELECT DISTINCT nome FROM genere ORDER BY nome;");
            List<String> nomi = new ArrayList<>();
            ResultSet rs = ps.executeQuery();
            while (rs.next())
                nomi.add(rs.getString(1));
            conn.close();
            return nomi;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
