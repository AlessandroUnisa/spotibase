package data.DAOUtente;

import data.Album.Album;
import data.Album.AlbumMapper;
import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneMapper;
import data.Exceptions.OggettoGiaPresenteException;
import data.Exceptions.OggettoNonCancellatoException;
import data.Exceptions.OggettoNonInseritoException;
import data.Exceptions.OggettoNonTrovatoException;
import data.utils.SingletonJDBC;
import data.Artista.Artista;
import data.Artista.ArtistaMapper;
import data.Attivazione.Attivazione;
import data.Attivazione.AttivazioneMapper;
import data.DAOPlaylist.Playlist;
import data.DAOPlaylist.PlaylistMapper;

import java.lang.annotation.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@interface Generated {
}

public class UtenteDAO implements UtenteAPI{
    //Metodi documentati per IS---------------------------------------------------------------------------------

    //private static final Pattern MAIL_USER = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    private static final Pattern PASSWD = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    private static final Pattern MAIL_USER = Pattern.compile("^(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])$");
    private static final Pattern USERNAME = Pattern.compile("^[a-zA-Z0-9\\-_]{1,40}$");

    public boolean isValidUsername(String username){
        return USERNAME.matcher(username).matches();
    }

    public boolean isValidEmail(String email){
       return MAIL_USER.matcher(email).matches();
    }

    public boolean isValidPasswd(String passwd){
        return PASSWD.matcher(passwd).matches();
    }

    @Override
    /**Questo metodo permette di prelevare un utente dal db
     * @param chiave la username dell'utente
     * @return l'oggetto utente
     * */
    public Utente doGet(String chiave) throws SQLException {
        if(chiave == null)
            throw new IllegalArgumentException("la chiave è null o non valida");
        PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM utente WHERE username=?");
        preparedStatement.setString(1,chiave);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.getRow()==0)
            throw new OggettoNonTrovatoException("L'utente non è stato trovato: "+chiave);
       UtenteMapper mapper = new UtenteMapper();
       return  mapper.map(resultSet);
    }
    /**Cerca gli utente che hanno field = value*/
    public List<Utente> findUsers(String field, String value) throws SQLException {
        if(field == null || value == null )
            throw new IllegalArgumentException("field o value null");
        PreparedStatement statement = SingletonJDBC.getConnection().prepareStatement(UtenteQuery.getQueryFindAccountField(field));
        statement.setString(1,value);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Utente> lista = new ArrayList<>();
        UtenteMapper mapper = new UtenteMapper();
        while (resultSet.next())
            lista.add(mapper.map(resultSet));
        return lista;
    }

    @Override
    public boolean exist(String chiave) throws SQLException {
        if(chiave == null)
            throw new IllegalArgumentException("chiave è null");

        PreparedStatement statement = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM utente WHERE username = ?");
        statement.setString(1, chiave);
        return statement.executeQuery().next();
    }

    /** Salva nel DB l'utente nel db */
    public void doSave(Utente utente) throws SQLException {
        if (utente == null || utente.getUsername() == null || utente.getPassword() == null || utente.getEmail() == null)
            throw new IllegalArgumentException("utente è null o qualche campo obbligatorio è null");

        if(exist(utente.getUsername()))
            throw new OggettoGiaPresenteException("L'utente è gia presente");
        else{
            PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement(UtenteQuery.getQueryUtenteSave());
            preparedStatement.setString(1, utente.getUsername());
            preparedStatement.setString(2, utente.getPassword());
            preparedStatement.setString(3, utente.getEmail());

            if (preparedStatement.executeUpdate() != 1)
                throw new OggettoNonInseritoException("L'utente non è stato inserito");
        }
    }

    /**Cerca un utente per email e password*/
    public Utente doGet(String email, String password) throws SQLException {
        if(email == null || password == null )
            throw new IllegalArgumentException("email o password sono null");
        PreparedStatement statement = SingletonJDBC.getConnection().prepareStatement(UtenteQuery.getQueryFindAccount());
        statement.setString(1,email);
        statement.setString(2,password);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        if(resultSet.getRow()==0)
            throw new OggettoNonTrovatoException("L'utente non è stato trovato");

        return new UtenteMapper().map(resultSet);
    }

    /** Elimina l'utente dal DB
     *  @param username la username dell'utente da eliminare
     * @return true se l'utente è stato eliminato, false altrimenti
     * */
    public void doDelete(String username) throws SQLException {
        if(username == null)
            throw new IllegalArgumentException("l'username è null o non valida");

        if(exist(username)){
            PreparedStatement preparedStatement = SingletonJDBC.getConnection().prepareStatement("DELETE FROM utente WHERE username=?");
            preparedStatement.setString(1,username);
            if(preparedStatement.executeUpdate()!=1)
                throw new OggettoNonCancellatoException("L'utente non è stato cancellato");

        }else throw new OggettoNonTrovatoException("L'utente non è stato trovato");
    }





    // Metodi non documentati per IS -----------------------------------------------------------------------------------
    /** Ritorna tutti gli utenti*/
    @Generated
    public List<Utente> doRetrieveAllUtenti() throws NoSuchAlgorithmException, SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM utente;");
        ResultSet rs = st.executeQuery();
        ArrayList<Utente> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new UtenteMapper().map(rs));

        return lista;
    }

    /** Modifica l'utente */
    @Generated
    public void doUpdate(Utente utente) throws SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(UtenteQuery.getQueryUtenteUpdate());
        st.setString(1,utente.getPassword());
        st.setString(2,utente.getEmail());
        st.setString(3,utente.getUsername());
        if(st.executeUpdate()!=1)
            throw new RuntimeException("delete error");
    }


    /** Ritorna la lista di utenti che hanno quel username */
    @Generated
    public List<Utente> doRetrieveByUsername(String username) throws NoSuchAlgorithmException, SQLException {
        PreparedStatement st = SingletonJDBC.getConnection().prepareStatement("SELECT * FROM utente WHERE username LIKE ?");
        String usernameLike = "%"+username+"%";
        st.setString(1,usernameLike);
        System.out.println(st.toString());
        ResultSet rs = st.executeQuery();
        ArrayList<Utente> lista = new ArrayList<>();
        while (rs.next()){

            lista.add(new UtenteMapper().map(rs));
        }
        return lista;
    }



    /**Ritona un Utente con album, artisti e canzoni preferite, playlist e abbonamenti*/
    @Generated
    public Utente fetchUtenteWithSongsAlbumArtistiPrefPlayAbbon(String username) throws SQLException {
            PreparedStatement st = SingletonJDBC.getConnection().prepareStatement(UtenteQuery.getQueryFetchPrefPlaylistAttByUsername());
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
    }

    private static final Pattern MAIL_ADMIN = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@spotibase.it");
    @Generated
    public boolean isAdminEmail(String email){
        return  MAIL_ADMIN.matcher(email).matches();
    }


}
