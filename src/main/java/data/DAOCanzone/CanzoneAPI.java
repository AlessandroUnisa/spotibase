package data.DAOCanzone;

import data.utils.Dao;

import java.sql.SQLException;
import java.util.List;

public interface CanzoneAPI extends Dao<Canzone> {

    //metodi non documentati per IS
    List<Canzone> doRetrieveCanzoniUltimeUscite() throws SQLException;
    List<Canzone> doRetrivePopularSongsWithArtista() throws SQLException;
    List<Canzone> doRetrieveCanzoneRandom() throws SQLException;
    List<String> doRetrieveaCodiciCanzoniPreferite(String username) throws SQLException;
    List<Canzone> doRetrieveSongsByPlaylist(String username, String titoloPlaylist) throws SQLException;
    List<Canzone> doRetrieveSongsByAlbum(String codAlbum) throws SQLException;
    List<Canzone> doRetrieveSinglesByArtista(String nomeDArte) throws SQLException;
    List<Canzone> doRetrieveSongsByArtista(String nomeDArte) throws SQLException;
    List<Canzone> doRetrieveCanzonePopular() throws SQLException;
    List<Canzone> doRetrieveByName(String titolo) throws SQLException;
    boolean doUpdate(Canzone canzone) throws SQLException;
    List<Canzone> doRetrieveAllCanzoni() throws SQLException;
    List<Canzone> doRetrieveAllCanzoni(int offset, int numElements) throws SQLException;
    List<Canzone> doRetrieveCanzoneByGenere(String genere) throws SQLException;
    List<Canzone> doRetrieveCanzoneByGenere(String genere, String filterSQL, int offset, int numElements) throws SQLException;
    int doRetrieveNumCanzoni() throws SQLException;
    double doRetrieveTotaleGuadagno() throws SQLException;
    List<Canzone> doRetrieveCanzoniTopBuy() throws SQLException;
    Canzone doRetrieveCanzoneByCodice(String codice) throws SQLException;
    List<Canzone> doRetrieveCanzoniByCodiciWithArtisti(List<String> codici) throws SQLException;

}
