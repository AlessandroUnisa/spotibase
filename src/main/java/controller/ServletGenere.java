package controller;

import model.Album.AlbumDAO;
import model.Canzone.CanzoneDAO;
import model.Playlist.PlaylistDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.TreeSet;

@WebServlet(name = "ServletGenere", value = "/genere")
public class ServletGenere extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        HttpSession session = request.getSession(true);
        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());

        if(session.getAttribute("isLogged")!=null) {
            String username = (String) session.getAttribute("username");
            request.setAttribute("listaPreferiti", new CanzoneDAO().doRetrieveaCodiciCanzoniPreferite(username));
            request.setAttribute("listaCanzoniAcquistate", new CanzoneDAO().doRetrieveCodiciCanzoniAcquistate(username));
            request.setAttribute("listPlaylist", new PlaylistDAO().doRetrievePlaylistByUtente(username));
        }

        AlbumDAO album= new AlbumDAO();
        String gen= request.getParameter("genere");
        request.setAttribute("albumGenere",album.doRetrieveAlbumByGenere(gen));
        CanzoneDAO canzone= new CanzoneDAO();
        request.setAttribute("canzoniGenere",canzone.doRetrieveCanzoneByGenere(gen));
        request.getRequestDispatcher("WEB-INF/views/genere/genere.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
