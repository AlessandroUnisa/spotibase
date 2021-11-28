package controller.carrello;

import com.sun.source.tree.Tree;
import org.json.simple.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.TreeSet;

@WebServlet(name = "jsonCarrelloServlet", value = "/jsonCarrelloServlet")
public class jsonCarrelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //inserimento elementi nel carrello
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String codice = request.getParameter("cod");
        HttpSession session = request.getSession(false);
        JSONObject object = new JSONObject();

        TreeSet<String> codici = (TreeSet<String>) session.getAttribute("listCart");
        object.put("flag", codici.add(codice));
        object.put("numElements", codici.size());  //per aggiornare i badgeCart
        response.setContentType("application/json");
        response.getWriter().println(object);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
