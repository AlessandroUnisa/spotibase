package logic.gestionePlaylist;

import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;
import data.DAOPlaylist.PlaylistAPI;
import data.DAOPlaylist.PlaylistDAO;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;
import data.Exceptions.OggettoNonInseritoException;
import org.json.simple.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
/**Questa classe viene utilizzata per aggiungere la canzone nella playlist
 * @version 1.0
 * @see HttpServlet fornisce l'interfaccia per creare una servlet
 */
@WebServlet(name = "jsonPlaylistServlet", value = "/jsonPlaylistServlet")
public class JsonPlaylistServlet extends HttpServlet {
    /**Questo metodo viene utilizzato per inserire una canzone all'interno della playlist
     * @param request viene utilizzata per prendere il codice della canzone e il nome della playlist e poi dalla sessione lo username dell'utente
     * @param canzoneAPI interfaccia di CanzoneDAO
     * @param playlistAPI interfaccia di PlaylistDAO
     * @param utenteAPI interfaccia dell'UtenteDAO
     * @return ritorna un json object
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws OggettoNonInseritoException Un'eccezione che viene lanciata quando l'oggetto canzone non è stato inserito
     *
     */
    public JSONObject insertCanzone(HttpServletRequest request, CanzoneAPI canzoneAPI, PlaylistAPI playlistAPI, UtenteAPI utenteAPI) throws SQLException {
        String codiceCanzone = request.getParameter("codCan");
        String nomePlay = request.getParameter("nomePlay");
        String username = (String) request.getSession(false).getAttribute("username");
        JSONObject object = new JSONObject();


        if(playlistAPI.isPresent(codiceCanzone,nomePlay,username,canzoneAPI,playlistAPI,utenteAPI)) //se la canzone è gia presente nella playlist
            object.put("flag", "isPresent");
        else{
            try{
                playlistAPI.doInsertSong(username,nomePlay,codiceCanzone,utenteAPI,canzoneAPI,playlistAPI);
                object.put("flag", true);
            }catch ( OggettoNonInseritoException e){
                object.put("flag",false);
            }
        }
        return object;
    }

    /** Questo metodo viene utilizzato per inizializzare le interfaccie  e passarle al metodo insertCanzone tramite response
     *
     * @param request contiene le informazioni del codice della canzone e nome playlist per poi passarle al metodo insertCanzone
     * @param response utlizzata per mandare i dati relativi in output di insetCanzone
     * @throws ServletException Un'eccezione lanciata quando c'è un problema nella servlet
     * @throws IOException Un' eccezione che viene lanciata quando c'è un errore di I/O
     */
    @Override
    @Generated
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //aggiunge una canzone alla playlist
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        response.setContentType("application/json");
        UtenteAPI utenteAPI = new UtenteDAO();
        PlaylistAPI playlistAPI = new PlaylistDAO();
        if(request.getParameter("cod") != null && request.getParameter("cod").equals("check")){
            try {
                response.getWriter().println(checkTitoloGiaPresente(request,playlistAPI,utenteAPI));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        else{
            try {
                CanzoneAPI canzoneAPI = new CanzoneDAO();

                response.getWriter().println(insertCanzone(request,canzoneAPI,playlistAPI, utenteAPI));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    @Generated
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    /** Questo metodo viene utilizzato dal front-end per sapere se l'utente ha gia una playlist con quel titolo
     *
     * @param request contiene il titolo della playlist
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @return l'oggetto json che contiene un flag, flag=true se la playlist è presente, flag=false altrimenti
     */
    public JSONObject checkTitoloGiaPresente(HttpServletRequest request, PlaylistAPI playlistAPI,  UtenteAPI utenteAPI) throws SQLException {
        String username = (String) request.getSession(false).getAttribute("username");
        JSONObject object = new JSONObject();

        String titolo = request.getParameter("titolo");
        System.out.println(titolo+"-  -"+ username);
        if(playlistAPI.isPresent(titolo, username, utenteAPI)){
            System.out.println("--true");
            object.put("checkT", true);
        }else{
            System.out.println("--false");
            object.put("checkT", false);
        }
        return  object;
    }
}
