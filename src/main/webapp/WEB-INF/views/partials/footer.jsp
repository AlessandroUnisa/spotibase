
<footer>
    <div><b>Titolo:</b> | <b>Artista:</b></div>
        <button class="btnControl" id="prefFooter" onclick="alert('Effettua il login per esprimere preferenze')">
            <i class="far fa-heart invalidSimple" aria-hidden="true"></i>
        </button>
    <c:choose>
        <c:when test="${sessionScope.isLogged}">
            <a href="libreria"><button class="btnControl" title="Vai nella libreria"><i class="fas fa-info" aria-hidden="true"></i></button></a>
        </c:when>
        <c:otherwise>
            <a href="./account/register"><button class="btnControl" title="Vai nella libreria"><i class="fas fa-info" aria-hidden="true"></i></button></a>
        </c:otherwise>
    </c:choose>

        <button class="btnControl" title="Aggiungi a playlist">
            <i class="fas fa-plus" aria-hidden="true">
                <c:if test="${sessionScope.isLogged && listPlaylist.size() > 0}">
                    <span class="contentPlaylist">
                        <c:forEach items="${listPlaylist}" var="playlist">
                            <p onclick="insertPlaylist('${playlist.titolo}')">${playlist.titolo}</p>
                        </c:forEach>
                    </span>
                    <span class="trianglePlaylist"></span>
                </c:if>
            </i>
        </button>

    <audio id="player" controls controlsList="nodownload">
        <source src="" type="audio/mpeg">
        Your browser does not support the audio element.
    </audio>

    <div id="darkModeComputer">
        <div class="dntoggle" onclick="setDarkMode()">
            <i class="far fa-moon"></i>
        </div>
    </div>
</footer>

