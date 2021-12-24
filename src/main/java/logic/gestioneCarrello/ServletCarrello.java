package logic.gestioneCarrello;

import data.Album.Album;
import data.Album.AlbumDAO;
import data.DAOAcquisto.AcquistoAPI;
import data.DAOAcquisto.AcquistoDAO;
import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneAPI;
import data.DAOCanzone.CanzoneDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

@WebServlet(name = "ServletCarrello", value = "/carrello")
public class ServletCarrello extends HttpServlet {

    private void visualizzaCarrello(HttpServletRequest request) throws SQLException {
        String codice = request.getParameter("del"); //viene settato se viene richiesta la cancellazione di un elemento
        HttpSession session = request.getSession(true);

        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());
        if (codice!=null)
            ((TreeSet<String>) session.getAttribute("listCart")).remove(codice);

        if(((TreeSet<String>)session.getAttribute("listCart")).size()>0){
            ArrayList<String> codici = new ArrayList<>((TreeSet<String>) session.getAttribute("listCart")); //codici presenti nel carrello
            CanzoneAPI canzoneDAO = new CanzoneDAO();
            AcquistoAPI acquistoDAO = new AcquistoDAO();
            AlbumDAO albumDAO = new AlbumDAO();
            String username = (String) request.getSession(false).getAttribute("username");

            ArrayList<Canzone> listaCanzoni = (ArrayList<Canzone>) canzoneDAO.doRetrieveCanzoniByCodiciWithArtisti(codici);
            ArrayList<Album> listAlbum = (ArrayList<Album>) albumDAO.doRetrieveAlbumsByCodiciWithArtisti(codici);

            ArrayList<String> listCanzoniAcquistate = null;

                listCanzoniAcquistate = (ArrayList<String>) acquistoDAO.doRetrieveCodiciCanzoniAcquistate(username);

            ArrayList<String> listAlbumAcquistati = (ArrayList<String>) albumDAO.doRetrieveCodiciAlbumAcquistati(username);

            //rimuovo le canzoni gia acquistate
            for (int i=0; i<listaCanzoni.size(); i++)
                if(listCanzoniAcquistate.contains(listaCanzoni.get(i).getCodice()))
                    ((TreeSet<String>) session.getAttribute("listCart")).remove(listaCanzoni.remove(i).getCodice());

            //rimuovo gli album gia acquistati
            for (int i=0; i<listAlbum.size(); i++)
                if(listAlbumAcquistati.contains(listAlbum.get(i).getCodice()))
                    ((TreeSet<String>) session.getAttribute("listCart")).remove(listAlbum.remove(i).getCodice());

            double totale = 0;
            for(Canzone canzone : listaCanzoni)
                totale+=canzone.getPrezzo();
            for (Album album : listAlbum)
                totale+=album.getPrezzo();

            request.setAttribute("totale", totale);
            request.setAttribute("canzoni", listaCanzoni);
            request.setAttribute("albums", listAlbum);
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //visualizzazione carrello
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        try {
            visualizzaCarrello(request);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        request.getRequestDispatcher("WEB-INF/views/carrello/carrello.jsp").forward(request,response);
    }

    private void acquisto(HttpServletRequest request) throws SQLException {
        if(request.getSession(true).getAttribute("isLogged")!=null) {
            TreeSet<String> codici = (TreeSet<String>) request.getSession(false).getAttribute("listCart");
            Iterator iterator = codici.iterator();
            CanzoneAPI canzoneDAO = new CanzoneDAO();
            AcquistoAPI acquistoDAO = new AcquistoDAO();
            AlbumDAO albumDAO = new AlbumDAO();
            String username = (String) request.getSession(false).getAttribute("username");
            while (iterator.hasNext()) {
                String item = (String) iterator.next();
                if (item.charAt(0) == 'C') {
                    try {
                        acquistoDAO.doInsertCanzoneAcquistata(username, item);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                else albumDAO.doInsertAlbumAcquistato(username, item);
            }

            ((TreeSet<String>) request.getSession(false).getAttribute("listCart")).clear();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //acquisto
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        try {
            acquisto(request);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        response.sendRedirect("libreria");
    }
}
