package logic.gestioneCanzone;

import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;
import data.DAOPreferenza.PreferenzaAPI;
import data.DAOPreferenza.PreferenzaDAO;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "jsonAlbumServlet", value = "/jsonCanzoneServlet")
public class jsonCanzoneServlet extends HttpServlet {

    private JSONObject getCanzone(HttpServletRequest request) throws SQLException {
        String code = request.getParameter("cod");
        JSONObject obj = new JSONObject();
        CanzoneAPI canzoneDAO = new CanzoneDAO();
        String username = (String) request.getSession(false).getAttribute("username");

        PreferenzaAPI preferenzaAPI = new PreferenzaDAO();
        if(username!=null){ //se il tasto play è stato cliccato da un utente loggato
            obj.put("isLogged",true);
            obj.put("pref",preferenzaAPI.doRetrieveaCodiciCanzoniPreferite(username).contains(code)); //vedo se la canzone è tra i preferiti
        }else obj.put("isLogged", false);

        Canzone canzone = canzoneDAO.doRetrieveCanzoneWithArtisti(code);
        obj.put("url",canzone.getPathMP3());  //setto i campi che servono per il footer
        obj.put("titolo",canzone.getTitolo());
        obj.put("artista",canzone.getArtisti().get(0).getNomeDArte());
        return obj;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //per il tasto play
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        try {
            response.getWriter().println(getCanzone(request));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
