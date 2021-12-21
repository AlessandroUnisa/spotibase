package data.DAOPlaylist;

import data.DAOCanzone.CanzoneMapper;
import data.utils.Dao;
import data.utils.SingletonJDBC;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO implements PlaylistAPI {
    //metodi documentati per IS-----------------------------------------------------------------------------------------

    @Override
    /**
     * @param chiave concatenazione del titolo della plyalist con la username. Esempio: "playRock;pluto"
     * */
    public Playlist doGet(String chiave) throws SQLException {
        String[] chiavi = chiave.split(";");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM playlist WHERE titolo=? && username=?");
        preparedStatement.setString(1,chiavi[0]);
        preparedStatement.setString(2,chiavi[1]);
        PlaylistMapper mapper = new PlaylistMapper();
        return mapper.map(preparedStatement.executeQuery());
    }

    /** Salva nel DB la playlist nel db */
    public boolean doSave(Playlist playlist) throws SQLException {
        PreparedStatement ps = SingletonJDBC.getConnection().prepareStatement(PlaylistQuery.getQueryPlaylistSave());
        ps.setString(1, playlist.getTitolo());
        ps.setString(2,playlist.getUsername());
        ps.setString(3, playlist.getNote());
        ps.setDouble(4, playlist.getDurata());
        ps.setDate(5, Date.valueOf(playlist.getDataCreazione()));
        return ps.executeUpdate()==1;
    }
    /** Elimina la playlist dal DB
     * @param chiave concatenazione del titolo della plyalist con la username. Esempio: "playRock;pluto"
     * */
    public boolean doDelete(String chiave) throws SQLException {
        String[] chiavi = chiave.split(";");
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("DELETE FROM playlist WHERE titolo=? AND username=?");
        st.setString(1,chiavi[0]);
        st.setString(2,chiavi[1]);
        return st.executeUpdate()==1;
    }



    /**Ritorna le playlist dell utente*/
    public List<Playlist> doRetrievePlaylistByUtente(String username) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(PlaylistQuery.getQueryDoRetrievePlaylistByUtente());
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<Playlist> list = new ArrayList<>();
        PlaylistMapper mapper = new PlaylistMapper();
        while (resultSet.next())
            list.add(mapper.map(resultSet));
        return list;
    }

    /**Inserisce una canzone nella playlist*/
    public boolean doInsertSong(String username, String titoloPlay, String codCanzone) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(PlaylistQuery.getQueryDoInsertSong());
        preparedStatement.setString(1,codCanzone);
        preparedStatement.setString(2,titoloPlay);
        preparedStatement.setString(3,username);
        return preparedStatement.executeUpdate()==1;
    }

    /**Controlla se la canzone è presente nella playlist*/
    public boolean isPresent(String codiceCanzone, String titolo, String username) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(PlaylistQuery.getQueryIsPresent());
        preparedStatement.setString(1,codiceCanzone);
        preparedStatement.setString(2, titolo);
        preparedStatement.setString(3, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    /**Controlla se la playlist è presente nel db*/
    public boolean isPresent(String titolo, String username) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(PlaylistQuery.getQueryIsPresentPlaylist());
        preparedStatement.setString(1, titolo);
        preparedStatement.setString(2, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    /**Inserisce una nuova playlist nel DB*/
    public boolean doInsertPlaylist(Playlist playlist) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(PlaylistQuery.getQueryDoInsertPlaylist());
        preparedStatement.setString(1,playlist.getTitolo());
        preparedStatement.setString(2,playlist.getUsername());
        preparedStatement.setString(3,playlist.getNote());
        preparedStatement.setDouble(4,0);
        preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
        return preparedStatement.executeUpdate()==1;
    }

    public int doRetrieveNumPlaylistOfUtente(String username) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(PlaylistQuery.getQuerydoRetrieveNumPlaylistOfUtente());
        preparedStatement.setString(1,username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next())
           return resultSet.getInt("num");
        else  throw new RuntimeException();
    }

    //metodi non documentati per IS-----------------------------------------------------------------------------------------
    /** Ritorna tutti le playlist*/
    public List<Playlist> doRetrieveAllPlaylist() throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM playlist;");
        ResultSet rs = st.executeQuery();
        ArrayList<Playlist> lista = new ArrayList<>();
        while(rs.next()){
            lista.add(new PlaylistMapper().map(rs));
        }
        return lista;
    }

    /** Ritorna la lista di playlist che hanno quel titolo*/
    public ArrayList<Playlist> doRetrieveByTitolo(String titolo) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM playlist WHERE titolo LIKE ?");
        String titoloLike = "%"+titolo+"%";
        st.setString(1,titoloLike);
        System.out.println(st.toString());
        ResultSet rs = st.executeQuery();
        ArrayList<Playlist> lista = new ArrayList<Playlist>();
        while (rs.next()){

            lista.add(new PlaylistMapper().map(rs));
        }

        return lista;
    }

    /**Rimuove la canzone dalla playlist*/
    public boolean doRemoveSong(String titoloPlaylist, String username, String codice) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(PlaylistQuery.getQueryDoRemoveSong());
        preparedStatement.setString(1,titoloPlaylist);
        preparedStatement.setString(2,username);
        preparedStatement.setString(3,codice);
        return preparedStatement.executeUpdate()==0;
    }
    /**Ritorna la playlist con le canzoni*/
    public Playlist doRetrievePlaylistWithSongs(String username, String nome) throws SQLException {
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(PlaylistQuery.getQueryDoRetrievePlaylistWithSongs());
        preparedStatement.setString(1,username);
        preparedStatement.setString(2,nome);
        ResultSet resultSet = preparedStatement.executeQuery();
        Playlist playlist=null;
        if(resultSet.next()){
            playlist = new PlaylistMapper().map(resultSet);
            playlist.setCanzoni(new ArrayList<>());
            if(resultSet.getString("CAN.codice")!=null)
                playlist.getCanzoni().add(new CanzoneMapper().map(resultSet));
            while (resultSet.next())
                playlist.getCanzoni().add(new CanzoneMapper().map(resultSet));
        }
        return playlist;
    }


    /** Modifica la playlist */
    public void doUpdate(Playlist playlist) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(PlaylistQuery.getQueryPlaylistUpdate());
        st.setString(1,playlist.getTitolo());
        st.setString(2,playlist.getNote());
        st.setDouble(3,playlist.getDurata());
        st.setDate(4, Date.valueOf(playlist.getDataCreazione()));
        st.setString(5,playlist.getTitolo());
        st.setString(6, playlist.getUsername());
        if(st.executeUpdate()!=1)
            throw new RuntimeException("delete error");
    }

}
