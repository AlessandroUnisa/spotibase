<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div id="mySidenav" class="sidenav">
        <div id="darkModeTendina">
           <span onclick="setDarkMode()">
              <i class="far fa-moon"></i>
           </span>
        </div>
        <a href="javascript:void(0)" class="closebtn" onclick="closeNav()" id="menu">&times;</a>
        <div class="tendina">
            <a href="${param.prefix}index.html">Home</a>
            <a href="${param.prefix}abbonamenti">Abbonati</a>
            <c:choose>
                <c:when test="${sessionScope.isLogged}">
                    <a href="./libreria">Libreria</a>
                </c:when>
                <c:otherwise>
                    <a href="${param.prefix}register">Libreria</a>
                </c:otherwise>
            </c:choose>

        </div>
    </div>

    <header>
        <ul class="computer">
            <li id="logo"><img src="${param.prefix}image/logo4.png"></li>
            <li><a class="active" href="${param.prefix}index.html">Home</a></li>
            <li><a href="${param.prefix}abbonamenti">Abbonamenti</a></li>
            <li>
                <c:choose>
                    <c:when test="${sessionScope.isLogged}">
                        <a href="./libreria">Libreria</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${param.prefix}register">Libreria</a>
                    </c:otherwise>
                </c:choose>
            </li>
            <div class="user" >
                <img src="${param.prefix}image/user.png">
                <span class="badgeCart">${sessionScope.listCart.size()}</span>
                <div class="triangle"></div>
                <div class="popupUser">
                    <c:choose>
                        <c:when test="${sessionScope.isLogged}">
                            <div class="popupUserOption"><a href="${param.prefix}logout">Logout</a></div>
                        </c:when>
                        <c:otherwise>
                            <div class="popupUserOption"><a href="${param.prefix}login" >Login</a></div>
                            <div class="popupUserOption"><a href="${param.prefix}register">Registati</a></div>
                        </c:otherwise>
                    </c:choose>
                    <div class="popupUserOption"><a href="${param.prefix}carrello">Carrello</a></div>
                </div>
            </div>
            <c:if test="${not empty sessionScope.username}">
                <div class="popupUser ticketBenvenuto">Ciao ${sessionScope.username}</div>
            </c:if>
            <li class="liForm">
                <form action="./ServletSearch" method="post">
                    <input type="search" name="search">
                    <button value="submit"><i class="fa fa-search" ></i></button>
                </form>
            </li>
        </ul>

        <span class="main">
            <span id="hamburger" onclick="openNav()">&#9776;</span>
            <div class="user" >
                <img src="${param.prefix}image/user.png" >
                <span class="badgeCart">${sessionScope.listCart.size()}</span>
                <div class="triangle"></div>
                <div class="popupUser">

                   <c:choose>
                       <c:when test="${sessionScope.isLogged}">
                           <div class="popupUserOption"><a href="${param.prefix}logout">Logout</a></div>
                       </c:when>
                       <c:otherwise>
                           <div class="popupUserOption"><a href="${param.prefix}login" >Login</a></div>
                           <div class="popupUserOption"><a href="${param.prefix}register">Registati</a></div>
                       </c:otherwise>
                   </c:choose>
                    <div class="popupUserOption"><a href="${param.prefix}carrello">Carrello</a></div>
                </div>
            </div>
            <c:if test="${not empty sessionScope.username}">
                <div class="popupUser ticketBenvenuto">Ciao ${sessionScope.username}</div>
            </c:if>
                <span id="nonen">
                    <li class="liForm" >
                        <form action="./ServletSearch" method="post">
                            <input type="search" name="search">
                             <button value="submit"><i class="fa fa-search" ></i></button>
                        </form>
                    </li>
                </span>
        </span>
    </header>

    <script>
        function openNav() {
            document.getElementById("nonen").style.display="none";
            document.getElementById("mySidenav").style.width = "200px";
        }

        function closeNav() {
            document.getElementById("nonen").style.display="inline";
            document.getElementById("mySidenav").style.width = "0";
        }
    </script>

