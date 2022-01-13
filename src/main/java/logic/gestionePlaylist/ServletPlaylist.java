package logic.gestionePlaylist;

import data.Artista.ArtistaDAO;
import data.Attivazione.Attivazione;
import data.Attivazione.AttivazioneDAO;
import data.DAOAcquisto.AcquistoAPI;
import data.DAOAcquisto.AcquistoDAO;
import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;
import data.DAOPlaylist.Playlist;
import data.DAOPlaylist.PlaylistAPI;
import data.DAOPlaylist.PlaylistDAO;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;
import data.Exceptions.OggettoGiaPresenteException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.sql.SQLException;
import java.util.TreeSet;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@interface Generated {
}
@WebServlet(name = "ServletPlaylist", value = "/playlist/*")
public class ServletPlaylist extends HttpServlet {
    /** Questo metodo viene utilizzato per cancellare una canzone dalla playlist
     *
     * @param request contiene il codice della canzone da cancellare, nome della playlist e dalla sessione lo username dell'utente
     * @param playlistAPI interfaccia di playlistDAO
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     */
    @Generated
    public void cancellaCanzone(HttpServletRequest request,PlaylistAPI playlistAPI) throws SQLException {
        String codiceDel = request.getParameter("del"); //cancellazione canzone
        if(codiceDel!=null){
            String username = (String) request.getSession(true).getAttribute("username");
            String nomePlaylist = request.getParameter("name");
            playlistAPI.doRemoveSong(nomePlaylist,username,codiceDel);
        }
    }

    /** Questo metodo mostra la playlist dell'utente
     *
     * @param request serve per prendere i parametri come il nome della playlist e dalla sessione verificare se l'utente è loggato oppure no
     * @param playlistAPI interfaccia di playlistDAO
     * @param canzoneAPI interfaccia di canzoneDAO
     * @param acquistoAPI interfaccia di acquistoDAO
     * @param artistaDAO interfaccia di artistaDAO
     * @return oggetto playlist
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     */
    public Playlist showPlaylist(HttpServletRequest request,  PlaylistAPI playlistAPI,CanzoneAPI canzoneAPI,AcquistoAPI acquistoAPI,ArtistaDAO artistaDAO) throws SQLException {
        String nomePlaylist = request.getParameter("name");
        String username = (String) request.getSession(true).getAttribute("username");

        HttpSession session = request.getSession(true);
        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());

        if(session.getAttribute("isLogged")!=null){
            request.setAttribute("listaPreferiti",canzoneAPI.doRetrieveaCodiciCanzoniPreferite(username));
            request.setAttribute("listaCanzoniAcquistate", acquistoAPI.doRetrieveCodiciCanzoniAcquistate(username));
            UtenteAPI utenteAPI = new UtenteDAO();
            request.setAttribute("listPlaylist",playlistAPI.doRetrievePlaylistByUtente(username,utenteAPI));
        }

        Playlist playlist = playlistAPI.doRetrievePlaylistWithSongs(username,nomePlaylist);
        //prendo gli artisti di ogni canzone

