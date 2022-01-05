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

@WebServlet(name = "jsonPlaylistServlet", value = "/jsonPlaylistServlet")
public class JsonPlaylistServlet extends HttpServlet {

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

    @Override
    @Generated
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //aggiunge una canzone alla playlist
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        response.setContentType("application/json");
        try {
            CanzoneAPI canzoneAPI = new CanzoneDAO();
            PlaylistAPI playlistAPI = new PlaylistDAO();
            UtenteAPI utenteAPI = new UtenteDAO();
            response.getWriter().println(insertCanzone(request,canzoneAPI,playlistAPI, utenteAPI));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    @Generated
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
