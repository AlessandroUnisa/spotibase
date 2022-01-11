package logic.gestionePreferenza;

import data.Album.AlbumDAO;
import data.Artista.Artista;
import data.Artista.ArtistaDAO;
import data.DAOPreferenza.Preferenza;
import data.DAOPreferenza.PreferenzaAPI;
import data.DAOPreferenza.PreferenzaDAO;
import data.Exceptions.OggettoGiaPresenteException;
import data.Exceptions.OggettoNonCancellatoException;
import data.Exceptions.OggettoNonInseritoException;
import data.Exceptions.OggettoNonTrovatoException;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({TYPE, METHOD})
@interface Generated {
}

@WebServlet(name = "jsonPreferitiServlet", value = "/jsonPreferitiServlet")
public class JsonPreferitiServlet extends HttpServlet {

    public void setPreferenzaCanzone(String username, String codice,PreferenzaAPI preferenzaAPI, JSONObject obj) throws SQLException {

        System.out.println(username);
        System.out.println(codice);
        System.out.println(preferenzaAPI.doRetrieveCodiciCanzoniPreferite(username));
        if(!preferenzaAPI.doRetrieveCodiciCanzoniPreferite(username).contains(codice)){
            try {
                preferenzaAPI.doSave(new Preferenza(codice,username));
                obj.put("flag",true);
            } catch (OggettoGiaPresenteException | IllegalArgumentException | OggettoNonInseritoException e) {
               obj.put("flag",false);
            }
            obj.put("action", "rimossa");
        }else{
            try {
                preferenzaAPI.doDelete(codice+";"+username);
                obj.put("flag",true);
            } catch (OggettoNonCancellatoException | IllegalArgumentException | OggettoNonTrovatoException e) {
                obj.put("flag",false);
            }
            obj.put("action", "aggiunta");
        }
    }
@Generated
    private void setPreferenzaArtista(String username, String codice, JSONObject obj) throws SQLException {
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

    }
@Generated
    private void setPreferenzaAlbum(String username, String codice, JSONObject obj) throws SQLException {
        AlbumDAO albumDAO = new AlbumDAO();
        if(albumDAO.doRetrieveaCodiciAlbumPreferiti(username).contains(codice))
            obj.put("flag", albumDAO.doRemovePreferenza(codice,username));
        else obj.put("flag", albumDAO.doInsertPreferenza(codice,username));
    }

    @Override
    @Generated
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //setta la preferenza per canzone, album e artista
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String codice = request.getParameter("cod");
        int tipo = Integer.parseInt(request.getParameter("tipo"));  //1-->canzone, 2-->artista, 3-->album
        String username = (String) request.getSession(false).getAttribute("username");


        JSONObject obj = new JSONObject();
        if(tipo==1){//canzone
            try {
                PreferenzaAPI preferenzaAPI=new PreferenzaDAO();
                setPreferenzaCanzone(username,codice,preferenzaAPI,obj);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(tipo==2){//artista, in questo caso codice = nomeDArte
            try {
                setPreferenzaArtista(username,codice,obj);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }else if(tipo==3){//album
            try {
                setPreferenzaAlbum(username,codice,obj);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        response.getWriter().println(obj.toString());
    }


    @Override
    @Generated
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
