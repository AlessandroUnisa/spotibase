package controller;

import model.Artista.ArtistaDAO;
import model.Canzone.CanzoneDAO;
import model.Playlist.PlaylistDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.Collections;
import java.util.TreeSet;

@WebServlet(name = "ServletArtista", value = "/artista")
public class ServletArtisti extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String nome= request.getParameter("nomeDArte");
        HttpSession session = request.getSession(true);
        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());

        if(session.getAttribute("isLogged")!=null){
            String username = (String)session.getAttribute("username");
            request.setAttribute("listaPreferiti",new CanzoneDAO().doRetrieveaCodiciCanzoniPreferite(username));
            request.setAttribute("listaCanzoniAcquistate", new CanzoneDAO().doRetrieveCodiciCanzoniAcquistate(username));
            request.setAttribute("listPlaylist", new PlaylistDAO().doRetrievePlaylistByUtente(username));
            request.setAttribute("flagPref", new ArtistaDAO().doRetrieveNomiArteArtistiPreferiti(username).contains(nome));
        }

        CanzoneDAO canzoni = new CanzoneDAO();
        request.setAttribute("pathImg",new ArtistaDAO().doRetrieveArtistaByNomeArte(nome).get(0).getPathImg());
        request.setAttribute("canzoni",canzoni.doRetrieveSongsByArtista(nome));
        request.getRequestDispatcher("WEB-INF/views/artista/artista.jsp").forward(request,response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
