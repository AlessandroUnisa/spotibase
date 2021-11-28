<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<ul class="grid-inline paginator justify-center noDisplay" id="${param.id}">
    <c:forEach var="page" begin="1" end="${param.pages}">
                <li>
                   <a onclick="loadJSONDoc(${param.numCard},${page})">${page}</a>
               </li>
    </c:forEach>
</ul>