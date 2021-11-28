package controller.album;

import model.Album.Album;
import model.Album.AlbumDAO;
import model.Artista.Artista;
import model.Artista.ArtistaDAO;
import model.Canzone.Canzone;
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
import java.util.List;
import java.util.TreeSet;

@WebServlet(name = "albumServlet", value = "/album")
public class albumServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String codice = request.getParameter("cod");  //prendo il codice dell album
        Album album = new AlbumDAO().doRetrieveAlbumWithSongs(codice);  //prendo i dati dell album con le canzoni che contiene
        ArtistaDAO artistaDAO = new ArtistaDAO();

        for(Canzone canzone : album.getCanzoni())
            canzone.setArtisti(artistaDAO.doRetrieveArtistiBySongGroupNomeDArte(canzone.getCodice()));

        //per il button artista
        List<Artista> artistaAlb= new ArtistaDAO().doRetriveArtistaByAlbum(codice);
        request.setAttribute("artistaNome",artistaAlb.get(0).getNomeDArte());

        HttpSession session = request.getSession(true);
        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());

        if(session.getAttribute("isLogged")!=null) {
            String username = (String) session.getAttribute("username");
            request.setAttribute("canzoniPreferite",new CanzoneDAO().doRetrieveaCodiciCanzoniPreferite(username));
            request.setAttribute("flagAlbumPreferito", new AlbumDAO().doRetrieveaCodiciAlbumPreferiti(username).contains(codice));
            request.setAttribute("canzoniAcquistate", new CanzoneDAO().doRetrieveCodiciCanzoniAcquistate(username));
            request.setAttribute("albumAcquistatato", new AlbumDAO().doRetrieveCodiciAlbumAcquistati(username).contains(codice));
            request.setAttribute("listPlaylist",new PlaylistDAO().doRetrievePlaylistByUtente(username));
        }

        request.setAttribute("album",album);
        request.setAttribute("braniRand", new CanzoneDAO().doRetrieveCanzoneRandom());
        request.getRequestDispatcher("WEB-INF/views/album/album.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
