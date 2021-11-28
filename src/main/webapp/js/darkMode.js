var modeDark =  localStorage.getItem("color");
if(modeDark != null && modeDark == "light"){
    document.body.classList.toggle("dark-mode");
    var element = document.getElementsByClassName("far fa-moon"); //cambio le icone sole luna
    element[0].classList.toggle("fa-sun");
    element[1].classList.toggle("fa-sun");
}

function setDarkMode() {
    var modeDark =  localStorage.getItem("color");
    if(modeDark == null){
        var element = document.getElementsByClassName("far fa-moon");
        element[0].classList.toggle("fa-sun");  //per il primo click
        element[1].classList.toggle("fa-sun");
        localStorage.setItem("color", "light");
        document.body.classList.toggle("dark-mode");  //dark mode setta i colori bianchi
    }else if(modeDark == "dark"){
        var element = document.getElementsByClassName("far fa-moon");
        element[0].classList.toggle("fa-sun");
        element[1].classList.toggle("fa-sun");
        localStorage.setItem("color", "light");
        document.body.classList.toggle("dark-mode");
    }else if(modeDark == "light"){
        var element = document.getElementsByClassName("far fa-moon");
        element[0].classList.toggle("fa-sun");
        element[1].classList.toggle("fa-sun");
        localStorage.setItem("color", "dark");
        document.body.classList.toggle("dark-mode");
    }
}