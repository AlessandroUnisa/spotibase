<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | Artisti Preferiti"/>
        <jsp:param name="style" value="album.css"/>
        <jsp:param name="footer" value="true"/>
        <jsp:param name="controlBtn" value="true"/>
    </jsp:include>
</head>
<body>
<jsp:include page="../partials/header.jsp">
    <jsp:param name="prefix" value="./"/>
</jsp:include>


    <main class="app grid-x justify-center">
        <section class="grid-x justify-center">
            <h1 class="w100">${album.nome}</h1>
            <img src="${album.pathImg}">
            <div class="justify-center grid-x w100">
                <button class="btn primary"  onclick="window.location.href ='./artista?nomeDArte=${artistaNome}'">Artista</button>
            </div>
            <div class="overflowY w100">
                <table>
                    <tr>
                        <th>Play</th>
                        <th>Titolo</th>
                        <th>Durata</th>
                        <th>Artista</th>
                        <th>Anno</th>
                        <th>Preferenza</th>
                        <th>Aquista</th>
                    </tr>
                    <c:forEach items="${album.canzoni}" var="canzone">
                        <tr>
                            <td><button onclick="play('${canzone.codice}', this)" id="${canzone.codice}" class="controlBtn play ${canzone.codice}"><i class="fas fa-play" aria-hidden="true"></i></button></td>
                            <td>${canzone.titolo}</td>
                            <td>${canzone.durata}</td>
                            <td>
                                <c:forEach items="${canzone.artisti}" var="artista">
                                    ${artista.nomeDArte}<br>
                                </c:forEach>
                            </td>
                            <td>${canzone.anno}</td>
                           <c:choose>
                               <c:when test="${!sessionScope.isLogged}">
                                   <td><button onclick="alert('Effettua il login per esprimere preferenze')" id="pref${canzone.codice}" class="controlBtn"><i class="far fa-heart invalidSimple" aria-hidden="true"></i></button></td>
                               </c:when>
                               <c:otherwise>
                                   <c:set var="flag" value="false" scope="page" />
                                   <c:forEach items="${canzoniPreferite}" var="cod">
                                       <c:if test="${cod eq canzone.codice}">
                                           <td><button onclick="preferenza('${canzone.codice}',1)" id="pref${canzone.codice}" class="controlBtn prefButton${canzone.codice}"><i class="fas fa-heart invalidSimple" aria-hidden="true"></i></button></td>
                                           <c:set var="flag" value="true" scope="page" />
                                       </c:if>
                                   </c:forEach>
                                   <c:if test="${flag eq 'false'}">
                                       <td><button onclick="preferenza('${canzone.codice}',1)" id="pref${canzone.codice}" class="controlBtn prefButton${canzone.codice}"><i class="far fa-heart invalidSimple" aria-hidden="true"></i></button></td>
                                   </c:if>
                               </c:otherwise>
                           </c:choose>

                            <c:set var="flagCan" value="false"></c:set>
                            <c:forEach items="${canzoniAcquistate}" var="codCan">
                                <c:if test="${codCan == canzone.codice}">
                                    <c:set var="flagCan" value="true"></c:set>
                                </c:if>
                            </c:forEach>
                            <c:choose>
                                <c:when test="${flagCan}">
                                    <td><a href="${canzone.pathMP3}" download="${canzone.titolo}"><button class="controlBtn"><i class="fas fa-arrow-down"></i></button></a></td>
                                </c:when>
                                <c:otherwise>
                                   <td><button class="controlBtn" onclick="cart('${canzone.codice}')"><i class="fas fa-shopping-cart" aria-hidden="true"></i></button></td>
                                </c:otherwise>
                            </c:choose>

                          </tr>
                    </c:forEach>
                    <tr>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td></td>
                        <td>
                            <c:choose>
                                <c:when test="${sessionScope.isLogged}">
                                    <c:choose>
                                        <c:when test="${flagAlbumPreferito}">
                                            <button title="Preferenze Album" onclick="preferenza('${album.codice}',3)" id="pref${album.codice}" class="controlBtn prefButton${album.codice}"><i class="fas fa-heart invalidSimple" aria-hidden="true"></i></button>
                                        </c:when>
                                        <c:otherwise>
                                            <button title="Preferenze Album" onclick="preferenza('${album.codice}',3)" id="pref${album.codice}" class="controlBtn prefButton${album.codice}"><i class="far fa-heart invalidSimple" aria-hidden="true"></i></button>
                                        </c:otherwise>
                                    </c:choose>
                                </c:when>
                                <c:otherwise>
                                    <button title="Preferenze Album" onclick="alert('Effettua il login per esprimere preferenze')" class="controlBtn"><i class="far fa-heart invalidSimple" aria-hidden="true"></i></button>
                                </c:otherwise>
                            </c:choose>

                        </td>
                        <td>
                            <c:if test="${!albumAcquistatato}">
                                <button class="btn primary" id="btnAcquista" onclick="cart('${album.codice}')">Acquista</button>
                            </c:if>
                        </td>
                    </tr>
                </table>
            </div>

            <h2 class="w100">Ascolta anche altri brani</h2>
            <c:forEach items="${braniRand}" var="brano">
                <jsp:include page="../partials/cardCanzone.jsp">
                    <jsp:param name="srcImg" value="${brano.pathImg}"/>
                    <jsp:param name="codiceCanzone" value="${brano.codice}"/>
                    <jsp:param name="titolo" value="${brano.titolo}"/>
                </jsp:include>
            </c:forEach>
        </section>
    </main>

<%@include file="../partials/footer.jsp"%>


</body>
</html>
