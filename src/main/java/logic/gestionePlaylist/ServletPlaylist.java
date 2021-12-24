package logic.gestionePlaylist;

import data.Artista.ArtistaDAO;
import data.Attivazione.Attivazione;
import data.Attivazione.AttivazioneDAO;
import data.DAOAcquisto.AcquistoDAO;
import data.DAOCanzone.CanzoneDAO;
import data.DAOPlaylist.Playlist;
import data.DAOPlaylist.PlaylistAPI;
import data.DAOPlaylist.PlaylistDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.TreeSet;

@WebServlet(name = "ServletPlaylist", value = "/playlist/*")
public class ServletPlaylist extends HttpServlet {

    private Playlist showPlaylist(HttpServletRequest request) throws SQLException {
        String nomePlaylist = request.getParameter("name");
        String username = (String) request.getSession(true).getAttribute("username");
        PlaylistAPI playlistDAO = new PlaylistDAO();
        String codiceDel = request.getParameter("del"); //cancellazione canzone
        if(codiceDel!=null)
            playlistDAO.doRemoveSong(nomePlaylist,username,codiceDel); //questo non sta nell'interfaccia

        HttpSession session = request.getSession(true);
        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());

        if(session.getAttribute("isLogged")!=null){

            request.setAttribute("listaPreferiti",new CanzoneDAO().doRetrieveaCodiciCanzoniPreferite(username));
            try {
                request.setAttribute("listaCanzoniAcquistate", new AcquistoDAO().doRetrieveCodiciCanzoniAcquistate(username));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            request.setAttribute("listPlaylist",new PlaylistDAO().doRetrievePlaylistByUtente(username));

        }



        Playlist playlist = playlistDAO.doRetrievePlaylistWithSongs(username,nomePlaylist);
        ArtistaDAO artistaDAO = new ArtistaDAO();
        //prendo gli artisti di ogni canzone
        playlist.getCanzoni().forEach(item-> {
            try {
                item.setArtisti(artistaDAO.doRetrieveArtistiBySong(item.getCodice()));
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
        return playlist;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //mostra la playlist
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");


        try {
            request.setAttribute("playlist", showPlaylist(request));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        request.getRequestDispatcher("WEB-INF/views/playlist/playlist.jsp").forward(request,response);
    }


    private void cancellaPlaylist(HttpServletRequest request) throws SQLException {
        String username = (String) request.getSession(false).getAttribute("username");
        String titolo = request.getParameter("delete");
        PlaylistAPI playlistDAO = new PlaylistDAO();
        playlistDAO.doDelete(titolo+";"+username);
    }

    private void creaPlaylist(HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException {
        String username = (String) request.getSession(false).getAttribute("username");
        String titolo = request.getParameter("titolo");
        AttivazioneDAO attivazioneDAO = new AttivazioneDAO();
        int numPlaylist = new PlaylistDAO().doRetrieveNumPlaylistOfUtente(username);
        PlaylistAPI playlistDAO = new PlaylistDAO();

        if(playlistDAO.isPresent(titolo,username)) //titolo gia presente
            response.sendRedirect("../libreria?presentPlay=1");
        else {
            boolean flag = false;
            if (numPlaylist > 3) {
                for (Attivazione attivazione : attivazioneDAO.doRetrieveAttivazioniAttiveByUtente(username)) {
                    if (attivazione.getAbbonamento().getNome().equals("plus") && numPlaylist + 1 < 15) {
                        flag = true;
                        break;
                    } else if (attivazione.getAbbonamento().getNome().equals("mega") && numPlaylist + 1 < 45) {
                        flag = true;
                        break;
                    } else if (attivazione.getAbbonamento().getNome().equals("ultra")) {
                        flag = true;
                        break;
                    }
                }
            } else flag = true;
            System.out.println(numPlaylist);
            System.out.println(flag);
            if (!flag) //num max raggiunto
                response.sendRedirect("../libreria?errInsPlay=1");
            else {
                String note = request.getParameter("note");

                Playlist playlist = new Playlist();
                playlist.setTitolo(titolo);
                playlist.setNote(note);
                playlist.setUsername(username);
                playlistDAO.doSave(playlist);
                response.sendRedirect("../libreria");
            }


        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        switch (path){
            case "/cancella": {
                //cancellazione playlist
                try {
                    cancellaPlaylist(request);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                response.sendRedirect("../libreria");
                break;
            }
            case "/crea": {
                //inserimento playlist. senza abbonamento max 4, plus-->15, mega-->45, ultra illimitate
                try {
                    creaPlaylist(request,response);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
                }
            }
        }

    }

