package controller.carrello;

import model.Album.Album;
import model.Album.AlbumDAO;
import model.Canzone.Canzone;
import model.Canzone.CanzoneDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

@WebServlet(name = "ServletCarrello", value = "/carrello")
public class ServletCarrello extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //visualizzazione carrello
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String codice = request.getParameter("del"); //viene settato se viene richiesta la cancellazione di un elemento
        HttpSession session = request.getSession(true);

        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());
        if (codice!=null)
            ((TreeSet<String>) session.getAttribute("listCart")).remove(codice);

        if(((TreeSet<String>)session.getAttribute("listCart")).size()>0){
            ArrayList<String> codici = new ArrayList<>((TreeSet<String>) session.getAttribute("listCart")); //codici presenti nel carrello
            CanzoneDAO canzoneDAO = new CanzoneDAO();
            AlbumDAO albumDAO = new AlbumDAO();
            String username = (String) request.getSession(false).getAttribute("username");

            ArrayList<Canzone> listaCanzoni = (ArrayList<Canzone>) canzoneDAO.doRetrieveCanzoniByCodiciWithArtisti(codici);
            ArrayList<Album> listAlbum = (ArrayList<Album>) albumDAO.doRetrieveAlbumsByCodiciWithArtisti(codici);

            ArrayList<String> listCanzoniAcquistate = (ArrayList<String>) canzoneDAO.doRetrieveCodiciCanzoniAcquistate(username);
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

        request.getRequestDispatcher("WEB-INF/views/carrello/carrello.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //acquisto
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        if(request.getSession(true).getAttribute("isLogged")!=null) {
            TreeSet<String> codici = (TreeSet<String>) request.getSession(false).getAttribute("listCart");
            Iterator iterator = codici.iterator();
            CanzoneDAO canzoneDAO = new CanzoneDAO();
            AlbumDAO albumDAO = new AlbumDAO();
            String username = (String) request.getSession(false).getAttribute("username");
            while (iterator.hasNext()) {
                String item = (String) iterator.next();
                if (item.charAt(0) == 'C')
                    canzoneDAO.doInsertCanzoneAcquistata(username, item);
                else albumDAO.doInsertAlbumAcquistato(username, item);
            }

            ((TreeSet<String>) request.getSession(false).getAttribute("listCart")).clear();
        }

       response.sendRedirect("libreria");
    }
}
