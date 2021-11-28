<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="utf-8" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="darkMode" value="false"/>
        <jsp:param name="title" value="Spotibase | Artisti Preferiti"/>
        <jsp:param name="style" value="album.css"/>
        <jsp:param name="footer" value="true"/>
    </jsp:include>
    <link href="./css/artistiPreferiti.css" rel="stylesheet">
    <link href="./css/braniPreferiti.css" rel="stylesheet">
</head>
<body>
<jsp:include page="../partials/header.jsp">
    <jsp:param name="prefix" value="./"/>
</jsp:include>


    <main class="app grid-x justify-center">
        <section class="grid-x justify-center w100">
                <h1 class="w100">Elementi nel carrello</h1>
            <h4>Gli elementi che hai già acquistato sono stati cancellati dal carrello</h4>
<c:choose>
    <c:when test="${sessionScope.listCart.size() == 0}">
        <h3>Non ci sono elementi nel carrello</h3>
    </c:when>
    <c:otherwise>
        <div class="overflowY w100">
            <table>
                <tr>
                    <th>Titolo</th>
                    <th>Artista</th>
                    <th>Anno</th>
                    <th>Prezzo</th>
                    <th>Tipo</th>
                    <th>Cancella</th>
                </tr>

                <c:forEach items="${albums}" var="album">
                    <tr>
                        <td>
                            <img src="${album.pathImg}"><br>
                                ${album.nome}
                        </td>
                        <td>${album.artisti[0].nomeDArte}</td>
                        <td>${album.anno}</td>
                        <td>${album.prezzo}</td>
                        <td>Album</td>
                        <td><a href="?del=${album.codice}"><button class="controlBtn"><i class="fas fa-trash-alt"></i></button></a></td>
                    </tr>
                </c:forEach>
                <c:forEach items="${canzoni}" var="canzone">
                    <tr>
                        <td>
                            <img src="${canzone.pathImg}"><br>
                                ${canzone.titolo}
                        </td>
                        <td>${canzone.artisti[0].nomeDArte}</td>
                        <td>${canzone.anno}</td>
                        <td>${canzone.prezzo}</td>
                        <td>Canzone</td>
                        <td><a href="?del=${canzone.codice}"><button class="controlBtn"><i class="fas fa-trash-alt"></i></button></a></td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <div class="grid-y w33">
            <div class="divTotale">Totale:<fmt:formatNumber value = "${totale}"/> Euro</div>

                <c:choose>
                    <c:when test="${sessionScope.isLogged}">
                        <form action="./carrello" method="post">
                            <button class="btn primary" value="submit">Procedi all'acquisto</button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <div>Devi effettuare il login per procedere all'acquisto</div>
                    </c:otherwise>
                </c:choose>

        </div>
    </c:otherwise>
</c:choose>
        </section>
    </main>
</body>
</html>