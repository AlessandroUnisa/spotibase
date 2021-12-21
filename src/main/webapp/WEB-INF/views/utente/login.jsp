<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="utf-8" language="java"%>
<!DOCTYPE html>
<html>
<head>
   <jsp:include page="../partials/head.jsp">
       <jsp:param name="title" value="Spotibase | Login"/>
       <jsp:param name="style" value="loginRegister.css"/>
       <jsp:param name="script" value="login.js"/>
       <jsp:param name="prefix" value="./"/>
   </jsp:include>
</head>
<body>
<jsp:include page="../partials/header.jsp">
    <jsp:param name="prefix" value="./"/>
</jsp:include>
<div class=" container grid-y">
    <h2>Login</h2>
    <c:if test="${errCredenziali != null}">
        <div class="invalidSimple">Credenziali errate. Riprovare.</div>
    </c:if>
    <div class="grid-x w40">
        <div class="grid-y w50">
            <form method="post">
                <label for="email">
                    *Email:<br>
                    <input type="email" id="email" name="email" required><br>
                </label>

                <label for="pwd">
                    *Password:<img src="./icons/eye.svg" onclick="showPwd()"><br>
                    <input type="password" id="pwd" name="passwd" required ><br>
                </label>
                <button class="btn primary" value="submit" >Accedi</button>
            </form>
        </div>
        <p class="text-small">Non hai un account?<a href="./register">Registati!</a></p>
        <p id="dettaglio">Il simbolo "*" posto davanti ai campi ne indica l'obbligo di compilazione.</p>

    </div>
</div>
</body>

</html>