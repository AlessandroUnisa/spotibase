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


@WebServlet(name = "ServletIndex", value = "/index.html")
public class ServletIndex extends HttpServlet {

    private void getElements(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        CanzoneAPI canzoni= new CanzoneDAO();
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

            request.setAttribute("listaCanzoniAcquistate", new AcquistoDAO().doRetrieveCodiciCanzoniAcquistate(username));

            request.setAttribute("listPlaylist", new PlaylistDAO().doRetrievePlaylistByUtente(username));
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        try {
            getElements(request,response);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        request.getRequestDispatcher("WEB-INF/views/utente/index.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
