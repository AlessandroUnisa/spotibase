package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

//indica l'istanza della servlet che aspetta richieste com il MIME multipart/form-data
@MultipartConfig
@WebServlet(name = "UploadServlet", value = "/UploadServlet")
public class UploadServlet extends HttpServlet {
    //i generi si devono caricare nel servletContext
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("fileImm"); //part è l'item ricevuto con il MIME multipart/form-data (metodo utilizzato POST)
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();//ritorna il nome del file nome.estensione

        String path = this.getServletContext().getRealPath("/image/singoli"); //cartella dove salvare l'immagine. Si trova in target
        System.out.println(path);
        File uploads = new File(path);

        int lenght= fileName.length(); //lunghezza nome immagine
        //Parametri: nome, estensione, directory
        File file = File.createTempFile(fileName.substring(0,lenght-4),fileName.substring(lenght-4,lenght), uploads);
        try (InputStream input = filePart.getInputStream()) {//prendo l'input stream dall'item ricevuto
            Files.copy(input, file.toPath() , StandardCopyOption.REPLACE_EXISTING);//copio il file
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }
}
