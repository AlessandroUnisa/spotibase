package controller;

import model.Utente.Utente;
import model.Utente.UtenteDAO;
import model.Utente.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;

@WebServlet(value = "/account/*")
public class LoginSignUpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String path = request.getPathInfo();
        switch (path) {
            case "/login":
                if(request.getSession(true).getAttribute("isLogged")==null)  //se l'utente non ha fatto ancora la login
                    request.getRequestDispatcher("../WEB-INF/views/utente/login.jsp").forward(request,response);
                else response.sendRedirect("../index.html");
            break;

            case "/register":
                if(request.getSession(true).getAttribute("isLogged")==null)
                    request.getRequestDispatcher("../WEB-INF/views/utente/register.jsp").forward(request, response);
                else response.sendRedirect("../index.html");
            break;

            case "/logout":
                HttpSession session = request.getSession(false);
                session.invalidate();
                response.sendRedirect("../index.html");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String path = request.getPathInfo();
        try {
            switch (path) {
                case "/login":{

                    Utente utenteTemp = new Utente();
                    utenteTemp.setEmail(request.getParameter("email"));
                    utenteTemp.setPassword(request.getParameter("passwd"));  //per criptare la password
                    UtenteDAO utenteDAO = new UtenteDAO();
                    Validator validator = new Validator();

                    Utente utente = utenteDAO.findUser(utenteTemp.getEmail(),utenteTemp.getPassword());  //cerco l utente nel db
                    if(utente!=null && validator.isAdminEmail(utente.getEmail())){  //amministratore
                        HttpSession session = request.getSession(true);
                        session.setAttribute("isLogged",true);
                        session.setAttribute("username","admin");
                        response.sendRedirect("../admin/dashboard");
                    }else if(utente!=null){  //utente
                        HttpSession session = request.getSession(true);
                        session.setAttribute("isLogged",true);
                        session.setAttribute("username",utente.getUsername());
                        response.sendRedirect("../index.html");
                     //sendRedirect invece del forward perche poi il browser fa request sbagliati
                    }else{
                        request.setAttribute("errCredenziali","Credenziali errate");
                        request.getRequestDispatcher("../WEB-INF/views/utente/login.jsp").forward(request,response);
                    }
                }
                break;

                case "/register":
                    String username = request.getParameter("username");
                    String email = request.getParameter("email");
                    String passwd = request.getParameter("passwd");
                    String passwdCheck = request.getParameter("passwdCheck");

                    Validator validator = new Validator();
                    UtenteDAO utenteDAO = new UtenteDAO();
                    int flag=0;
                    //errori: username gia presente, email non valida, passwd non uguali, passwd non valida
                    //se l email non è valida oppure ci sono gia utenti con quella mail (la lista ne conterra al massimo uno)
                    if(!validator.isValidEmail(email) || utenteDAO.findUsers("email",email).size()!=0){ //check mail
                        request.setAttribute("errEmail","L'email inserita non è valida");
                        flag++;
                    }
                    if(!passwd.equals(passwdCheck)){ //check passwd uguali
                        request.setAttribute("errPasswdNE","Le password non coincidono");
                        flag++;
                    }
                    if(!validator.isValidPasswd(passwd)){  //check password
                        request.setAttribute("errPasswd","Password non valida");
                        flag++;
                    }
                    if(utenteDAO.findUsers("username",username).size()!=0){ //check username
                        request.setAttribute("errUsername","Username già presente");
                        flag++;
                    }

                    if(flag>0){  //setto i campi che aveva gia inserito
                       request.setAttribute("username",username);
                       request.setAttribute("email",email);
                       request.setAttribute("passwd",passwd);
                       request.setAttribute("passwdCheck",passwdCheck);
                       request.getRequestDispatcher("../WEB-INF/views/utente/register.jsp").forward(request,response);
                    }else{ //tutto ok
                        Utente utente = new Utente();
                        utente.setPassword(passwd);
                        utente.setEmail(email);
                        utente.setUsername(username);
                        utenteDAO.doSave(utente);  //inserisco l utente nel db
                        HttpSession session = request.getSession(true); //è automaticamente loggato
                        session.setAttribute("username",utente.getUsername());
                        session.setAttribute("isLogged", true);
                        response.sendRedirect("../index.html");
                    }
                    break;

                 default:
                  response.sendError(HttpServletResponse.SC_NOT_FOUND, "Risorsa non trovata");
            }
        }catch (IOException | NoSuchAlgorithmException e){
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Si è verificato un errore");
        }
    }
}
