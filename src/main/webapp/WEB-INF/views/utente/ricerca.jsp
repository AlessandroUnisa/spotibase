<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | HOME"/>
        <jsp:param name="style" value="preferiti.css"/>
        <jsp:param name="style" value="album.css"/>
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
    <c:if test="${canzoni.size() > 0}">
    <section class="grid-x justify-center w100">
        <h1 class="w100">Canzoni </h1>
                <c:forEach items="${canzoni}" var="canzone">
                    <jsp:include page="../partials/cardCanzone.jsp">
                        <jsp:param name="srcImg" value="${canzone.pathImg}"/>
                        <jsp:param name="titolo" value="${canzone.titolo}"/>
                        <jsp:param name="codiceCanzone" value="${canzone.codice}"/>
                    </jsp:include>
                </c:forEach>
    </section>
    </c:if>
    <c:if test="${artisti.size() > 0}">
    <section class="grid-x justify-center w100">
        <h1 class="w100">Artisti </h1>
        <c:forEach items="${artisti}" var="artista">
            <jsp:include page="../partials/cardArtistaHome.jsp">
                <jsp:param name="srcImg" value="${artista.pathImg}"/>
                <jsp:param name="classImg" value="imgArtista"/>
                <jsp:param name="nomeDArte" value="${artista.nomeDArte}"/>
            </jsp:include>
        </c:forEach>
    </section>
    </c:if>
    <c:if test="${album.size() > 0}">
    <section class="grid-x justify-center w100">
        <h1 class="w100">Album</h1>
        <c:forEach items="${album}" var="album">
            <jsp:include page="../partials/cardAlbum.jsp">
                <jsp:param name="pathImg" value="${album.pathImg}"/>
                <jsp:param name="text" value="${album.nome}"/>
                <jsp:param name="codice" value="${album.codice}"/>
            </jsp:include>
        </c:forEach>
    </section>
    </c:if>
</main>

<%@include file="../partials/footer.jsp"%>
</body>
</html>
