<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | Artisti Preferiti"/>
        <jsp:param name="style" value="preferiti.css"/>
        <jsp:param name="numCartElem" value="${sessionScope.listCart.size()}"/>
        <jsp:param name="prefix" value="../"/>
    </jsp:include>
    <link rel="stylesheet" href="../css/artistiPreferiti.css">
</head>
<body style="background-color: black; color: white">
    <main class="app">
        <section class="grid-x justify-center">
            <h1 class="w100">Artisti Preferiti</h1>
            <c:forEach begin="1" end="20">
                <jsp:include page="../partials/cardArtista.jsp">
                    <jsp:param name="srcImg" value="../image/singoli/eminem.jpg"/>
                    <jsp:param name="classDiv" value="w10"/>
                    <jsp:param name="text" value="baldh ss"/>
                </jsp:include>
            </c:forEach>
        </section>
    </main>
</body>
</html>
