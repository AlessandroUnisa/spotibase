package data.Attivazione;

import data.DAOUtente.Utente;
import data.Abbonamento.Abbonamento;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AttivazioneMapper {
    public Attivazione map(ResultSet rs) throws SQLException {
        Attivazione attivazione = new Attivazione();
        attivazione.setDataInizio(rs.getDate("ATT.dataInizio").toLocalDate());
        attivazione.setDataFine(rs.getDate("ATT.dataFine").toLocalDate());
        Abbonamento abbonamento = new Abbonamento();
        abbonamento.setNome(rs.getString("ATT.nomeAbbonamento"));
        attivazione.setAbbonamento(abbonamento);
        Utente utente = new Utente();
        utente.setUsername(rs.getString("ATT.usernameUtente"));
        attivazione.setUtente(utente);
        return attivazione;
    }
}
