package model.Genere;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenereMapper {
    public Genere map(ResultSet rs) throws SQLException {
        Genere genere = new Genere();
        genere.setCodArtista(rs.getString("GEN.codFiscale"));
        genere.setNome(rs.getString("GEN.nome"));
        return genere;
    }
}
