package model.Artista;

import model.Album.Album;
import model.Canzone.Canzone;
import model.Genere.Genere;

import java.util.List;

public class Artista {
	private String codFiscale;
	private String nome;
	private String cognome;
	private String nomeDArte;
	private String pathImg;

	private List<Album> album;
	private List<Canzone> canzoni;
	private List<Genere> generi;

	public List<Genere> getGeneri() {
		return generi;
	}

	public void setGeneri(List<Genere> generi) {
		this.generi = generi;
	}

	public List<Canzone> getCanzoni() {
		return canzoni;
	}

	public void setCanzoni(List<Canzone> canzoni) {
		this.canzoni = canzoni;
	}

	public List<Album> getAlbum() {
		return album;
	}

	public void setAlbum(List<Album> album) {
		this.album = album;
	}

	public String getPathImg() {
		return pathImg;
	}

	public void setPathImg(String pathImg) {
		this.pathImg = pathImg;
	}

	public String getCodFiscale() {
		return codFiscale;
	}
	public void setCodFiscale(String codFiscale) {
		this.codFiscale = codFiscale;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getNomeDArte() {
		return nomeDArte;
	}
	public void setNomeDArte(String nomeDArte) {
		this.nomeDArte = nomeDArte;
	}

	@Override
	public String toString() {
		return "Artista{" +
				"codFiscale='" + codFiscale + '\'' +
				", nome='" + nome + '\'' +
				", cognome='" + cognome + '\'' +
				", nomeDArte='" + nomeDArte + '\'' +
				", pathImg='" + pathImg + '\'' +
				", album=" + album +
				", canzoni=" + canzoni +
				", generi=" + generi +
				'}';
	}
}
