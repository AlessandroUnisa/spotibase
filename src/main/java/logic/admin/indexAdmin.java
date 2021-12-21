package logic.admin;

import data.Abbonamento.AbbonamentoDAO;
import data.Album.Album;
import data.Album.AlbumDAO;
import data.Artista.Artista;
import data.Artista.ArtistaDAO;
import data.DAOCanzone.Canzone;
import data.DAOCanzone.CanzoneDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "indexAdmin", value = "/admin/dashboard")
public class indexAdmin extends HttpServlet {
    private void doGetElem(HttpServletRequest request) throws SQLException {
        CanzoneDAO canzoneDAO = new CanzoneDAO();
        AlbumDAO albumDAO = new AlbumDAO();
        AbbonamentoDAO abbonamentoDAO = new AbbonamentoDAO();
        ArtistaDAO artistaDAO = new ArtistaDAO();

        request.setAttribute("listaTopArtisti", artistaDAO.doRetrieveArtistPopular());
        request.setAttribute("listaTopBrani", canzoneDAO.doRetrieveCanzonePopular());
        request.setAttribute("listaTopCanzoniAcquistate", canzoneDAO.doRetrieveCanzoniTopBuy());
        request.setAttribute("listaTopAlbumAcquistati", albumDAO.doRetrieveAlbumTopBuy());
        request.setAttribute("listaAbbonamenti", abbonamentoDAO.doRetrieveAll());

        double guadagnoCanzoni = canzoneDAO.doRetrieveTotaleGuadagno();
        double guadagnoAlbum = albumDAO.doRetrieveTotaleGuadagno();
        double guadagnoAbbon = abbonamentoDAO.doRetrieveTotaleGuadagno();
        request.setAttribute("guadagnoCanzoni", guadagnoCanzoni);
        request.setAttribute("guadagnoAlbum", guadagnoAlbum);
        request.setAttribute("guadagnoAbbonamenti", guadagnoAbbon);
        request.setAttribute("guadagnoTot", guadagnoCanzoni + guadagnoAbbon + guadagnoAlbum);

        int numPageCanzoni = (int) Math.ceil((canzoneDAO.doRetrieveNumCanzoni() / 10.0));
        int numPageAlbum = (int) Math.ceil((albumDAO.doRetrieveNumAlbum() / 10.0));
        int numPageArtisti = (int) Math.ceil((artistaDAO.doRetrieveNumArtisti() / 10.0));
        request.setAttribute("numPageCanzoni", numPageCanzoni);
        request.setAttribute("numPageAlbum", numPageAlbum);
        request.setAttribute("numPageArtisti", numPageArtisti);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        if (request.getSession(false)==null || request.getSession(false).getAttribute("username")==null ||!request.getSession(false).getAttribute("username").equals("admin")){
            response.sendRedirect("../index.html");
        }else{
            try {
                doGetElem(request);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            RequestDispatcher dispatcher = request.getRequestDispatcher("../WEB-INF/views/crm/home.jsp");
            dispatcher.forward(request, response);
        }
    }

    private void doAggiornamentoElem(String cod, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SQLException {
        if(cod.charAt(0)=='C'){ //canzone
            Canzone canzone = new Canzone();
            canzone.setCodice(cod);
            canzone.setTitolo(request.getParameter("titolo"));
            canzone.setAnno(Integer.parseInt(request.getParameter("anno")));
            canzone.setDurata(Double.parseDouble(request.getParameter("durata")));
            canzone.setPrezzo(Double.parseDouble(request.getParameter("prezzo")));
            CanzoneDAO canzoneDAO= new CanzoneDAO();
            request.setAttribute("modifica",canzoneDAO.doUpdate(canzone));
            doGet(request,response);

        }else
        if(cod.charAt(0)=='A' && cod.length()>10){ //artista
            Artista artista= new Artista();
            artista.setCodFiscale(cod);
            artista.setNome(request.getParameter("nome"));
            artista.setCognome(request.getParameter("cognome"));
            artista.setNomeDArte(request.getParameter("nomeDArte"));
            ArtistaDAO artistaDAO= new ArtistaDAO();
            request.setAttribute("modifica",artistaDAO.doUpdate(artista));
            doGet(request,response);
        }else{
            Album album= new Album();
            album.setCodice(cod);
            album.setNome(request.getParameter("nome"));
            album.setAnno(Integer.parseInt(request.getParameter("anno")));
            album.setDescrizione(request.getParameter("descrizione"));
            album.setNumCanzoni(Integer.parseInt(request.getParameter("numCanzoni")));
            AlbumDAO albumDAO = new AlbumDAO();
            request.setAttribute("modifica",albumDAO.doUpdate(album));
            doGet(request,response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //aggiornamento elementi
        String cod= request.getParameter("codice");
        try {
            doAggiornamentoElem(cod, request,response);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
