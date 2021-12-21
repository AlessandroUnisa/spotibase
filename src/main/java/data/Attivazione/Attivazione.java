package data.Attivazione;
import data.DAOUtente.Utente;
import data.Abbonamento.Abbonamento;

import java.time.LocalDate;

public class Attivazione {	
	private LocalDate dataInizio;
	private LocalDate dataFine;
	private Abbonamento abbonamento;
	private Utente utente;

	@Override
	public String toString() {
		return "Attivazione{" +
				"dataInizio=" + dataInizio +
				", dataFine=" + dataFine +
				", abbonamento=" + abbonamento +
				", utente=" + utente +
				'}';
	}

	public Abbonamento getAbbonamento() {
		return abbonamento;

	}

	public void setAbbonamento(Abbonamento abb) {
		this.abbonamento = abb;
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}


	public LocalDate getDataInizio() {
		return dataInizio;
	}

	public void setDataInizio(LocalDate dataInizio) {
		this.dataInizio = dataInizio;
	}

	public LocalDate getDataFine() {
		return dataFine;
	}

	public void setDataFine(LocalDate dataFine) {
		this.dataFine = dataFine;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Attivazione that = (Attivazione) o;
		return this.dataInizio.equals(that.dataInizio)
				&& this.dataFine.equals(that.dataFine)
				&& this.abbonamento.getNome().equals(that.abbonamento.getNome())
				&& this.utente.getUsername().equals(that.utente.getUsername());
	}

}
