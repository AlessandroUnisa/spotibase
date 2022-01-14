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

/**Questa classe viene utilizzata per aggiungere o rimuovere una preferenza
 * @version 1.0
 * @see HttpServlet fornisce l'interfaccia per creare una servlet
 */
@WebServlet(name = "jsonPreferitiServlet", value = "/jsonPreferitiServlet")
public class JsonPreferitiServlet extends HttpServlet {

    /** Questo metodo si occupa di settare la preferenza per una canzone e anche rimuoverla nel caso già ci sia
     * @param username username dell'utente che ha espresso la preferenza alla canzone
     * @param codice codice della canzone
     * @param preferenzaAPI viene utilizzato per verificare se per quella canzone è stata già espressa la preferenza
     * @param obj oggetto json per aggiornare in tempo reale l'icona per notificare la presenza o assenza della canzone
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws OggettoGiaPresenteException Un'eccezine che viene lancita quando l'oggetto già è presente
     * @throws IllegalArgumentException  Un'eccezione che viene lanciata quando la preferenza è null o non valida
     * @throws OggettoNonInseritoException Un'eccezione che viene lanciata quando l'oggetto preferenza non è stato inserito
     * @throws OggettoNonCancellatoException Un'eccezione che viene lanciata quando l'oggetto non può essere cancellato
     * @throws OggettoNonTrovatoException Un'eccezione che viene lanciata quando l'acquisto non è stato trovato nel db
     */
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

    /** Questo metodo viene utilizzato per prendere i parametri dalla request e capire che tipo di preferenza è stata espressa, se per una canzone o album o artista
     * @param request viene utilizzata per prendere i parametri tipo della preferenza e il codice della canzone e anche dalla session l'username dell'utente
     * @param response viene utilizzata per inoltrare l'oggetto json
     * @throws ServletException Un'eccezione lanciata quando c'è un problema nella servlet
     * @throws IOException Un' eccezione che viene lanciata quando c'è un errore di I/O
     */
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
