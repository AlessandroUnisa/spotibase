package controller.admin;


import model.Album.AlbumDAO;
import model.Album.AlbumMapperJSON;
import model.Artista.ArtistaDAO;
import model.Artista.ArtistaMapperJSON;
import model.Canzone.CanzoneDAO;
import model.Canzone.CanzoneMapperJSON;
import org.json.simple.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class JSONServlet
 */
@WebServlet("/JsonAdmin")
public class JsonAdmin extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //tabelle elementi json
        response.setContentType("application/json");
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        JSONArray array= new JSONArray();
        int codice = Integer.parseInt(request.getParameter("cod"));
        int pagina = Integer.parseInt(request.getParameter("page"));
        final int ELEMENTS_FOR_PAGE = 10;

        switch (codice) {
            case 1:
                new AlbumDAO().doRetrieveAllAlbum((pagina-1)*ELEMENTS_FOR_PAGE, ELEMENTS_FOR_PAGE).forEach(item->array.add(new AlbumMapperJSON().map(item)));
                response.getWriter().println(array);
            break;
            case 2:
                new CanzoneDAO().doRetrieveAllCanzoni((pagina-1)*ELEMENTS_FOR_PAGE, ELEMENTS_FOR_PAGE).forEach(item->array.add(new CanzoneMapperJSON().map(item)));
                response.getWriter().println(array);
            break;
            case 3:
                new ArtistaDAO().doRetrieveAllArtist((pagina-1)*ELEMENTS_FOR_PAGE, ELEMENTS_FOR_PAGE).forEach(item->array.add(new ArtistaMapperJSON().map(item)));
                response.getWriter().println(array);
            break;
            default:
                response.getWriter().println("Errore java");
        }

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
