package logic;

import data.Album.AlbumDAO;
import data.Artista.ArtistaDAO;
import data.DAOAcquisto.AcquistoDAO;
import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;
import data.DAOPlaylist.PlaylistDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TreeSet;

@WebServlet(name = "ServletSearch", value = "/ServletSearch")
public class ServletSearch extends HttpServlet {

    private void getSearch(HttpServletRequest request) throws SQLException {
        String searchStr = request.getParameter("search");
        CanzoneAPI canzoni= new CanzoneDAO();
        ArtistaDAO artisti= new ArtistaDAO();
        AlbumDAO album = new AlbumDAO();
        request.setAttribute("canzoni",canzoni.doRetrieveByName(searchStr));
        request.setAttribute("artisti",artisti.doRetrieveByName(searchStr));
        request.setAttribute("album",album.doRetrieveAlbumByName(searchStr));

        HttpSession session = request.getSession(true);
        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());

        if(session.getAttribute("isLogged")!=null){
            String username = (String)session.getAttribute("username");
            request.setAttribute("listaPreferiti",new CanzoneDAO().doRetrieveaCodiciCanzoniPreferite(username));
            try {
                request.setAttribute("listaCanzoniAcquistate", new AcquistoDAO().doRetrieveCodiciCanzoniAcquistate(username));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            request.setAttribute("listPlaylist", new PlaylistDAO().doRetrievePlaylistByUtente(username));
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        try {
            getSearch(request);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        request.getRequestDispatcher("WEB-INF/views/utente/ricerca.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
