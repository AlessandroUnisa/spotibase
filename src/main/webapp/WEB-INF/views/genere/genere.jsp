<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>

    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | Generi"/>
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
        <c:if test="${canzoniGenere.size() > 0}">
            <section class="grid-x justify-center">
                <h1 class="w100">Canzoni:</h1>
                <c:forEach items="${canzoniGenere}" var="canzone">
                    <jsp:include page="../partials/cardCanzone.jsp">
                        <jsp:param name="srcImg" value="${canzone.pathImg}"/>
                        <jsp:param name="titolo" value="${canzone.titolo}"/>
                        <jsp:param name="codiceCanzone" value="${canzone.codice}"/>
                    </jsp:include>
                </c:forEach>
            </section>
        </c:if>
        <c:if test="${albumGenere.size() > 0}">
            <section class="grid-x justify-center">
                <h1 class="w100">Album:</h1>
                <c:forEach items="${albumGenere}" var="album">
                    <jsp:include page="../partials/cardAlbum.jsp">
                        <jsp:param name="classDiv" value="w12"/>
                        <jsp:param name="pathImg" value="${album.pathImg}"/>
                        <jsp:param name="text" value="${album.nome}"/>
                        <jsp:param name="codice" value="${album.codice}"/>

                    </jsp:include>
                </c:forEach>
            </section>
        </c:if>
</main>
<%@ include file="../partials/footer.jsp"%>
</body>
</html>