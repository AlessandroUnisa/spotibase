package controller.canzone;

import model.Canzone.Canzone;
import model.Canzone.CanzoneDAO;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "jsonAlbumServlet", value = "/jsonCanzoneServlet")
public class jsonCanzoneServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //per il tasto play
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String code = request.getParameter("cod");
        JSONObject obj = new JSONObject();
        CanzoneDAO canzoneDAO = new CanzoneDAO();
        String username = (String) request.getSession(false).getAttribute("username");

        if(username!=null){ //se il tasto play è stato cliccato da un utente loggato
            obj.put("isLogged",true);
            obj.put("pref",canzoneDAO.doRetrieveaCodiciCanzoniPreferite(username).contains(code)); //vedo se la canzone è tra i preferiti
        }else obj.put("isLogged", false);

        Canzone canzone = canzoneDAO.doRetrieveCanzoneWithArtisti(code);
        obj.put("url",canzone.getPathMP3());  //setto i campi che servono per il footer
        obj.put("titolo",canzone.getTitolo());
        obj.put("artista",canzone.getArtisti().get(0).getNomeDArte());
        response.setContentType("application/json");
        response.getWriter().println(obj);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
