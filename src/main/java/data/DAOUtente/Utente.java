package data.DAOUtente;

import data.Album.Album;
import data.Artista.Artista;
import data.DAOCanzone.Canzone;
import data.Attivazione.Attivazione;
import data.DAOPlaylist.Playlist;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.util.List;

public class Utente {
	private String username;
	private String password;
	private String email;
	private int numPlaylistCreate;
	private List<Attivazione> attivazioni;
	private List<Artista> artisti;
	private List<Playlist> playlists;
	private List<Canzone> canzoni;
	private List<Album> albums;

	private List<Album> albumAcquistati;
	private List<Canzone> canzoniAcquistate;

	@Override
	@Generated
	public String toString() {
		return "Utente{" +
				"username='" + username + '\'' +
				", password='" + password + '\'' +
				", email='" + email + '\'' +
				", numPlaylistCreate=" + numPlaylistCreate +
				", attivazioni=" + attivazioni +
				", artisti=" + artisti +
				", playlists=" + playlists +
				", canzoni=" + canzoni +
				", albums=" + albums +
				", albumAcquistati=" + albumAcquistati +
				", canzoniAcquistate=" + canzoniAcquistate +
				'}';
	}

	public List<Album> getAlbumAcquistati() {
		return albumAcquistati;
	}

	public void setAlbumAcquistati(List<Album> albumAcquistati) {
		this.albumAcquistati = albumAcquistati;
	}

	public List<Canzone> getCanzoniAcquistate() {
		return canzoniAcquistate;
	}

	public void setCanzoniAcquistate(List<Canzone> canzoniAcquistate) {
		this.canzoniAcquistate = canzoniAcquistate;
	}

	public List<Artista> getArtisti() {
		return artisti;
	}

	public void setArtisti(List<Artista> artisti) {
		this.artisti = artisti;
	}

	public List<Playlist> getPlaylists() {
		return playlists;
	}

	public void setPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}

	public List<Canzone> getCanzoni() {
		return canzoni;
	}

	public void setCanzoni(List<Canzone> canzoni) {
		this.canzoni = canzoni;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}



	public List<Attivazione> getAttivazioni() {
		return attivazioni;
	}

	public void setAttivazioni(List<Attivazione> attivazioni) {
		this.attivazioni = attivazioni;
	}



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-512");
		byte[] hashedPwd = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		StringBuilder builder = new StringBuilder();
		for (byte bit:hashedPwd)
			builder.append(String.format("%02x",bit));
		this.password = builder.toString();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getNumPlaylistCreate() {
		return numPlaylistCreate;
	}

	public void setNumPlaylistCreate(int numPlaylistCreate) {
		this.numPlaylistCreate = numPlaylistCreate;
	}
	
	
}
