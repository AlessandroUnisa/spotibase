package data.DAOPlaylist;

import data.utils.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface PlaylistAPI extends Dao<Playlist> {
    List<Playlist> doRetrievePlaylistByUtente(String username) throws SQLException;
    boolean doInsertSong(String username, String titoloPlay, String codCanzone) throws SQLException;
    boolean isPresent(String codiceCanzone, String titolo, String username) throws SQLException;
    boolean isPresent(String titolo, String username) throws SQLException;
    boolean doInsertPlaylist(Playlist playlist) throws SQLException;
    int doRetrieveNumPlaylistOfUtente(String username) throws SQLException;

    //metodi non documentati per IS
    List<Playlist> doRetrieveAllPlaylist() throws SQLException;
    ArrayList<Playlist> doRetrieveByTitolo(String titolo) throws SQLException;
    boolean doRemoveSong(String titoloPlaylist, String username, String codice) throws SQLException;
    Playlist doRetrievePlaylistWithSongs(String username, String nome) throws SQLException;
    void doUpdate(Playlist playlist) throws SQLException;
}
