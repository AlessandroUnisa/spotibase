package model.Playlist;
import model.Canzone.Canzone;
import model.Utente.Utente;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Playlist {
	private String titolo;
	private String note;
	private double durata;
	private LocalDate dataCreazione;
	private String username;
	private Utente utente;
	private List<Canzone> canzoni;

	@Override
	public String toString() {
		return "Playlist{" +
				"titolo='" + titolo + '\'' +
				", note='" + note + '\'' +
				", durata=" + durata +
				", dataCrezione=" + dataCreazione +
				", username='" + username + '\'' +
				", utente=" + utente +
				", canzoni=" + canzoni +
				'}';
	}

	public Utente getUtente() {
		return utente;
	}

	public void setUtente(Utente utente) {
		this.utente = utente;
	}

	public List<Canzone> getCanzoni() {
		return canzoni;
	}

	public void setCanzoni(List<Canzone> canzoni) {
		this.canzoni = canzoni;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}

	public String getTitolo() {
		return titolo;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public double getDurata() {
		return durata;
	}
	public void setDurata(double durata) {
		this.durata = durata;
	}
	public LocalDate getDataCreazione() {
		return dataCreazione;
	}
	public void setDataCreazione(LocalDate dataCrezione) {
		this.dataCreazione = dataCrezione;
	}
	
}
