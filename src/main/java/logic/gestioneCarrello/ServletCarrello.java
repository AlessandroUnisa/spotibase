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
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@interface Generated {
}
@WebServlet(name = "ServletCarrello", value = "/carrello")
public class ServletCarrello extends HttpServlet {


    public void visualizzaCarrello(HttpServletRequest request, CanzoneAPI canzoneAPI, AcquistoAPI acquistoAPI, AlbumDAO albumDAO) throws SQLException {
        HttpSession session = request.getSession(true);

        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());

        if(((TreeSet<String>)session.getAttribute("listCart")).size()>0){
            ArrayList<String> codici = new ArrayList<>((TreeSet<String>) session.getAttribute("listCart")); //codici presenti nel carrello
            String username = (String) request.getSession(false).getAttribute("username");

            ArrayList<Canzone> listaCanzoni = (ArrayList<Canzone>) canzoneAPI.doRetrieveCanzoniByCodiciWithArtisti(codici);
            ArrayList<Album> listAlbum = (ArrayList<Album>) albumDAO.doRetrieveAlbumsByCodiciWithArtisti(codici);

            ArrayList<String> listCanzoniAcquistate = (ArrayList<String>) acquistoAPI.doRetrieveCodiciCanzoniAcquistate(username);

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

    @Generated
    public void eliminaDalCarrello(HttpServletRequest request){
        String codice = request.getParameter("del"); //viene settato se viene richiesta la cancellazione di un elemento
        if (codice!=null){
            HttpSession session = request.getSession(true);
            ((TreeSet<String>) session.getAttribute("listCart")).remove(codice);
        }
    }

    @Override
    @Generated
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //visualizzazione carrello
        request.setCharacterEncoding("utf-8");
       // response.setCharacterEncoding("utf-8");

        eliminaDalCarrello(request);

        try {
            AcquistoAPI acquistoAPI = new AcquistoDAO();
            CanzoneAPI canzoneAPI = new CanzoneDAO();
            visualizzaCarrello(request,canzoneAPI,acquistoAPI, new AlbumDAO());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        request.getRequestDispatcher("WEB-INF/views/carrello/carrello.jsp").forward(request,response);
    }

    public void acquisto(HttpServletRequest request, AcquistoAPI acquistoAPI, AlbumDAO albumDAO) throws SQLException {
        if(request.getSession(true).getAttribute("isLogged")!=null) {
            TreeSet<String> codici = (TreeSet<String>) request.getSession(false).getAttribute("listCart");
            Iterator iterator = codici.iterator();

            String username = (String) request.getSession(false).getAttribute("username");
            while (iterator.hasNext()) {
                String item = (String) iterator.next();
                if (item.charAt(0) == 'C') {
                        acquistoAPI.doInsertCanzoneAcquistata(username, item);
                }
                else albumDAO.doInsertAlbumAcquistato(username, item);
            }

            ((TreeSet<String>) request.getSession(false).getAttribute("listCart")).clear();
        }

    }

    @Override
    @Generated
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //acquisto
        request.setCharacterEncoding("utf-8");
      //  response.setCharacterEncoding("utf-8");
        try {
            AcquistoAPI acquistoAPI = new AcquistoDAO();
            acquisto(request,acquistoAPI,new AlbumDAO());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        response.sendRedirect("libreria");
    }
}
