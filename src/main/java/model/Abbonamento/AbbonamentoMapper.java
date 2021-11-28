package model.Abbonamento;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AbbonamentoMapper {
    public Abbonamento map(ResultSet rs) throws SQLException {
        Abbonamento abbonamento = new Abbonamento();
        abbonamento.setNome(rs.getString("ABB.nome"));
        abbonamento.setPrezzoMensile(rs.getDouble("ABB.prezzoMensile"));
        return abbonamento;
    }
}
