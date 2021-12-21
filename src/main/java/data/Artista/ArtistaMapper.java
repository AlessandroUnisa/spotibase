package data.Artista;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ArtistaMapper {
    public Artista map(ResultSet rs) throws SQLException {
        Artista artista = new Artista();
        artista.setCodFiscale(rs.getString("ART.codFiscale"));
        artista.setNome(rs.getString("ART.nome"));
        artista.setCognome(rs.getString("ART.cognome"));
        artista.setNomeDArte(rs.getString("ART.nomeDArte"));
        artista.setPathImg(rs.getString("ART.pathImg"));
        return artista;
    }
}