            playlist.getCanzoni().forEach(item-> {
                try {
                    item.setArtisti(artistaDAO.doRetrieveArtistiBySong(item.getCodice()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            });
        return playlist;
    }

    /** Questo metodo viene utilizzato per visualizzare la jsp della playlist
     * @param request viene passata la metodo showPlaylist e cnacella canzone
     * @param response  viene passata per effettuare il forward
     * @throws ServletException Un'eccezione lanciata quando c'è un problema nella servlet
     * @throws IOException Un' eccezione che viene lanciata quando c'è un errore di I/O
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     */
    @Override
    @Generated
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //mostra la playlist
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        PlaylistAPI playlistAPI = new PlaylistDAO();
        CanzoneAPI canzoneAPI = new CanzoneDAO();
        AcquistoAPI acquistoAPI = new AcquistoDAO();
        try {
            cancellaCanzone(request,playlistAPI);
            System.out.println(showPlaylist(request,playlistAPI,canzoneAPI,acquistoAPI,new ArtistaDAO())+"*************");
            request.setAttribute("playlist", showPlaylist(request,playlistAPI,canzoneAPI,acquistoAPI,new ArtistaDAO()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        request.getRequestDispatcher("WEB-INF/views/playlist/playlist.jsp").forward(request,response);
    }

    /** Questo metodo viene utilizzare per cancellare la playlist
     * @param request per prendere lo username dalla sessione e la canzone da canellare
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     */
    @Generated
    private void cancellaPlaylist(HttpServletRequest request) throws SQLException {
        String username = (String) request.getSession(false).getAttribute("username");
        String titolo = request.getParameter("delete");
        PlaylistAPI playlistAPI = new PlaylistDAO();
        playlistAPI.doDelete(titolo+";"+username);
    }

    /** Questo metodo viene utilizzato per creare una nuova playlist
     *
     * @param request viene utilizzata per prendere lo username dell'utente e il titolo della playlist
     * @param response per fare la redirect nel caso di errore oppure in caso di successo
     * @param playlistAPI interfaccia di playlistDAO
     * @throws IOException Un' eccezione che viene lanciata quando c'è un errore di I/O
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando il titolo o la nota è null o non valida
     */
    public void creaPlaylist(HttpServletRequest request, HttpServletResponse response,PlaylistAPI playlistAPI) throws SQLException, IOException {
        String username = (String) request.getSession(false).getAttribute("username");
        String titolo = request.getParameter("titolo");
        System.out.println(titolo);

        if(!playlistAPI.isValidTitolo(titolo)){
            throw new IllegalArgumentException("titolo non valido");
        }

        AttivazioneDAO attivazioneDAO = new AttivazioneDAO();

        UtenteAPI utenteAPI = new UtenteDAO();
        int numPlaylist = playlistAPI.doRetrieveNumPlaylistOfUtente(username,utenteAPI);


        if(playlistAPI.isPresent(titolo,username,utenteAPI)) { //titolo gia presente
            response.sendRedirect("../libreria?presentPlay=1");
            //throw new IllegalArgumentException("playlist già esistente");
            //throw new OggettoGiaPresenteException("playlist gia esistente");
        }

        else {
            boolean flag = false;
            if (numPlaylist > 3) {
                for (Attivazione attivazione : attivazioneDAO.doRetrieveAttivazioniAttiveByUtente(username)) {
                    if (attivazione.getAbbonamento().getNome().equals("plus") && numPlaylist + 1 < 15) {
                        flag = true;
                        break;
                    } else if (attivazione.getAbbonamento().getNome().equals("mega") && numPlaylist + 1 < 45) {
                        flag = true;
                        break;
                    } else if (attivazione.getAbbonamento().getNome().equals("ultra")) {
                        flag = true;
                        break;
                    }
                }
            } else flag = true;
            System.out.println(numPlaylist);
            System.out.println(flag);
            if (!flag) //num max raggiunto
                response.sendRedirect("../libreria?errInsPlay=1");
            else {
                String note = request.getParameter("note");
                if( !playlistAPI.isValidNota(note)){
                    System.out.println(note);
                    throw new IllegalArgumentException("nota non valida");
                }
                else {
                    Playlist playlist = new Playlist();
                    playlist.setTitolo(titolo);
                    playlist.setNote(note);
                    playlist.setUsername(username);
                    playlistAPI.doSave(playlist);
                    response.sendRedirect("../libreria");
                }
            }
        }
    }

    /** Questo metodo in base alla path si cancella o crea una playlist
     *
     * @param request utilizzata per prenere la path
     * @param response utilizzata per creaPlaylist
     * @throws IOException Un' eccezione che viene lanciata quando c'è un errore di I/O
     * @throws ServletException Un'eccezione lanciata quando c'è un problema nella servlet
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        switch (path){
            case "/cancella": {
                //cancellazione playlist
                try {
                    cancellaPlaylist(request);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                response.sendRedirect("../libreria");
                break;
            }
            case "/crea": {
                //inserimento playlist. senza abbonamento max 4, plus-->15, mega-->45, ultra illimitate
                try {
                    PlaylistAPI playlistAPI = new PlaylistDAO();
                    creaPlaylist(request,response,playlistAPI);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
                }
            }
        }

    }

