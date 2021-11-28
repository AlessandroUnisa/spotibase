<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | Abbonamenti"/>
        <jsp:param name="controlBtn" value="true"/>
        <jsp:param name="footer" value="true"/>
        <jsp:param name="style" value="abbonamenti.css"/>
    </jsp:include>
    <script src="https://kit.fontawesome.com/19b680db63.js" crossorigin="anonymous" defer></script>
</head>
<body>
<jsp:include page="../partials/header.jsp">
    <jsp:param name="prefix" value="./"/>
</jsp:include>
    <main class="app grid-x justify-center">
        <section class="grid-x justify-center">
           <div class="w20 wrapper grid-x justify-center">
               <img src="./image/abb.png">
               <h1>ABBONAMENTO PLUS</h1>
               <h2>5.99€</h2>
               <p><i class="fas fa-check-circle"></i>73 milioni di brani</p><br>
               <p><i class="fas fa-check-circle"></i>senza pubblicità</p><br>
               <p><i class="fas fa-check-circle"></i>alta qualità</p><br>
               <p><i class="fas fa-times-circle"></i>playlist limitate(15)</p><br>
               <c:choose>
                   <c:when test="${sessionScope.isLogged}">
                       <button class="btnAcquista" onclick="cartAbbonamento('plus')">Acquista</button>
                   </c:when>
                   <c:otherwise>
                       <button class="btnAcquista" onclick="alert('Effettua il login per acquistare')">Acquista</button>
                   </c:otherwise>
               </c:choose>
           </div>
           <div class="w20 wrapper grid-x justify-center">
               <img src="./image/abb2.png">
               <h1>ABBONAMENTO MEGA</h1>
               <h2>10.99€</h2>
               <p><i class="fas fa-check-circle"></i>73 milioni di brani</p><br>
               <p><i class="fas fa-check-circle"></i>senza pubblicità</p><br>
               <p><i class="fas fa-check-circle"></i>alta qualità</p><br>
               <p><i class="fas fa-times-circle"></i>playlist limitate(45)</p><br>
               <c:choose>
                   <c:when test="${sessionScope.isLogged}">
                       <button class="btnAcquista" onclick="cartAbbonamento('mega')">Acquista</button>
                   </c:when>
                   <c:otherwise>
                       <button class="btnAcquista" onclick="alert('Effettua il login per acquistare')">Acquista</button>
                   </c:otherwise>
               </c:choose>
           </div>
           <div class="w20 wrapper grid-x justify-center">
               <img src="./image/abb3.png">
               <h1>ABBONAMENTO ULTRA</h1>
               <h2>14.99€</h2>
               <p><i class="fas fa-check-circle"></i>73 milioni di brani</p><br>
               <p><i class="fas fa-check-circle"></i>senza pubblicità</p><br>
               <p><i class="fas fa-check-circle"></i>alta qualità</p><br>
               <p><i class="fas fa-check-circle"></i>playlist illimitate</p><br>
               <c:choose>
                   <c:when test="${sessionScope.isLogged}">
                       <button class="btnAcquista" onclick="cartAbbonamento('ultra')">Acquista</button>
                   </c:when>
                   <c:otherwise>
                       <button class="btnAcquista" onclick="alert('Effettua il login per acquistare')">Acquista</button>
                   </c:otherwise>
               </c:choose>
           </div>
            <c:if test="${sessionScope.isLogged}">
                <p>Clicca <a href="./libreria">qui</a> per vedere i tuoi abbonamenti</p>
            </c:if>
        </section>
    </main>
    <%@include file="../partials/footer.jsp"%>
</body>
</html>
