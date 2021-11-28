<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java"%>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head lang="it" dir="ltr">
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | Dashboard"/>
        <jsp:param name="style" value="dashboard.css"/>
        <jsp:param name="prefix" value="../"/>
        <jsp:param name="darkMode" value="false"/>
    </jsp:include>
    <link rel="stylesheet" href="../css/crm.css">
    <link rel="stylesheet" href="../css/popup.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js" defer></script>
    <script src="../js/crm.js" defer></script>
    <script src="../js/popup.js" defer></script>
</head>
<body>
    <main class="app">
       <section class="content grid-x">
            <header class="topbar grid-x align-center">
                <img src="../image/logo5.png">
                <h3>GESTIONE SPOTIBASE</h3>

                <div>
                    Benvenuto amministratore
                    <button class="btn primary" onclick="window.location.href = '../account/logout';">Logout</button>
                </div>
            </header>
            <div class="body grid-x justify-center">
                <jsp:include page="../partials/crm/card.jsp">
                    <jsp:param name="urlImage" value="../icons/album.svg"/>
                    <jsp:param name="title" value="Album"/>
                    <jsp:param name="click" value="loadJSONDoc(1,1)"/>
                </jsp:include>
                <jsp:include page="../partials/crm/card.jsp">
                    <jsp:param name="urlImage" value="../icons/song.svg"/>
                    <jsp:param name="title" value="Canzoni"/>
                    <jsp:param name="click" value="loadJSONDoc(2,1)"/>
                </jsp:include>
                <jsp:include page="../partials/crm/card.jsp">
                    <jsp:param name="urlImage" value="../icons/artist.svg"/>
                    <jsp:param name="title" value="Artisti"/>
                    <jsp:param name="click" value="loadJSONDoc(3,1)"/>
                </jsp:include>


                <div class="grid-y cell w100"><!--id="tableMOD"-->
                    <table id="tableMOD">
                    </table>
                    <div class="modal">
                        <div class="modal-content">
                            <span class="close">&times;</span>
                            <h4></h4>
                            <form class=" align-center" action="" method="post">
                            </form>
                        </div>
                    </div>
                    <jsp:include page="../partials/crm/paginator.jsp">
                        <jsp:param name="pages" value="${numPageAlbum}"/>
                        <jsp:param name="id" value="idAlb"/>
                        <jsp:param name="numCard" value="1"/>
                    </jsp:include>
                    <jsp:include page="../partials/crm/paginator.jsp">
                        <jsp:param name="pages" value="${numPageCanzoni}"/>
                        <jsp:param name="id" value="idCan"/>
                        <jsp:param name="numCard" value="2"/>
                    </jsp:include>
                    <jsp:include page="../partials/crm/paginator.jsp">
                        <jsp:param name="pages" value="${numPageArtisti}"/>
                        <jsp:param name="id" value="idArt"/>
                        <jsp:param name="numCard" value="3"/>
                    </jsp:include>
                </div>

                <div class="grid-y cell w40 panelData borderRight">
                    <table>
                        <caption><b>Artisti più ascoltati</b></caption>
                        <tbody>
                            <tr>
                                <th>Cod Fiscale</th>
                                <th>Nome</th>
                                <th>Cognome</th>
                                <th>Nome d'arte</th>
                            </tr>
                            <c:forEach items="${listaTopArtisti}" var="artista">
                                <tr>
                                    <td>${artista.codFiscale}</td>
                                    <td>${artista.nome}</td>
                                    <td>${artista.cognome}</td>
                                    <td>${artista.nomeDArte}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>


                <div class="grid-y cell w40 panelData borderLeft">
                    <table>
                        <caption><b>Brani più ascoltati</b></caption>
                        <tbody>
                            <tr>
                                <th>Codice</th>
                                <th>Titolo</th>
                                <th>Anno</th>
                                <th>Prezzo</th>
                            </tr>
                            <c:forEach items="${listaTopBrani}" var="brano">
                                <tr>
                                    <td>${brano.codice}</td>
                                    <td>${brano.titolo}</td>
                                    <td>${brano.anno}</td>
                                    <td>${brano.prezzo}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>




                <div class="grid-y cell w20 align-center panel">
                    <img src="../icons/money.svg">
                    <div>
                        Ad oggi hai guadagnato: <fmt:formatNumber value = "${guadagnoTot}"/> Euro<br>
                        Guadagno canzoni:  <fmt:formatNumber value = "${guadagnoCanzoni}"/> Euro<br>
                        Guadagno album:  <fmt:formatNumber value = "${guadagnoAlbum}"/> Euro<br>
                        Guadagno abbonamenti:  <fmt:formatNumber value = "${guadagnoAbbonamenti}"/> Euro<br>
                    </div>
                </div>

                <div class="panelData" class="grid-y cell w40">
                    <table>
                        <caption><b>Album e brani più acquistati</b></caption>
                        <tbody>
                        <tr>
                            <th>Codice</th>
                            <th>Nome</th>
                            <th>Tipo</th>
                            <th>Prezzo</th>
                        </tr>
                        <c:forEach items="${listaTopAlbumAcquistati}" var="album">
                            <tr>
                                <td>${album.codice}</td>
                                <td>${album.nome}</td>
                                <td>Album</td>
                                <td>${album.prezzo}</td>
                            </tr>
                        </c:forEach>
                        <c:forEach items="${listaTopCanzoniAcquistate}" var="canz">
                            <tr>
                                <td>${canz.codice}</td>
                                <td>${canz.titolo}</td>
                                <td>Canzone</td>
                                <td>${canz.prezzo}</td>
                            </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                </div>

                <div class="grid-y cell w20 panel">
                    <b>Abbonamenti disponibili</b><hr>
                    <table>
                        <tr>
                           <th>Nome</th>
                           <th>PrezzoMensile</th>
                        </tr>
                        <c:forEach items="${listaAbbonamenti}" var="abbonamento">
                            <tr>
                                <td>${abbonamento.nome}</td>
                                <td>${abbonamento.prezzoMensile}</td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </section>
    </main>
    <c:choose>
        <c:when test="${modifica==true}"><script>alert('Modifica andata a buon fine :)')</script></c:when>
        <c:when test="${modifica==false}"><script>alert('Errore durante la procedura di modifica :(')</script></c:when>
    </c:choose>
</body>
</html>
