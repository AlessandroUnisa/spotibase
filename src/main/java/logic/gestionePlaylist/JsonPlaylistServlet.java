package logic.gestionePlaylist;

import data.DAOPlaylist.PlaylistAPI;
import data.DAOPlaylist.PlaylistDAO;
import data.Exceptions.OggettoNonInseritoException;
import org.json.simple.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "jsonPlaylistServlet", value = "/jsonPlaylistServlet")
public class JsonPlaylistServlet extends HttpServlet {

    private JSONObject insertCanzone(HttpServletRequest request) throws SQLException {
        String codiceCanzone = request.getParameter("codCan");
        String nomePlay = request.getParameter("nomePlay");
        String username = (String) request.getSession(false).getAttribute("username");
        PlaylistAPI playlistDAO = new PlaylistDAO();
        JSONObject object = new JSONObject();

        if(playlistDAO.isPresent(codiceCanzone,nomePlay,username)) //se la canzone è gia presente nella playlist
            object.put("flag", "isPresent");
        else{
            try{
                new PlaylistDAO().doInsertSong(username,nomePlay,codiceCanzone);
                object.put("flag", true);
            }catch ( OggettoNonInseritoException e){
                object.put("flag",false);
            }
        }
        return object;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //aggiunge una canzone alla playlist
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        response.setContentType("application/json");
        try {
            response.getWriter().println(insertCanzone(request));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
