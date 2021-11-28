package model.Album;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AlbumMapper {
    public Album map(ResultSet rs) throws SQLException {
        Album album = new Album();
        album.setCodice(rs.getString("ALB.codice"));
        album.setNome(rs.getString("ALB.nome"));
        album.setAnno(rs.getInt("ALB.anno"));
        album.setDescrizione(rs.getString("ALB.descrizione"));
        album.setNumCanzoni(rs.getInt("ALB.numCanzoni"));
        album.setPathImg(rs.getString("ALB.pathImg"));
        album.setPathZip(rs.getString("ALB.pathZip"));
        album.setPrezzo(rs.getDouble("ALB.prezzo"));
        return album;
    }
}
