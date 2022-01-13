package logic.gestioneAutenticazione;

import data.DAOUtente.Utente;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;
import data.Exceptions.OggettoNonTrovatoException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

/**Questa classe permette di effettuare l'accesso alla piattaforma
 * @see HttpServlet fornisce l'interfaccia per creare una servlet
 * @version 1.0
 */

@WebServlet(name = "Login", value = "/login")
public class Login extends HttpServlet {

    /**
     * Il metodo consente all'utente di accedere alla home della piattaforma nel caso sia gia registrato
     * @param request oggetto della servlet, che contiene i parametri email e password dell'utente, la sessione corrente
     * @param response oggetto della servlet, che contiene i parametri della risposta
     * @param utenteAPI interfaccia  di utenteDAO
     * @throws SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws NoSuchAlgorithmException Un'eccezione lanciata quando è richiesto un particolare algoritmo ma che non e disponibile
     * @throws IOException Un'eccezione lanciata quando si verifica un errore I/O
     * @throws ServletException Un'eccezione lanciata quando si verifica un errore nella servlet
     */
    public void login(HttpServletRequest request, HttpServletResponse response,UtenteAPI utenteAPI) throws SQLException, NoSuchAlgorithmException, IOException, ServletException {
        Utente utenteTemp = new Utente();
        utenteTemp.setEmail(request.getParameter("email"));
        utenteTemp.setPassword(request.getParameter("passwd"));  //per criptare la password

        Utente utente;
        try{
            utente = utenteAPI.doGet(utenteTemp.getEmail(), utenteTemp.getPassword());  //cerco l utente nel db
        }catch (OggettoNonTrovatoException e){
            utente=null;
        }

        if (utente != null && utenteAPI.isAdminEmail(utente.getEmail())) {  //amministratore
            HttpSession session = request.getSession(true);
            session.setAttribute("isLogged", true);
            session.setAttribute("username", "admin");
            response.sendRedirect("./admin/dashboard");
        } else if (utente != null ) {  //utente
            HttpSession session = request.getSession(true);
            session.setAttribute("isLogged", true);
            session.setAttribute("username", utente.getUsername());
            response.sendRedirect("./index.html");
            //sendRedirect invece del forward perche poi il browser fa request sbagliati
        } else {
            request.setAttribute("errCredenziali", "Credenziali errate");
            request.getRequestDispatcher("./WEB-INF/views/utente/login.jsp").forward(request, response);
        }

    }

    /**
     * Il metodo ereditato dalla classe HttpServlet che esplicita i parametri della request e permette di accedere
     * alla pagina di login nel caso in cui l'utente non sia loggato, altrimenti alla home
     * @param request oggetto della servlet che permette di settare la codifica dei caratteri della richiesta, verificare
     *                che l'utente sia loggato
     * @param response oggetto della servlet che permette di settare la codifica dei caratteri della risposta
     * @throws ServletException Un'eccezione lanciata quando si verifica un errore nella servlet
     * @throws IOException Un'eccezione lanciata quando si verifica un errore I/O
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String path = request.getPathInfo();
        if(request.getSession(true).getAttribute("isLogged")==null)  //se l'utente non ha fatto ancora la login
            request.getRequestDispatcher("/WEB-INF/views/utente/login.jsp").forward(request,response);
        else response.sendRedirect("./index.html");

    }

    /**
     * Il metodo ereditato dalla classe HttpServlet che chiama il metodo login
     * @param request oggetto della servlet permette di settare la codifica dei caratteri della richiesta
     * @param response oggetto della servlet che permette di settare la codifica dei caratteri della risposta
     * @throws ServletException Un'eccezione lanciata quando si verifica un errore nella servlet
     * @throws IOException Un'eccezione lanciata quando si verifica un errore I/O
     * @throws  SQLException Un'eccezione che fornisce informazioni su un errore di accesso al database o altri errori.
     * @throws NoSuchAlgorithmException Un'eccezione lanciata quando è richiesto un particolare algoritmo ma che non e disponibile
     */
    @Override
    @Generated
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        UtenteAPI utenteAPI = new UtenteDAO();
        String path = request.getPathInfo();
        try {
            login(request,response,utenteAPI);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
