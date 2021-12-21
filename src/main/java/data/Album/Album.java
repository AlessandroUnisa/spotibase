package data.Album;

import data.Artista.Artista;
import data.DAOCanzone.Canzone;

import java.util.List;

public class Album {
	private String codice;
	private String nome;
	private int anno;
	private double prezzo;
	private String descrizione;
	private int numCanzoni;
	private String pathImg, pathZip;

	private List<Canzone> canzoni;
	private List<Artista> artisti;

	@Override
	public String toString() {
		return "Album{" +
				"codice='" + codice + '\'' +
				", nome='" + nome + '\'' +
				", anno=" + anno +
				", prezzo=" + prezzo +
				", descrizione='" + descrizione + '\'' +
				", numCanzoni=" + numCanzoni +
				", pathImg='" + pathImg + '\'' +
				", pathZip='" + pathZip + '\'' +
				", canzoni=" + canzoni +
				", artisti=" + artisti +
				'}';
	}

	public List<Canzone> getCanzoni() {
		return canzoni;
	}

	public void setCanzoni(List<Canzone> canzoni) {
		this.canzoni = canzoni;
	}

	public List<Artista> getArtisti() {
		return artisti;
	}

	public void setArtisti(List<Artista> artisti) {
		this.artisti = artisti;
	}

	public String getPathImg() {
		return pathImg;
	}

	public void setPathImg(String pathImg) {
		this.pathImg = pathImg;
	}

	public String getPathZip() {
		return pathZip;
	}

	public void setPathZip(String pathZip) {
		this.pathZip = pathZip;
	}


	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getAnno() {
		return anno;
	}

	public void setAnno(int anno) {
		this.anno = anno;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public int getNumCanzoni() {
		return numCanzoni;
	}

	public void setNumCanzoni(int numCanzoni) {
		this.numCanzoni = numCanzoni;
	}

}
