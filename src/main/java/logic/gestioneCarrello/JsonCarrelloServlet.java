package logic.gestioneCarrello;

import org.json.simple.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.TreeSet;

/**
 * Questa classe modella l'interfaccia del carrello, quando si aggiunge un elemento al carrello
 * Inoltre aggiunge il codice della canzone al carrello
 * @see HttpServlet fornisce l'interfaccia per creare una servlet
 * @version 1.0
 */
@WebServlet(name = "jsonCarrelloServlet", value = "/jsonCarrelloServlet")
public class JsonCarrelloServlet extends HttpServlet {

    /**
     * Il metodo aggiunge una canzone al carrello e modifica il badge del carrello
     * @param request oggetto della servlet da cui preleviamo codice canzone e sessione corrente
     * @return JSONObject che rappresenta il carrello
     */
    private JSONObject aggiungi(HttpServletRequest request){
        String codice = request.getParameter("cod");
        HttpSession session = request.getSession(false);
        JSONObject object = new JSONObject();

        TreeSet<String> codici = (TreeSet<String>) session.getAttribute("listCart");
        object.put("flag", codici.add(codice));
        object.put("numElements", codici.size());  //per aggiornare i badgeCart
        return object;
    }

    /**
     * Il metodo ereditato dalla classe HttpServlet che esplicita i parametri della request e che inserisce gli elementi
     * nel carrello
     * @param request permette di settare la codifica dei caratteri della richiesta
     * @param response permette di settare la codifica dei caratteri della risposta e l'oggetto json
     * @throws ServletException Un'eccezione lanciata quando si verifica un errore nella servlet
     * @throws IOException Un'eccezione lanciata quando si verifica un errore I/O
     */
    @Override
    @Generated
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //inserimento elementi nel carrello
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");
        response.getWriter().println(aggiungi(request));
    }

    @Override
    @Generated
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
