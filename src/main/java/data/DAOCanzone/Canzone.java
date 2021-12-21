package data.DAOCanzone;

import data.Album.Album;
import data.Artista.Artista;

import java.util.List;

public class Canzone {
	private String codice;
	private String titolo;
	private double durata;
	private int anno;
	private double prezzo;
	private String pathImg, pathMP3;

	private Album album;
	private List<Artista> artisti;

	public Album getAlbum() { return album; }

	public void setAlbum(Album album) { this.album = album; }
	public List<Artista> getArtisti() {return artisti;}

	public void setArtisti(List<Artista> artisti) {this.artisti = artisti;}

	public String getPathImg() {
		return pathImg;
	}

	public void setPathImg(String pathImg) {
		this.pathImg = pathImg;
	}

	public String getPathMP3() {
		return pathMP3;
	}

	public void setPathMP3(String pathMP3) {
		this.pathMP3 = pathMP3;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public String getTitolo() {
		return titolo;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public double getDurata() {
		return durata;
	}

	public void setDurata(double durata) {
		this.durata = durata;
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

	@Override
	public String toString() {
		return "Canzone{" +
				"codice='" + codice + '\'' +
				", titolo='" + titolo + '\'' +
				", durata=" + durata +
				", anno=" + anno +
				", prezzo=" + prezzo +
				", pathImg='" + pathImg + '\'' +
				", pathMP3='" + pathMP3 + '\'' +
				", album=" + album +
				", artisti=" + artisti +
				'}';
	}


}
