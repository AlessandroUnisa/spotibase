<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | Artisti"/>
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

<main class="app grid-x justify-center w100">
    <section class="grid-x justify-center w100">
        <h1 class="titoloArtisti w100"> ${param.nomeDArte}</h1> <!--lo prende dall url-->
        <div class="artista">
            <img src="${pathImg}" class="imgArtistaPlus">
        </div>
        <div class="w100 grid-x justify-center">
            <c:choose>
                <c:when test="${!sessionScope.isLogged}">
                    <button title="Preferenze Artista" onclick="alert('Effettua il login per esprimere preferenze')"  class="controlBtn">
                        <i class="far fa-heart invalidSimple" aria-hidden="true"></i>
                    </button>
                </c:when>
                <c:when test="${flagPref}">
                    <button title="Preferenze Artista" onclick="preferenza('${fn:replace(param.nomeDArte,' ','-')}',2)" id="pref${fn:replace(param.nomeDArte,' ','-')}" class="controlBtn prefButton${fn:replace(param.nomeDArte,' ','-')}">
                        <i class="fas fa-heart invalidSimple" aria-hidden="true"></i>
                    </button>
                </c:when>
                <c:otherwise>
                    <button title="Preferenze Artista" onclick="preferenza('${fn:replace(param.nomeDArte,' ','-')}',2)" id="pref${fn:replace(param.nomeDArte,' ','-')}" class="controlBtn prefButton${fn:replace(param.nomeDArte,' ','-')}">
                        <i class="far fa-heart invalidSimple" aria-hidden="true"></i>
                    </button>
                </c:otherwise>
            </c:choose>
        </div>


    </section>
    <section class="grid-x justify-center w100">
        <h1 class="titoloArtisti w100">Canzoni</h1>
        <c:forEach items="${canzoni}" var="canzone">
            <jsp:include page="../partials/cardCanzone.jsp">
                <jsp:param name="srcImg" value="${canzone.pathImg}"/>
                <jsp:param name="codiceCanzone" value="${canzone.codice}"/>
                <jsp:param name="titolo" value="${canzone.titolo}"/>
                <jsp:param name="url" value="${canzone.pathMP3}"/>
            </jsp:include>
        </c:forEach>
    </section>


</main>


<%@ include file="../partials/footer.jsp"%>

</body>
</html>