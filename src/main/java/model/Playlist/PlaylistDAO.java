package model.Playlist;

import model.Canzone.CanzoneMapper;
import model.ConPool;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlaylistDAO {


    /** Ritorna la lista di playlist che hanno quel titolo*/
    public ArrayList<Playlist> doRetrieveByTitolo(String titolo){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement("SELECT * FROM playlist WHERE titolo LIKE ?");
            String titoloLike = "%"+titolo+"%";
            st.setString(1,titoloLike);
            System.out.println(st.toString());
            ResultSet rs = st.executeQuery();
            ArrayList<Playlist> lista = new ArrayList<Playlist>();
            while (rs.next()){

                lista.add(new PlaylistMapper().map(rs));
            }
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /** Salva nel DB la playlist nel db */
    public void doSave(Playlist playlist) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(PlaylistQuery.getQueryPlaylistSave());
            ps.setString(1, playlist.getTitolo());
            ps.setString(2,playlist.getUsername());
            ps.setString(3, playlist.getNote());
            ps.setDouble(4, playlist.getDurata());
            ps.setDate(5, Date.valueOf(playlist.getDataCreazione()));

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Elimina la playlist dal DB*/
    public void doDelete(String username,String titolo){
        try (Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement("DELETE FROM playlist WHERE titolo=? AND username=?");
            st.setString(1,titolo);
            st.setString(2,username);
            if(st.executeUpdate()!=1)
                throw new RuntimeException("delete error");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /** Modifica la playlist */
    public void doUpdate(Playlist playlist){
        try (Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement(PlaylistQuery.getQueryPlaylistUpdate());
            st.setString(1,playlist.getTitolo());
            st.setString(2,playlist.getNote());
            st.setDouble(3,playlist.getDurata());
            st.setDate(4, Date.valueOf(playlist.getDataCreazione()));
            st.setString(5,playlist.getTitolo());
            st.setString(6, playlist.getUsername());
            if(st.executeUpdate()!=1)
                throw new RuntimeException("delete error");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /** Ritorna tutti le playlist*/
    public List<Playlist> doRetrieveAllPlaylist(){
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement("SELECT * FROM playlist;");
            ResultSet rs = st.executeQuery();
            ArrayList<Playlist> lista = new ArrayList<>();
            while(rs.next()){
                lista.add(new PlaylistMapper().map(rs));
            }
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna la playlist con le canzoni*/
    public Playlist doRetrievePlaylistWithSongs(String username, String nome){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(PlaylistQuery.getQueryDoRetrievePlaylistWithSongs());
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
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Rimuove la canzone dalla playlist*/
    public boolean doRemoveSong(String titoloPlaylist, String username, String codice){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(PlaylistQuery.getQueryDoRemoveSong());
            preparedStatement.setString(1,titoloPlaylist);
            preparedStatement.setString(2,username);
            preparedStatement.setString(3,codice);
            return preparedStatement.executeUpdate()==0;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Ritorna le playlist dell utente*/
    public List<Playlist> doRetrievePlaylistByUtente(String username){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(PlaylistQuery.getQueryDoRetrievePlaylistByUtente());
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Playlist> list = new ArrayList<>();
            PlaylistMapper mapper = new PlaylistMapper();
            while (resultSet.next())
                list.add(mapper.map(resultSet));
            return list;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Inserisce una canzone nella playlist*/
    public boolean doInsertSong(String username, String titoloPlay, String codCanzone){
        try(Connection connection = ConPool.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement(PlaylistQuery.getQueryDoInsertSong());
            preparedStatement.setString(1,codCanzone);
            preparedStatement.setString(2,titoloPlay);
            preparedStatement.setString(3,username);
            return preparedStatement.executeUpdate()==1;
        }catch (SQLException e){
            throw  new RuntimeException(e);
        }
    }

    /**Controlla se la canzone è presente nella playlist*/
    public boolean isPresent(String codiceCanzone, String titolo, String username){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(PlaylistQuery.getQueryIsPresent());
            preparedStatement.setString(1,codiceCanzone);
            preparedStatement.setString(2, titolo);
            preparedStatement.setString(3, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Controlla se la playlist è presente nel db*/
    public boolean isPresent(String titolo, String username){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(PlaylistQuery.getQueryIsPresentPlaylist());
            preparedStatement.setString(1, titolo);
            preparedStatement.setString(2, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Inserisce una nuova playlist nel DB*/
    public boolean doInsertPlaylist(Playlist playlist){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(PlaylistQuery.getQueryDoInsertPlaylist());
            preparedStatement.setString(1,playlist.getTitolo());
            preparedStatement.setString(2,playlist.getUsername());
            preparedStatement.setString(3,playlist.getNote());
            preparedStatement.setDouble(4,0);
            preparedStatement.setDate(5, Date.valueOf(LocalDate.now()));
            return preparedStatement.executeUpdate()==1;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public int doRetrieveNumPlaylistOfUtente(String username){
        try(Connection connection = ConPool.getConnection()){
            PreparedStatement preparedStatement = connection.prepareStatement(PlaylistQuery.getQuerydoRetrieveNumPlaylistOfUtente());
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
               return resultSet.getInt("num");
            else  throw new RuntimeException();
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
