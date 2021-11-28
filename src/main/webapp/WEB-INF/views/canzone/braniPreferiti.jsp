<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <jsp:include page="../partials/head.jsp">
            <jsp:param name="title" value="Spotibase | Brani Preferiti"/>
            <jsp:param name="style" value="preferiti.css"/>
            <jsp:param name="prefix" value="../"/>
        </jsp:include>
        <link rel="stylesheet" href="../css/braniPreferiti.css">
    </head>
    <body>
    <jsp:include page="../partials/header.jsp">
        <jsp:param name="linkLibrary" value="#"/>
        <jsp:param name="numCartElem" value="${sessionScope.listCart.size()}"/>
        <jsp:param name="prefix" value="../"/>
    </jsp:include>
      <main class="app">
          <section class="grid-y">
              <h1 class="w100">Brani Preferiti</h1>

              <ul id="buttonPref" class="w100">
                  <li><a href="#">Album Preferiti</a></li>
                  <li><a href="#">Artisti Preferiti</a></li>
              </ul>
              <table class="w75">
                  <tr>
                      <th>Titolo</th>
                      <th>Durata</th>
                      <th>Artista</th>
                      <th>Album</th>
                      <th>Anno</th>
                      <th>Prezzo</th>
                  </tr>
                  <tr>
                      <td>
                          <img src="https://images-na.ssl-images-amazon.com/images/I/71WVq7VqUkL._AC_SL1260_.jpg">
                          <div class="grid-x justify-center">
                              <div class="w100">Bohemian Rhapsody</div>
                              <button class="controlBtn"><i class="fas fa-play"></i></button>
                              <button class="controlBtn"><i class="far fa-heart"></i></button>
                              <button class="controlBtn"><i class="fas fa-shopping-cart"></i></button>
                          </div>
                      </td>
                      <td>5.54</td>
                      <td>Queen</td>
                      <td>Bohemian Rhapsody</td>
                      <td>1975</td>
                      <td>0.99</td>
                  </tr>
              </table>
          </section>
      </main>
      <script src="https://kit.fontawesome.com/19b680db63.js" crossorigin="anonymous"></script>
    </body>
</html>