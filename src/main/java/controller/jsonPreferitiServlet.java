package controller;

import model.Album.AlbumDAO;
import model.Artista.Artista;
import model.Artista.ArtistaDAO;
import model.Canzone.CanzoneDAO;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "jsonPreferitiServlet", value = "/jsonPreferitiServlet")
public class jsonPreferitiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //setta la preferenza per canzone, album e artista
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String codice = request.getParameter("cod");
        int tipo = Integer.parseInt(request.getParameter("tipo"));  //1-->canzone, 2-->artista, 3-->album
        String username = (String) request.getSession(false).getAttribute("username");
        JSONObject obj = new JSONObject();


        if(tipo==1){//canzone
            CanzoneDAO canzoneDAO = new CanzoneDAO();
            if(canzoneDAO.doRetrieveaCodiciCanzoniPreferite(username).contains(codice)){
                obj.put("flag",canzoneDAO.doRemovePreferenza(codice,username));
                obj.put("action", "rimossa");
            }else{
                obj.put("flag",canzoneDAO.doInsertPreferenza(codice,username));
                obj.put("action", "aggiunta");
            }
        }else if(tipo==2){//artista, in questo caso codice = nomeDArte
            codice = codice.replace('-',' '); //per i gruppi con piu nomi
            ArtistaDAO artistaDAO = new ArtistaDAO();
            ArrayList<Artista> listPref = (ArrayList<Artista>) artistaDAO.doRetrieveArtistiPreferiti(username);
            ArrayList<String> codiciFisc = new ArrayList<>();
            boolean flag=true;
            for(Artista item : listPref){ //vedo se l artista è tra i preferiti e recupero i cod fiscali
                if(item.getNomeDArte().equals(codice)){
                    codiciFisc.add(item.getCodFiscale());
                    flag=false;
                }
            }
            if(!flag)
                obj.put("flag",artistaDAO.doRemovePreferenza(username,codiciFisc));
            if(flag){ //se non è tra i preferiti
                ArrayList<String> codFiscali = new ArrayList<>();
                for(Artista item : artistaDAO.doRetrieveArtistaByNomeArte(codice)) //prendo i codFiscali associati a quel nome d arte (es Queen)
                    codFiscali.add(item.getCodFiscale());

                obj.put("flag", artistaDAO.doInsertPreferenza(username,codFiscali));
            }

        }else if(tipo==3){//album
            AlbumDAO albumDAO = new AlbumDAO();
            if(albumDAO.doRetrieveaCodiciAlbumPreferiti(username).contains(codice))
                obj.put("flag", albumDAO.doRemovePreferenza(codice,username));
            else obj.put("flag", albumDAO.doInsertPreferenza(codice,username));
        }
        response.getWriter().println(obj);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
