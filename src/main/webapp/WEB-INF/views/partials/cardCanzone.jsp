<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page  pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" language="java" %>

<c:set var="flag" value="flase"></c:set>
<c:forEach items="${requestScope.listaPreferiti}" var="codice">
    <c:if test="${param.codiceCanzone == codice}">
        <c:set var="flag" value="true"></c:set>
    </c:if>
</c:forEach>

<c:set value="flagAcquistata" var="false"></c:set>
<c:forEach items="${requestScope.listaCanzoniAcquistate}" var="codice">
    <c:if test="${param.codiceCanzone == codice}">
        <c:set var="flagAcquistata" value="true"></c:set>
    </c:if>
</c:forEach>

<div class="containerCanzone" >
    <img src="${param.srcImg}" class="${param.classImg} ">
    <div class="bottom-left">
        <button class="controlBtnHome play ${param.codiceCanzone}" id="${param.codiceCanzone}" onclick="play('${param.codiceCanzone}',this)"><i class="fas fa-play" aria-hidden="true"></i></button>

        <c:choose>
            <c:when test="${!sessionScope.isLogged}">
                <button class="controlBtnHome prefButton${param.codiceCanzone}" onclick="alert('Effettua il login per esprimere delle preferenze')"><i class="far invalidSimple fa-heart" aria-hidden="true"></i></button>
            </c:when>
            <c:otherwise>
                <c:choose>
                    <c:when test="${flag}">
                        <button class="controlBtnHome prefButton${param.codiceCanzone}"  onclick="preferenza('${param.codiceCanzone}',1)" ><i class="fas invalidSimple fa-heart" aria-hidden="true"></i></button>
                    </c:when>
                    <c:otherwise>
                        <button class="controlBtnHome prefButton${param.codiceCanzone}" onclick="preferenza('${param.codiceCanzone}',1)" ><i class="far invalidSimple fa-heart" aria-hidden="true"></i></button>
                    </c:otherwise>
                </c:choose>
            </c:otherwise>
        </c:choose>

        <c:choose>
            <c:when test="${flagAcquistata}">
                <a href="${param.url}" download="${param.titolo}"><button class="controlBtnHome"><i class="fas fa-arrow-down"></i></button></a>
            </c:when>
            <c:otherwise>
                <button class="controlBtnHome" onclick="cart('${param.codiceCanzone}')"><i class="fas fa-shopping-cart" aria-hidden="true"></i></button>
            </c:otherwise>
        </c:choose>
         </div>
    <div class="centro">
        ${param.titolo}
    </div>
    <div class="centro">
        ${param.artista}
    </div>

</div>
