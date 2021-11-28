package model.Utente;

import model.Album.Album;
import model.Album.AlbumMapper;
import model.Artista.Artista;
import model.Artista.ArtistaMapper;
import model.Attivazione.Attivazione;
import model.Attivazione.AttivazioneMapper;
import model.Canzone.Canzone;
import model.Canzone.CanzoneMapper;
import model.ConPool;
import model.Playlist.Playlist;
import model.Playlist.PlaylistMapper;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class UtenteDAO {

    /**Cerca un utente per email e password*/
    public Utente findUser(String email, String password){
        try (Connection conn = ConPool.getConnection()){
            PreparedStatement statement = conn.prepareStatement(UtenteQuery.getQueryFindAccount());
            statement.setString(1,email);
            statement.setString(2,password);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
                return new UtenteMapper().map(resultSet);
            else return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /**Cerca gli utente che hanno field = value*/
    public List<Utente> findUsers(String field, String value){
        try (Connection conn = ConPool.getConnection()){
            PreparedStatement statement = conn.prepareStatement(UtenteQuery.getQueryFindAccountField(field));
            statement.setString(1,value);
            ResultSet resultSet = statement.executeQuery();
            ArrayList<Utente> lista = new ArrayList<>();
            UtenteMapper mapper = new UtenteMapper();
            while (resultSet.next())
                 lista.add(mapper.map(resultSet));
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }


    /**Ritona un Utente con album, artisti e canzoni preferite, playlist e abbonamenti*/
   public Utente fetchUtenteWithSongsAlbumArtistiPrefPlayAbbon(String username) {
        try (Connection conn = ConPool.getConnection()){
            PreparedStatement st = conn.prepareStatement(UtenteQuery.getQueryFetchPrefPlaylistAttByUsername());
            st.setString(1,username);
            System.out.println(st);
            ResultSet rs = st.executeQuery();


            AlbumMapper albumMapper = new AlbumMapper();
            CanzoneMapper canzoneMapper = new CanzoneMapper();
            ArtistaMapper artistaMapper = new ArtistaMapper();
            PlaylistMapper playlistMapper = new PlaylistMapper();
            AttivazioneMapper attivazioneMapper = new AttivazioneMapper();

            Map<String, Album> mapAlbumPref = new LinkedHashMap<>();
            Map<String, Canzone> mapCanzoniPerf = new LinkedHashMap<>();
            Map<String, Artista> mapArtistiPref = new LinkedHashMap<>();
            Map<String, Playlist> mapPlaylist = new LinkedHashMap<>();
           ArrayList<Attivazione> listAttivazione = new ArrayList<>();

            while(rs.next()){
                String idAlbum = rs.getString("ALB.codice");
                if(idAlbum!=null && !mapAlbumPref.containsKey(idAlbum)){
                    Album album = albumMapper.map(rs);
                    mapAlbumPref.put(idAlbum,album);
                }

                String idCanzone = rs.getString("CAN.codice");
                if(idCanzone!=null && !mapCanzoniPerf.containsKey(idCanzone)){
                    Canzone canzone = canzoneMapper.map(rs);
                    System.out.println(idCanzone);
                    System.out.println(canzone);
                    mapCanzoniPerf.put(idCanzone,canzone);
                }

                String idArtista = rs.getString("ART.nomeDArte");
                if(idArtista!=null && !mapArtistiPref.containsKey(idArtista)){
                    Artista artista = artistaMapper.map(rs);
                    mapArtistiPref.put(idArtista,artista);
                }

                String idPlaylist = rs.getString("PLA.titolo");
                if(idPlaylist!=null && !mapPlaylist.containsKey(idPlaylist)){
                    Playlist playlist = playlistMapper.map(rs);
                    mapPlaylist.put(idPlaylist,playlist);
                }

                String idAttivazione = rs.getString("ATT.dataFine");
                if(idAttivazione!=null){
                    Attivazione attivazione = attivazioneMapper.map(rs);
                    if(!listAttivazione.contains(attivazione))
                        listAttivazione.add(attivazione);
                }
            }//end resultSet

            Utente utente = new Utente();
            utente.setAlbums(new ArrayList<>(mapAlbumPref.values()));
            utente.setCanzoni(new ArrayList<>(mapCanzoniPerf.values()));
            utente.setArtisti(new ArrayList<>(mapArtistiPref.values()));
            utente.setPlaylists(new ArrayList<>(mapPlaylist.values()));
            utente.setAttivazioni(listAttivazione);
            return utente;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
   }

    /** Ritorna la lista di utenti che hanno quel username */
    public List<Utente> doRetrieveByUsername(String username) throws NoSuchAlgorithmException{
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement("SELECT * FROM utente WHERE username LIKE ?");
            String usernameLike = "%"+username+"%";
            st.setString(1,usernameLike);
            System.out.println(st.toString());
            ResultSet rs = st.executeQuery();
            ArrayList<Utente> lista = new ArrayList<>();
            while (rs.next()){

                lista.add(new UtenteMapper().map(rs));
            }
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /** Salva nel DB l'utente nel db */
    public void doSave(Utente utente) {
        try (Connection con = ConPool.getConnection()) {
            PreparedStatement ps = con.prepareStatement(UtenteQuery.getQueryUtenteSave());
            ps.setString(1, utente.getUsername());
            ps.setString(2, utente.getPassword());
            ps.setString(3, utente.getEmail());

            if (ps.executeUpdate() != 1) {
                throw new RuntimeException("INSERT error.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** Elimina l'utente dal DB*/
    public void doDelete(String username){
        try (Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement("DELETE FROM utente WHERE username=?");
            st.setString(1,username);
            if(st.executeUpdate()!=1)
                throw new RuntimeException("delete error");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /** Modifica l'utente */
    public void doUpdate(Utente utente){
        try (Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement(UtenteQuery.getQueryUtenteUpdate());
            st.setString(1,utente.getPassword());
            st.setString(2,utente.getEmail());
            st.setString(3,utente.getUsername());
            if(st.executeUpdate()!=1)
                throw new RuntimeException("delete error");
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    /** Ritorna tutti gli utenti*/
    public List<Utente> doRetrieveAllUtenti() throws NoSuchAlgorithmException{
        try(Connection con = ConPool.getConnection()){
            PreparedStatement st = con.prepareStatement("SELECT * FROM utente;");
            ResultSet rs = st.executeQuery();
            ArrayList<Utente> lista = new ArrayList<>();
            while(rs.next())
                lista.add(new UtenteMapper().map(rs));
            con.close();
            return lista;
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
