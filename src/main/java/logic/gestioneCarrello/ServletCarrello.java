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

/**Questa classe permette di compiere operazioni sul carrello
 * @see HttpServlet fornisce l'interfaccia per creare una servlet
 * @version 1.0
 */
@WebServlet(name = "ServletCarrello", value = "/carrello")
public class ServletCarrello extends HttpServlet {

    /**
     * Il metodo permette di visualizzare il carrello con il totale, le canzoni e album inseriti
     * @param request oggetto della servlet che permette di prelevare la sessione e settare totale, canzoni e album del
     *                carrello
     * @param canzoneAPI interfaccia di CanzoneDAO
     * @param acquistoAPI interfaccia di AcquistoDAO
     * @param albumDAO interfaccia di AlbumDAO
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     */
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

    /**
     * Il metodo consente di eliminare un oggetto dal carrello, identificato da un codice
     * @param request oggetto della servlet che consente di prelevare il codice della canzone da eliminare e la
     *                sessione corrente
     */
    @Generated
    public void eliminaDalCarrello(HttpServletRequest request){
        String codice = request.getParameter("del"); //viene settato se viene richiesta la cancellazione di un elemento
        if (codice!=null){
            HttpSession session = request.getSession(true);
            ((TreeSet<String>) session.getAttribute("listCart")).remove(codice);
        }
    }

    /**
     * Il metodo ereditato dalla classe HttpServlet che esplicita i parametri della request e permette di visualizzare
     * il carrello
     * @param request oggetto della servlet che permette di settare la codifica dei caratteri
     * @param response oggetto della servlet che contiene la risposta
     * @throws ServletException Un'eccezione lanciata quando si verifica un errore nella servlet
     * @throws IOException Un'eccezione lanciata quando si verifica un errore I/O
     */
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

    /**
     * Il metodo consente di effettuare un acquisto
     * @param request oggetto della servlet che preleva la sessione , la username e l'oggetto carrello per eliminarne
     *                gli elementi
     * @param acquistoAPI interfaccia Dao di Acquisto
     * @param albumDAO interfaccia Dao di Album
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori
     */
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

    /**
     * Il metodo ereditato dalla classe HttpServlet che chiama il metodo acquisto
     * @param request oggetto della servlet permette di settare la codifica dei caratteri della richiesta
     * @param response oggetto della servlet che permette di settare la codifica dei caratteri della risposta
     * @throws ServletException Un'eccezione lanciata quando si verifica un errore nella servlet
     * @throws IOException Un'eccezione lanciata quando si verifica un errore I/O
     */
    @Override
    @Generated
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //acquisto
        request.setCharacterEncoding("utf-8");
      //response.setCharacterEncoding("utf-8");
        try {
            AcquistoAPI acquistoAPI = new AcquistoDAO();
            acquisto(request,acquistoAPI,new AlbumDAO());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        response.sendRedirect("libreria");
    }
}
