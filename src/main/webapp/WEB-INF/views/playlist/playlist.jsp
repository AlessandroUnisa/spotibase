<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | Playlist"/>
        <jsp:param name="style" value="preferiti.css"/>
        <jsp:param name="isDashboard" value="false"/>
        <jsp:param name="footer" value="true"/>
        <jsp:param name="controlBtn" value="true"/>
    </jsp:include>
    <link rel="stylesheet" href="./css/playlist.css">
    <link rel="stylesheet" href="./css/braniPreferiti.css">
</head>
<body>
<jsp:include page="../partials/header.jsp">
    <jsp:param name="prefix" value="./"/>
</jsp:include>

<main class="app">
    <section class="grid-y">
        <h1 class="w100">${playlist.titolo}</h1>
        <p>${playlist.note}</p>
        <c:choose>
            <c:when test="${playlist.canzoni.size() == 0}">
                <h3>Non ci sono elementi nella playlist</h3>
            </c:when>
            <c:otherwise>
                <table class="w75">
                    <tr>
                        <th>Titolo</th>
                        <th>Artista</th>
                        <th>Anno</th>
                        <th>Prezzo</th>
                        <th>Durata</th>
                        <th>Cancella</th>
                    </tr>
                    <c:forEach items="${playlist.canzoni}" var="canzone">
                        <tr>
                            <td>
                                <img src="${canzone.pathImg}">
                                <div class="grid-x justify-center">
                                    <div class="w100">${canzone.titolo}</div>
                                    <button class="controlBtn play ${canzone.codice}" id="${canzone.codice}" onclick="play('${canzone.codice}',this)"><i class="fas fa-play"></i></button>
                                    <c:set var="flag" value="false"></c:set>
                                    <c:forEach items="${listaPreferiti}" var="codice">
                                        <c:if test="${codice == canzone.codice}">
                                            <c:set var="flag" value="true"></c:set>
                                        </c:if>
                                    </c:forEach>
                                    <c:choose>
                                        <c:when test="${flag}">
                                            <button class="controlBtn prefButton${canzone.codice}" onclick="preferenza('${canzone.codice}',1)"><i class="fas invalidSimple fa-heart"></i></button>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="controlBtn prefButton${canzone.codice}" onclick="preferenza('${canzone.codice}',1)"><i class="far invalidSimple fa-heart"></i></button>
                                        </c:otherwise>
                                    </c:choose>

                                    <c:set var="flag" value="false"></c:set>
                                    <c:forEach items="${listaCanzoniAcquistate}" var="codice">
                                        <c:if test="${codice == canzone.codice}">
                                            <c:set var="flag" value="true"></c:set>
                                        </c:if>
                                    </c:forEach>
                                    <c:choose>
                                        <c:when test="${flag}">
                                            <a href="${canzone.pathMP3}" download="${canzone.titolo}">
                                                <button class="controlBtn">
                                                    <i class="fas fa-arrow-down"></i>
                                                </button>
                                            </a>
                                        </c:when>
                                        <c:otherwise>
                                            <button class="controlBtn" onclick="cart('${canzone.codice}')"><i class="fas fa-shopping-cart"></i></button>
                                        </c:otherwise>
                                    </c:choose>

                                </div>
                            </td>
                            <td>${canzone.artisti[0].nomeDArte}</td>
                            <td>${canzone.anno}</td>
                            <td>${canzone.prezzo}</td>
                            <td>${canzone.durata}</td>
                            <td><a href="?name=${playlist.titolo}&del=${canzone.codice}"><button class="controlBtn"><i class="fas fa-trash-alt"></i></button></a></td>
                        </tr>
                    </c:forEach>

                </table>
            </c:otherwise>
        </c:choose>
        <div>
            <form action="./playlist/cancella" method="post">
                <input type="hidden" name="delete" value="${playlist.titolo}">
                <button class="btn primary">Cancella Playlist</button>
            </form>
        </div>
    </section>
</main>

    <%@ include file="../partials/footer.jsp"%>
</body>
</html>
