package controller.playlist;

import com.mysql.cj.xdevapi.JsonArray;
import model.Playlist.PlaylistDAO;
import org.json.simple.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet(name = "jsonPlaylistServlet", value = "/jsonPlaylistServlet")
public class jsonPlaylistServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //aggiunge una canzone alla playlist
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String codiceCanzone = request.getParameter("codCan");
        String nomePlay = request.getParameter("nomePlay");
        String username = (String) request.getSession(false).getAttribute("username");
        PlaylistDAO playlistDAO = new PlaylistDAO();
        JSONObject object = new JSONObject();

        if(playlistDAO.isPresent(codiceCanzone,nomePlay,username)) //se la canzone è gia presente nella playlist
            object.put("flag", "isPresent");
        else object.put("flag", new PlaylistDAO().doInsertSong(username,nomePlay,codiceCanzone));
        response.setContentType("application/json");
        response.getWriter().println(object);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
