<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width,initial-scale=1,viewport-fit=cover">
<title>${param.title}</title>
<meta name="description" content="Piattaforma musicale">

<link rel="icon" type="image/png" href="${param.prefix}image/logo5.png">
<link rel="apple-touch-icon" href="${param.prefix}images/logo5.png">
<link rel="apple-touch-startup-image" href="${param.prefix}images/logo5.png">
<link rel="stylesheet" href="${param.prefix}css/library.css">
<link rel="stylesheet" href="${param.prefix}css/effects.css">
<c:if test="${param.controlBtn}">
    <script src="${param.prefix}js/controlBtn.js" defer></script>
</c:if>
<c:if test="${empty param.darkMode}">
    <script src="${param.prefix}js/darkMode.js" defer></script>
</c:if>
<c:if test="${!param.isDashboard}">
    <link rel="stylesheet" href="${param.prefix}css/header.css">
</c:if>

<c:if test="${param.footer}">
    <link rel="stylesheet" href="${param.prefix}css/footer.css">
</c:if>
<script src="${param.prefix}js/library.js" defer></script>
<c:if test="${not empty param.style}">
    <link rel="stylesheet" href="${param.prefix}css/${param.style}">
</c:if>
<c:if test="${not empty param.script}">
    <script src="${param.prefix}js/${param.script}" defer></script>
</c:if>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js" defer></script>
<link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.15.3/css/all.css" integrity="sha384-SZXxX4whJ79/gErwcOYf+zWLeJdY/qpuqC4cAa9rOGUstPomtqpuNWT9wdPEn2fk" crossorigin="anonymous">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="format-detection" content="telephone-no">
<meta name="apple-mobile-web-app-title" content="Spotibase">
<meta name="apple-mobile-web-app-status-bar-style" content="default">
<meta name="theme-color" content="#000000">
