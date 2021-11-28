package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "preferitiServlet", value = "/preferiti/*")
public class preferitiServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        String path = request.getPathInfo();
        switch (path){
            case "/brani":
                request.getRequestDispatcher("../WEB-INF/views/canzone/braniPreferiti.jsp").forward(request,response);
                break;

            case "/artisti":
                request.getRequestDispatcher("../WEB-INF/views/artista/artistiPreferiti.jsp").forward(request,response);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
