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

/**Questa classe permette  di gestire le operazioni relative ai dati persistenti dell’utente.
 * @version 1.0
 * @see UtenteAPI interfacci della classe
 * */
public class UtenteDAO implements UtenteAPI{
    //Metodi documentati per IS---------------------------------------------------------------------------------

    //private static final Pattern MAIL_USER = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$");
    private static final Pattern PASSWD = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,}$");
    private static final Pattern MAIL_USER = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]{1,63}@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
    private static final Pattern USERNAME = Pattern.compile("^[a-zA-Z0-9\\-_]{1,40}$");

    Connection connection;
    public UtenteDAO(){
        this.connection = SingletonJDBC.getConnection();
    }
    public UtenteDAO(Connection connectionTesting) {
        this.connection = connectionTesting;
    }


    /**Questo metodo serve per convalidare lo username dell'utente secondo la regex
     * <p><b>pre: </b> lo username != null </p>
     * @param username username dell'utente per verificare la sua correttezza
     * @return true se l'username è falido, false altrimenti
     * */
    public boolean isValidUsername(String username){

        return USERNAME.matcher(username).matches();
    }


    /** Questo metodo consente di validare l’email dell’utente secondo la regex
     * <p><b>pre: </b>email != null</p>
     * @param email email dell'utente da convalidare
     * @return true se l'email rispetta la regex false altrimenti
     * */
    public boolean isValidEmail(String email){
       return MAIL_USER.matcher(email).matches();
    }


    /** Questo metodo consente di validare la password dell’utente secondo la regex
     * <p><b>pre: </b>password != null</p>
     * @param passwd password dell'utente da convalidare
     * @return true se la password rispetta la regex, false altrimenti
     * */
    public boolean isValidPasswd(String passwd){
        return PASSWD.matcher(passwd).matches();
    }


    /**Questo metodo permette di prelevare un utente dal db
     * <p><b>pre: </b>chiave != null, chiave.contains(;) == true e l'utente deve esistere nel db</p>
     * @param chiave l'username dell'utente
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando la chiave è null o non valida
     * @throws OggettoNonTrovatoException Un'eccezione che viene lanciata quando l'utente non è stato trovato nel db
     * @return l'oggetto utente
     * */
    public Utente doGet(String chiave) throws SQLException {
        if(chiave == null)
            throw new IllegalArgumentException("la chiave è null o non valida");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM utente UTE WHERE username=?");
        preparedStatement.setString(1,chiave);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        if(resultSet.getRow()==0)
            throw new OggettoNonTrovatoException("L'utente non è stato trovato: "+chiave);
       UtenteMapper mapper = new UtenteMapper();
       return  mapper.map(resultSet);
    }

    /**Cerca gli utente che hanno field = value
     * <p><b>pre: </b> field != null e value != null</p>
     * @param field parametro della query attraverso il quale si effettua la ricerca dell'utente
     * @param value valore passato al parametro per la ricerca dell'utente
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando field o value dell'utente è null o non valido
     * @return lista di utenti
     * */
    public List<Utente> findUsers(String field, String value) throws SQLException {
        if(field == null || value == null )
            throw new IllegalArgumentException("field o value null");
        PreparedStatement statement = connection.prepareStatement(UtenteQuery.getQueryFindAccountField(field));
        statement.setString(1,value);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<Utente> lista = new ArrayList<>();
        UtenteMapper mapper = new UtenteMapper();
        while (resultSet.next())
            lista.add(mapper.map(resultSet));
        return lista;
    }

    /**Questo metodo consente di verificare l’acquisto all’interno del database
     * <p><b>pre: </b>chiave != null</p>
     * @param chiave rappresenta lo username dell'utente
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando la chiave è null o non valida
     * @return true se l'utente esiste, false altrimenti
     */
    public boolean exist(String chiave) throws SQLException {
        if(chiave == null)
            throw new IllegalArgumentException("chiave è null");

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM utente WHERE username = ?");
        statement.setString(1, chiave);
        return statement.executeQuery().next();
    }

    /** Salva nel DB l'utente nel db
     * <p><b>pre: </b>utente deve essere diverso da null e non esistere nel db, email!= null, password != null e l'username != null<br>
     *    <b>post: </b>l'utente è presente nel db</p>
     * @param utente l'oggetto da salvare
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando l'utente o l'username dell'utente o la password oppure l'email sono null o non validi
     * @throws OggettoGiaPresenteException  Un'eccezione che viene lanciata quando l'utente è già presente nel db
     * @throws OggettoNonInseritoException Un'eccezione che viene lanciata quando l'utente non è stato inserito
     * */
    public void doSave(Utente utente) throws SQLException {
        if (utente == null || utente.getUsername() == null || utente.getPassword() == null || utente.getEmail() == null)
            throw new IllegalArgumentException("utente è null o qualche campo obbligatorio è null");

        if(exist(utente.getUsername()))
            throw new OggettoGiaPresenteException("L'utente è gia presente");
        else{
            PreparedStatement preparedStatement = connection.prepareStatement(UtenteQuery.getQueryUtenteSave());
            preparedStatement.setString(1, utente.getUsername());
            preparedStatement.setString(2, utente.getPassword());
            preparedStatement.setString(3, utente.getEmail());

            if (preparedStatement.executeUpdate() != 1)
                throw new OggettoNonInseritoException("L'utente non è stato inserito");
        }
    }

    /**Cerca un utente per email e password
     * <p><b>pre: </b>email != null,password != null e la loro corrispondenza deve esistere nel db</p>
     * @param email email dell'utente
     * @param password password dell'utente
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando l'email o password dell'utente sono null o non valide
     * @throws OggettoNonTrovatoException Un'eccezione che viene lanciata quando l'utente non è stato trovato nel db
     * @return un oggetto utente
     * */
    public Utente doGet(String email, String password) throws SQLException {
        if(email == null || password == null )
            throw new IllegalArgumentException("email o password sono null");
        PreparedStatement statement = connection.prepareStatement(UtenteQuery.getQueryFindAccount());
        statement.setString(1,email);
        statement.setString(2,password);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        if(resultSet.getRow()==0)
            throw new OggettoNonTrovatoException("L'utente non è stato trovato");

        return new UtenteMapper().map(resultSet);
    }

    /** Elimina l'utente dal DB
     * <p><b>pre: </b>username != null e presente nel db<br>
     *    <b>post: </b>utente eliminato dal db </p>
     * @param username la username dell'utente da eliminare
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando l'username è null o non valida
     * @throws OggettoNonCancellatoException  Un'eccezione che viene lanciata quando l'utente non è stato cancellato nel db
     * @throws OggettoNonTrovatoException Un'eccezione che viene lanciata quando l'utente non è stato trovato nel db
     * */
    public void doDelete(String username) throws SQLException {
        if(username == null)
            throw new IllegalArgumentException("l'username è null o non valida");

        if(exist(username)){
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM utente WHERE username=?");
            preparedStatement.setString(1,username);
            if(preparedStatement.executeUpdate()!=1)
                throw new OggettoNonCancellatoException("L'utente non è stato cancellato");

        }else throw new OggettoNonTrovatoException("L'utente non è stato trovato");
    }





    // Metodi non documentati per IS -----------------------------------------------------------------------------------
    /** Ritorna tutti gli utenti*/
    @Generated
    public List<Utente> doRetrieveAllUtenti() throws NoSuchAlgorithmException, SQLException {
        PreparedStatement st = connection.prepareStatement("SELECT * FROM utente;");
        ResultSet rs = st.executeQuery();
        ArrayList<Utente> lista = new ArrayList<>();
        while(rs.next())
            lista.add(new UtenteMapper().map(rs));

        return lista;
    }

    /** Modifica l'utente */
    @Generated
    public void doUpdate(Utente utente) throws SQLException {
        PreparedStatement st = connection.prepareStatement(UtenteQuery.getQueryUtenteUpdate());
        st.setString(1,utente.getPassword());
        st.setString(2,utente.getEmail());
        st.setString(3,utente.getUsername());
        if(st.executeUpdate()!=1)
            throw new RuntimeException("delete error");
    }


    /** Ritorna la lista di utenti che hanno quel username */
    @Generated
    public List<Utente> doRetrieveByUsername(String username) throws NoSuchAlgorithmException, SQLException {
        PreparedStatement st = connection.prepareStatement("SELECT * FROM utente WHERE username LIKE ?");
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
            PreparedStatement st = connection.prepareStatement(UtenteQuery.getQueryFetchPrefPlaylistAttByUsername());
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
