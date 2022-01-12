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

@WebServlet(name = "register", value = "/register")
public class Registrazione extends HttpServlet {


    public void register(HttpServletRequest request, HttpServletResponse response, UtenteAPI utenteAPI) throws SQLException, NoSuchAlgorithmException, IOException, ServletException {
        String username = request.getParameter("username");
        System.out.println(username+"**********");
        String email = request.getParameter("email");
        String passwd = request.getParameter("passwd");
        String passwdCheck = request.getParameter("passwdCheck");


        int flag=0;
        //errori: username gia presente, email non valida, passwd non uguali, passwd non valida
        //se l email non è valida oppure ci sono gia utenti con quella mail (la lista ne conterra al massimo uno)
        System.out.println(utenteAPI.isValidEmail(email)+" "+email);
        if(!utenteAPI.isValidEmail(email) || utenteAPI.findUsers("email",email).size()!=0){ //check mail
            request.setAttribute("errEmail","Email non valida");
            flag++;
        }
        if(!passwd.equals(passwdCheck)){ //check passwd uguali
            request.setAttribute("errPasswdNE","Le password non coincidono");
            flag++;
        }
        System.out.println(passwd + " " + utenteAPI.isValidPasswd(passwd));
        if(!utenteAPI.isValidPasswd(passwd)){  //check password
            request.setAttribute("errPasswd","Password non valida");
            flag++;
        }
        System.out.println(utenteAPI.findUsers("username",username) + " "+username);
        if(utenteAPI.findUsers("username",username).size()!=0){ //check username
            System.out.println("ora qui");
            request.setAttribute("errUsername","Username già presente");
            flag++;
        }
        if(!utenteAPI.isValidUsername(username)){
            request.setAttribute("errUsernameFormato", "La username non rispetta il formato");
            flag++;
        }

        if(flag>0){  //setto i campi che aveva gia inserito
            System.out.println("flag= "+flag);
            request.setAttribute("username",username);
            request.setAttribute("email",email);
            request.setAttribute("passwd",passwd);
            request.setAttribute("passwdCheck",passwdCheck);
            request.getRequestDispatcher("./WEB-INF/views/utente/register.jsp").forward(request,response);
        }else{ //tutto ok
            System.out.println("tutto ok");

            Utente utente = new Utente();
            utente.setPassword(passwd);
            utente.setEmail(email);
            utente.setUsername(username);
            utenteAPI.doSave(utente);  //inserisco l utente nel db

            HttpSession session = request.getSession(true); //è automaticamente loggato
            session.setAttribute("username",utente.getUsername());
            session.setAttribute("isLogged", true);

            response.sendRedirect("./index.html");
        }

    }



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String path = request.getPathInfo();
        if(request.getSession(true).getAttribute("isLogged")==null)
            request.getRequestDispatcher("./WEB-INF/views/utente/register.jsp").forward(request, response);
        else response.sendRedirect("./index.html");

    }

    @Override
    @Generated
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String path = request.getPathInfo();
        try {
            UtenteAPI utenteAPI = new UtenteDAO();
            register(request,response, utenteAPI);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
