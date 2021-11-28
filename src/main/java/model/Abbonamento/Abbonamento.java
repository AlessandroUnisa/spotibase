package model.Abbonamento;

import model.Attivazione.Attivazione;

import java.util.List;

public class Abbonamento {
	private String nome;
	private double prezzoMensile;
	private List<Attivazione> attivazioni;

	public List<Attivazione> getAttivazioni() {

		return attivazioni;
	}

	public void setAttivazioni(List<Attivazione> attivazioni) {
		this.attivazioni = attivazioni;
	}

	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public double getPrezzoMensile() {
		return prezzoMensile;
	}
	public void setPrezzoMensile(double prezzoMensile) {
		this.prezzoMensile = prezzoMensile;
	}

	@Override
	public String toString() {
		return "Abbonamento{" +
				"nome='" + nome + '\'' +
				", prezzoMensile=" + prezzoMensile +
				", attivazioni=" + attivazioni +
				'}';
	}
}
