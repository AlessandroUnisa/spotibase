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
import java.util.ArrayList;
import java.util.TreeSet;


@WebServlet(name = "ServletIndex", value = "/index.html")
public class ServletIndex extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        CanzoneDAO canzoni= new CanzoneDAO();
        ArtistaDAO artisti= new ArtistaDAO();
        AlbumDAO album = new AlbumDAO();
        request.setAttribute("canzoni",canzoni.doRetrivePopularSongsWithArtista());
        request.setAttribute("artisti",artisti.doRetrieveArtistPopular());
        request.setAttribute("albums",album.doRetrieveAlbumPopular());
        request.setAttribute("artistiCasuali",artisti.doRetrieveArtistRandom());
        request.setAttribute("albumCasuali",album.doRetrieveAlbumRandom());
        request.setAttribute("canzoniCasuali",canzoni.doRetrieveCanzoneRandom());
        request.setAttribute("canzoniNew",canzoni.doRetrieveCanzoniUltimeUscite());
        request.setAttribute("albumNew",album.doRetrieveAlbumUltimeUscite());
        request.setAttribute("canzoniTopBuy",canzoni.doRetrieveCanzoniTopBuy());
        request.setAttribute("albumTopBuy",album.doRetrieveAlbumTopBuy());


        HttpSession session = request.getSession(true);
        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());

        if(session.getAttribute("isLogged")!=null){
            String username = (String)session.getAttribute("username");
            request.setAttribute("listaPreferiti",new CanzoneDAO().doRetrieveaCodiciCanzoniPreferite(username));
            request.setAttribute("listaCanzoniAcquistate", new CanzoneDAO().doRetrieveCodiciCanzoniAcquistate(username));
            request.setAttribute("listPlaylist", new PlaylistDAO().doRetrievePlaylistByUtente(username));
        }

        request.getRequestDispatcher("WEB-INF/views/utente/index.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
