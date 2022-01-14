package data.DAOPlaylist;

import data.DAOCanzone.CanzoneAPI;
import data.DAOUtente.UtenteAPI;
import data.utils.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**Questa classe rappresenta l'interfaccia utlizzata dalla playlist e implementata per rispettare il facade pattern
 *
 */
public interface PlaylistAPI extends Dao<Playlist> {
    List<Playlist> doRetrievePlaylistByUtente(String username, UtenteAPI utenteAPI) throws SQLException;
    void doInsertSong(String username, String titoloPlay, String codCanzone, UtenteAPI utenteAPI, CanzoneAPI canzoneAPI,
                      PlaylistAPI playlistAPI) throws SQLException;
    boolean isPresent(String codiceCanzone, String titolo, String username,CanzoneAPI canzoneAPI,
                        PlaylistAPI playlistAPI, UtenteAPI utenteAPI) throws SQLException;
    boolean isPresent(String titolo, String username, UtenteAPI utenteAPI) throws SQLException;
    int doRetrieveNumPlaylistOfUtente(String username, UtenteAPI utenteAPI) throws SQLException;
    boolean isValidTitolo(String titolo) throws SQLException;
    boolean isValidNota(String nota) throws SQLException;

    //metodi non documentati per IS
    List<Playlist> doRetrieveAllPlaylist() throws SQLException;
    ArrayList<Playlist> doRetrieveByTitolo(String titolo) throws SQLException;
    boolean doRemoveSong(String titoloPlaylist, String username, String codice) throws SQLException;
    void doUpdate(Playlist playlist) throws SQLException;
    Playlist doRetrievePlaylistWithSongs(String username, String nome) throws SQLException;

}
