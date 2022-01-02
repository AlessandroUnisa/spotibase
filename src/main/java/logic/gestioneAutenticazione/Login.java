package logic.gestioneAutenticazione;

import data.DAOUtente.Utente;
import data.DAOUtente.UtenteAPI;
import data.DAOUtente.UtenteDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@WebServlet(name = "Login", value = "/login")
public class Login extends HttpServlet {

    public void login(HttpServletRequest request, HttpServletResponse response,UtenteAPI utenteAPI) throws SQLException, NoSuchAlgorithmException, IOException, ServletException {
        Utente utenteTemp = new Utente();
        utenteTemp.setEmail(request.getParameter("email"));
        utenteTemp.setPassword(request.getParameter("passwd"));  //per criptare la password


            Utente utente = utenteAPI.doGet(utenteTemp.getEmail(), utenteTemp.getPassword());  //cerco l utente nel db
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


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String path = request.getPathInfo();
        if(request.getSession(true).getAttribute("isLogged")==null)  //se l'utente non ha fatto ancora la login
            request.getRequestDispatcher("/WEB-INF/views/utente/login.jsp").forward(request,response);
        else response.sendRedirect("./index.html");

    }

    @Override
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
