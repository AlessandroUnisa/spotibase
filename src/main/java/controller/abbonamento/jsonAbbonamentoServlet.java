package controller.abbonamento;

import model.Abbonamento.Abbonamento;
import model.Attivazione.Attivazione;
import model.Attivazione.AttivazioneDAO;
import model.Utente.Utente;
import org.json.simple.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.time.LocalDate;

@WebServlet(name = "jsonAbbonamentoServlet", value = "/jsonAbbonamentoServlet")
public class jsonAbbonamentoServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //acquisto abbonamento
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String nomeAbbonamento = request.getParameter("nome");
        String username = (String) request.getSession(false).getAttribute("username");
        LocalDate dataAttizazione = LocalDate.now();
        LocalDate dataScadenza = LocalDate.of(dataAttizazione.getYear(), dataAttizazione.getMonthValue()+1, dataAttizazione.getDayOfMonth());
        Attivazione attivazione = new Attivazione();
        attivazione.setUtente(new Utente());
        attivazione.setAbbonamento(new Abbonamento());
        attivazione.getUtente().setUsername(username);
        attivazione.getAbbonamento().setNome(nomeAbbonamento);
        attivazione.setDataInizio(dataAttizazione);
        attivazione.setDataFine(dataScadenza);
        JSONObject object = new JSONObject();
        object.put("flag", new AttivazioneDAO().doInsertAttivazione(attivazione));
        response.setContentType("application/json");
        response.getWriter().println(object);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
