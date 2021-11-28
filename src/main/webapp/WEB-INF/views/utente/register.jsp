<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <jsp:include page="../partials/head.jsp">
        <jsp:param name="title" value="Spotibase | Registrati"/>
        <jsp:param name="style" value="loginRegister.css"/>
        <jsp:param name="script" value="register.js"/>
        <jsp:param name="prefix" value="../"/>
    </jsp:include>
</head>
<body>
<jsp:include page="../partials/header.jsp">
    <jsp:param name="prefix" value="../"/>

</jsp:include>
<div class="container grid-y">
    <div class="grid-x w40">
        <div class="grid-y w100">
            <form method="post" name="modulo" action="./register" onsubmit="return testData(this)">
                <h2 >Registrati</h2>
                <label for="user">
                    Username:
                    <c:if test="${errUsername != null}">
                       <div class="invalidSimple">${errUsername}</div>
                    </c:if>
                    <input type="text" id="user"name="username" size="28" value="${username}" required >
                </label><br>


                <label for="email" >
                    Email:
                    <c:if test="${errEmail != null}">
                        <div class="invalidSimple">${errEmail}</div>
                    </c:if>
                    <input type="email" id="email" name="email" size="28" value="${email}" required>
                </label><br>
                <label for="pwd">
                    Password:<img src="../icons/eye.svg" onclick="showPwd()"><br>
                    <c:if test="${errPasswd != null}">
                        <div class="invalidSimple"> ${errPasswd}</div>
                    </c:if>
                    <input type="password" id="pwd" name="passwd" size="28" value="${passwd}" required>

                </label><br>
                <label for="pwd2">
                    Conferma Password:
                    <c:if test="${errPasswdNE != null}">
                        <div class="invalidSimple">${errPasswdNE}</div>
                    </c:if>
                    <input type="password" id="pwd2" name="passwdCheck" size="28" value="${passwdCheck}" required >
                </label><br>

                <button class="btn primary" value="submit" >Registrati</button>
            </form>
        </div>
        <div class="text-small">Hai già un account?<a href="login">Accedi!</a></div>
        <div class="grid-y justify-center">
            <div id="message">
                <div class="titolo">La password deve contenere:</div>
                <p id="letter" class="invalid"> Lettera minuscola</p>
                <p id="capital" class="invalid">Lettera maiuscola</p>
                <p id="number" class="invalid"> Un numero</p>
                <p id="length" class="invalid">minimo 8 caratteri</p>
            </div>
        </div>

        <div id="dettaglio">
            La password deve contenere almeno un numero, una lettera maiuscola, una minuscola e almeno 8 caratteri.
        </div>
    </div>
</div>
</body>
</html>