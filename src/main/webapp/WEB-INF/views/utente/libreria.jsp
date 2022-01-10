<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | My Area"/>
        <jsp:param name="controlBtn" value="true"/>
        <jsp:param name="footer" value="true"/>
        <jsp:param name="style" value="libreriaUtente.css"/>
        <jsp:param name="script" value="playlist.js"/>
    </jsp:include>
    <link href="./css/artistiPreferiti.css" rel="stylesheet">
    <script src="./js/popup.js" defer></script>
    <link rel="stylesheet" href="./css/popup.css">
    <link rel="stylesheet" href="./css/libreria.css">
</head>
<body>
    <c:choose>
        <c:when test="${flagInsert==false}">
            <script>alert('Si è verificato un errore. Riprovare')</script>
        </c:when>
    </c:choose>

    <jsp:include page="../partials/header.jsp">
        <jsp:param name="prefix" value="./"/>
    </jsp:include>
    <main class="app grid-x">
        <section class="grid-x">
            
            <h2 class="w100">I tuoi abbonamenti</h2>
            <c:forEach items="${utente.attivazioni}" var="attivazione">
                <div class="w15 grid-x justify-center wrapperAbbonamenti">
                    <img src="./icons/ticketColor.svg">
                    <span class="w100">${attivazione.abbonamento.nome}</span><br>
                    <span class="w100">Data inizio: ${attivazione.dataInizio.dayOfMonth}-${attivazione.dataInizio.monthValue}-${attivazione.dataInizio.year}</span>
                    <span class="w100">Data scadenza: ${attivazione.dataFine.dayOfMonth}-${attivazione.dataFine.monthValue}-${attivazione.dataFine.year}</span>
                        <c:choose>
                            <c:when test="${attivazione.dataFine < dataOggi}">
                                <p class="scaduto w100">Scaduto</p>
                            </c:when>
                            <c:otherwise>
                                <p class="attivo w100">Attivo</p>
                            </c:otherwise>
                        </c:choose>
                    <span><a href="./abbonamenti">info</a></span>
                </div>
            </c:forEach>


             <h2 class="w100">I tuoi artisti preferiti</h2>
             <c:forEach items="${utente.artisti}" var="artista">
                 <jsp:include page="../partials/cardArtista.jsp">
                     <jsp:param name="classDiv" value="w25 divArtista"/>
                     <jsp:param name="srcImg" value="${artista.pathImg}"/>
                     <jsp:param name="text" value="${artista.nomeDArte}"/>
                     <jsp:param name="classImg" value="imgArtista"/>
                 </jsp:include>
             </c:forEach>


            <div class="modal">
                <div class="modal-content">
                    <span class="close">&times;</span>
                    <form method="post" action="./playlist/crea" onsubmit="return checkNote() && checkTitolo()">
                        <label for="titolo">Titolo</label><br>
                        <span class="invalidSimple" style="opacity: 0;" id="errorTitolo">Campo titolo non valido</span>
                        <input type="text" id="titolo" name="titolo"><br>
                        <label for="note">Note</label><br>
                        <span class="invalidSimple" style="opacity: 0;" id="errorNote">Campo note non valido</span>
                        <textarea cols="10" rows="10" name="note" id="note" ></textarea><br>
                        <button type="submit" class="btn primary" >Invia</button><br>
                    </form>
                </div>
            </div>
            <h2 class="w100">Le tue playlist<i onclick="openPopup()" class="fas fa-plus-circle"></i></h2>
            <c:set var="count" value="0" scope="page"/>
            <c:forEach items="${utente.playlists}" var="playlist">
                <jsp:include page="../partials/cardPlaylist.jsp">
                    <jsp:param name="classDiv" value="w20 imgEffectFull small center"/>
                    <jsp:param name="pathImg" value="./image/playlist/${numImgPlaylist[count]}.jpg"/>
                    <jsp:param name="text" value="${playlist.titolo}"/>
                </jsp:include>
                <c:set var="count" value="${count+1}" scope="page"/>
            </c:forEach>

            <h2 class="w100">I tuoi album preferiti</h2>
            <c:forEach items="${utente.albums}" var="album">
                <jsp:include page="../partials/cardAlbum.jsp">
                    <jsp:param name="classDiv" value="w12"/>
                    <jsp:param name="pathImg" value="${album.pathImg}"/>
                    <jsp:param name="text" value="${album.nome}"/>
                    <jsp:param name="codice" value="${album.codice}"/>
                </jsp:include>
            </c:forEach>

            <h2 class="w100">Le canzoni che hai acquistato</h2>
            <c:forEach items="${canzoniAcquistate}" var="canzone">
                <jsp:include page="../partials/cardCanzone.jsp">
                    <jsp:param name="srcImg" value="${canzone.pathImg}"/>
                    <jsp:param name="codiceCanzone" value="${canzone.codice}"/>
                    <jsp:param name="titolo" value="${canzone.titolo}"/>
                    <jsp:param name="url" value="${canzone.pathMP3}"/>
                </jsp:include>
            </c:forEach>

            <h2 class="w100">Gli album che hai acquistato</h2>
            <c:forEach items="${albumAcquistati}" var="album">
                <jsp:include page="../partials/cardAlbum.jsp">
                    <jsp:param name="classDiv" value="w12"/>
                    <jsp:param name="pathImg" value="${album.pathImg}"/>
                    <jsp:param name="text" value="${album.nome}"/>
                    <jsp:param name="codice" value="${album.codice}"/>
                </jsp:include>
            </c:forEach>

            <h2 class="w100">Brani preferiti</h2>
            <c:forEach items="${utente.canzoni}" var="brano">
                <jsp:include page="../partials/cardCanzone.jsp">
                    <jsp:param name="srcImg" value="${brano.pathImg}"/>
                    <jsp:param name="codiceCanzone" value="${brano.codice}"/>
                    <jsp:param name="titolo" value="${brano.titolo}"/>
                </jsp:include>
            </c:forEach>
        </section>
    </main>

<%@include file="../partials/footer.jsp"%>
<script>
    function openPopup(){
        document.getElementsByClassName('modal')[0].style.display = 'block';
    }
</script>
<c:if test="${errInsPlay!=null}">
    <script>alert('${errInsPlay}');</script>
</c:if>
<c:if test="${errTitolo!=null}">
    <script>alert('${errTitolo}');</script>
</c:if>
</body>
</html>
