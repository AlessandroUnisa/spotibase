package controller.playlist;

import model.Artista.ArtistaDAO;
import model.Attivazione.Attivazione;
import model.Attivazione.AttivazioneDAO;
import model.Canzone.CanzoneDAO;
import model.Playlist.Playlist;
import model.Playlist.PlaylistDAO;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.TreeSet;

@WebServlet(name = "ServletPlaylist", value = "/playlist/*")
public class ServletPlaylist extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //mostra la playlist
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String nomePlaylist = request.getParameter("name");
        String username = (String) request.getSession(true).getAttribute("username");
        PlaylistDAO playlistDAO = new PlaylistDAO();
        String codiceDel = request.getParameter("del"); //cancellazione canzone
        if(codiceDel!=null)
            playlistDAO.doRemoveSong(nomePlaylist,username,codiceDel);

        HttpSession session = request.getSession(true);
        if(session.getAttribute("listCart")==null)
            session.setAttribute("listCart", new TreeSet<String>());

        if(session.getAttribute("isLogged")!=null){
            request.setAttribute("listaPreferiti",new CanzoneDAO().doRetrieveaCodiciCanzoniPreferite(username));
            request.setAttribute("listaCanzoniAcquistate", new CanzoneDAO().doRetrieveCodiciCanzoniAcquistate(username));
            request.setAttribute("listPlaylist",new PlaylistDAO().doRetrievePlaylistByUtente(username));

        }



        Playlist playlist = playlistDAO.doRetrievePlaylistWithSongs(username,nomePlaylist);
        ArtistaDAO artistaDAO = new ArtistaDAO();
        //prendo gli artisti di ogni canzone
        playlist.getCanzoni().forEach(item->item.setArtisti(artistaDAO.doRetrieveArtistiBySong(item.getCodice())));

        request.setAttribute("playlist", playlist);
        request.getRequestDispatcher("WEB-INF/views/playlist/playlist.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        switch (path){
            case "/cancella": {
                //cancellazione playlist
                String username = (String) request.getSession(false).getAttribute("username");
                String titolo = request.getParameter("delete");
                PlaylistDAO playlistDAO = new PlaylistDAO();
                playlistDAO.doDelete(username, titolo);
                response.sendRedirect("../libreria");
                break;
            }
            case "/crea": {
                //inserimento playlist. senza abbonamento max 4, plus-->15, mega-->45, ultra illimitate
                String username = (String) request.getSession(false).getAttribute("username");
                String titolo = request.getParameter("titolo");
                AttivazioneDAO attivazioneDAO = new AttivazioneDAO();
                int numPlaylist = new PlaylistDAO().doRetrieveNumPlaylistOfUtente(username);
                PlaylistDAO playlistDAO = new PlaylistDAO();

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
                        playlistDAO.doInsertPlaylist(playlist);
                        response.sendRedirect("../libreria");
                    }
                    break;
                }
            }
        }

    }
}
