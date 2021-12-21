package data.DAOCanzone;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CanzoneMapper {
    public Canzone map(ResultSet rs) throws SQLException {
        Canzone canzone = new Canzone();
        canzone.setCodice(rs.getString("CAN.codice"));
        canzone.setAnno(rs.getInt("CAN.anno"));
        canzone.setDurata(rs.getDouble("CAN.durata"));
        canzone.setTitolo(rs.getString("CAN.titolo"));
        canzone.setPrezzo(rs.getDouble("CAN.prezzo"));
        canzone.setPathImg(rs.getString("CAN.pathImg"));
        canzone.setPathMP3(rs.getString("CAN.pathMP3"));
        return canzone;
    }
}
