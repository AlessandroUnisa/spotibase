package controller;

import model.Album.AlbumDAO;
import model.Canzone.CanzoneDAO;
import model.Playlist.Playlist;
import model.Playlist.PlaylistDAO;
import model.Utente.Utente;
import model.Utente.UtenteDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

@WebServlet(name = "ServletTest", value = "/libreria")
public class libreriaServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //mostra la libreria
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");

        //per l alert max num playlist creabili
        String errInsPlay = request.getParameter("errInsPlay");//riceve errInsPlay dal get
        if (errInsPlay!=null)
            request.setAttribute("errInsPlay","Ops, hai raggiunto il numero massimo di playlist creabili. Acquista un abbonamento maggiore.");

        //quando l utente inserisce un titolo di playlist gia presente
        String errTitolo = request.getParameter("presentPlay");
        if(errTitolo!=null)
            request.setAttribute("errTitolo", "Playlist gia presente");

        String username = (String) request.getSession(false).getAttribute("username");
        Random random = new Random();
        ArrayList<Integer> numImgPlaylist = new ArrayList<>();  //contiene num random per sfondo card playlist
        CanzoneDAO canzoneDAO = new CanzoneDAO();

        Utente utente = new UtenteDAO().fetchUtenteWithSongsAlbumArtistiPrefPlayAbbon(username); //recupera i preferiti, playlist e abbonamenti
        for(int i=0; i<utente.getPlaylists().size(); i++)
            numImgPlaylist.add(1+random.nextInt(10));

        request.setAttribute("canzoniAcquistate", canzoneDAO.doRetrieveCanzoniAcquistate(username));
        request.setAttribute("albumAcquistati", new AlbumDAO().doRetrieveAlbumAcquistati(username));
        request.setAttribute("listaCanzoniAcquistate", canzoneDAO.doRetrieveCodiciCanzoniAcquistate(username));
        request.setAttribute("listaPreferiti", canzoneDAO.doRetrieveaCodiciCanzoniPreferite(username));
        request.setAttribute("numImgPlaylist", numImgPlaylist);
        request.setAttribute("utente",utente);
        request.setAttribute("listPlaylist", utente.getPlaylists());
        request.setAttribute("dataOggi", LocalDate.now()); //per vedere se un abbonamento è attivo oppure no
        request.getRequestDispatcher("WEB-INF/views/utente/libreria.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
