package controller;

import model.Album.AlbumDAO;
import model.Artista.ArtistaDAO;
import model.Canzone.CanzoneDAO;
import model.Playlist.PlaylistDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.TreeSet;

@WebServlet(name = "ServletSearch", value = "/ServletSearch")
public class ServletSearch extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String searchStr = request.getParameter("search");
        CanzoneDAO canzoni= new CanzoneDAO();
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
            request.setAttribute("listaCanzoniAcquistate", new CanzoneDAO().doRetrieveCodiciCanzoniAcquistate(username));
            request.setAttribute("listPlaylist", new PlaylistDAO().doRetrievePlaylistByUtente(username));
        }

        request.getRequestDispatcher("WEB-INF/views/utente/ricerca.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
