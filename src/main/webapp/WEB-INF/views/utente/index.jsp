<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | HOME"/>
        <jsp:param name="style" value="preferiti.css"/>
        <jsp:param name="isDashboard" value="false"/>
        <jsp:param name="footer" value="true"/>
        <jsp:param name="controlBtn" value="true"/>
    </jsp:include>

    <link href="./css/artistiPreferiti.css" rel="stylesheet">


</head>

<body>
<jsp:include page="../partials/header.jsp">
    <jsp:param name="prefix" value="./"/>
</jsp:include>

<main class="app grid-x justify-center">
        <section class="grid-x justify-center">
            <h1 class="w100">Alcune canzoni</h1>

        <c:forEach items="${canzoni}" var="canzone">
            <jsp:include page="../partials/cardCanzone.jsp">
                <jsp:param name="srcImg" value="${canzone.pathImg}"/>
                <jsp:param name="titolo" value="${canzone.titolo}"/>
                <jsp:param name="artista" value="${canzone.artisti[0].nomeDArte}"/>
                <jsp:param name="codiceCanzone" value="${canzone.codice}"/>
                <jsp:param name="url" value="${canzone.pathMP3}"/>
            </jsp:include>
        </c:forEach>



        <h1 class="w100">Generi</h1>
        <jsp:include page="../partials/cardGenere.jsp">
            <jsp:param name="classDiv" value="grid-x justify-center w75"/>
        </jsp:include>


    </section>
    <section class="grid-x justify-center">
        <h1 class="w100">Artisti con più preferenze</h1>
        <c:forEach items="${artisti}" var="artista">
            <jsp:include page="../partials/cardArtistaHome.jsp">
                <jsp:param name="srcImg" value="${artista.pathImg}"/>
                <jsp:param name="classImg" value="imgArtista"/>
                <jsp:param name="nomeDArte" value="${artista.nomeDArte}"/>
            </jsp:include>
        </c:forEach>
    </section>
    <section class="grid-x justify-center">
        <h1 class="w100">Album con più preferenze</h1>
        <c:forEach items="${albums}" var="album">
            <jsp:include page="../partials/cardAlbum.jsp">
                <jsp:param name="pathImg" value="${album.pathImg}"/>
                <jsp:param name="text" value="${album.nome}"/>
                <jsp:param name="codice" value="${album.codice}"/>
            </jsp:include>
        </c:forEach>
    </section>
    <section class="grid-x justify-center">
        <h1 class="w100">Artisti</h1>
        <c:forEach items="${artistiCasuali}" var="artista">
            <jsp:include page="../partials/cardArtistaHome.jsp">
                <jsp:param name="srcImg" value="${artista.pathImg}"/>
                <jsp:param name="classImg" value="imgArtista"/>
                <jsp:param name="nomeDArte" value="${artista.nomeDArte}"/>
            </jsp:include>
        </c:forEach>
    </section>
    <section class="grid-x justify-center">
        <h1 class="w100">Album</h1>
        <c:forEach items="${albumCasuali}" var="album">
            <jsp:include page="../partials/cardAlbum.jsp">
                <jsp:param name="pathImg" value="${album.pathImg}"/>
                <jsp:param name="text" value="${album.nome}"/>
                <jsp:param name="codice" value="${album.codice}"/>
            </jsp:include>
        </c:forEach>
    </section>


    <section class="grid-x justify-center">
        <h1 class="w100">Canzoni nuove uscite</h1>
        <c:forEach items="${canzoniNew}" var="canzone">
            <jsp:include page="../partials/cardCanzone.jsp">
                <jsp:param name="srcImg" value="${canzone.pathImg}"/>
                <jsp:param name="titolo" value="${canzone.titolo}"/>
                <jsp:param name="codiceCanzone" value="${canzone.codice}"/>
                <jsp:param name="url" value="${canzone.pathMP3}"/>
            </jsp:include>
        </c:forEach>
    </section>

    <section class="grid-x justify-center">
        <h1 class="w100">Album nuove uscite</h1>
        <c:forEach items="${albumNew}" var="album">
            <jsp:include page="../partials/cardAlbum.jsp">
                <jsp:param name="pathImg" value="${album.pathImg}"/>
                <jsp:param name="text" value="${album.nome}"/>
                <jsp:param name="codice" value="${album.codice}"/>
            </jsp:include>
        </c:forEach>
    </section>

    <section class="grid-x justify-center">
        <h1 class="w100">Canzoni più acquistate</h1>
        <c:forEach items="${canzoniTopBuy}" var="canzone">
            <jsp:include page="../partials/cardCanzone.jsp">
                <jsp:param name="srcImg" value="${canzone.pathImg}"/>
                <jsp:param name="titolo" value="${canzone.titolo}"/>
                <jsp:param name="url" value="${canzone.pathMP3}"/>
                <jsp:param name="codiceCanzone" value="${canzone.codice}"/>
            </jsp:include>
        </c:forEach>
    </section>

    <section class="grid-x justify-center">
        <h1 class="w100">Albums più acquistati</h1>
        <c:forEach items="${albumTopBuy}" var="album">
            <jsp:include page="../partials/cardAlbum.jsp">
                <jsp:param name="pathImg" value="${album.pathImg}"/>
                <jsp:param name="text" value="${album.nome}"/>
                <jsp:param name="codice" value="${album.codice}"/>
            </jsp:include>
        </c:forEach>
    </section>

</main>

  <%@include file="../partials/footer.jsp"%>


</body>
</html>