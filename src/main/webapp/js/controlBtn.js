function insertPlaylist(titoloPlaylist){  //inserisce una canzone nella playlist
    var codice = getCodiceCanzone($('source').attr('src'));  //prendo il cod della canzone
        $.getJSON("./jsonPlaylistServlet?nomePlay=" + titoloPlaylist + "&codCan=" + codice, function (result) {
            if (result.flag == "isPresent")
                alert('La canzone è già presente nella playlist')
            else if(result.flag)
                alert("Canzone inserita correttamente");
            else alert("Si è verificato un errore. Riprova")
        })
}


function cart(codice){//inserisce un elemento nel carrello
    $.getJSON("jsonCarrelloServlet?cod="+codice, function (result){
        if(result.flag){
            $('.badgeCart').text(result.numElements); //aggiorno il numero di elem nel carrello
        }else alert('Elemento gia presente nel carrello');
    })
}

function preferenza(codice, tipo){ //setta la preferenza per l elemento tipo 1-->canzone, 2-->artista(nomeDArte), 3-->album
    $.getJSON("./jsonPreferitiServlet?cod="+codice+"&tipo="+tipo,function (result){
        if(result.flag==true){
            if(getCodiceCanzone($('source').attr('src'))==codice)  //controllo se devo modificare l icona nel footer
              $('#prefFooter .fa-heart').toggleClass("fas far");
            var id = ".prefButton"+codice+" .fa-heart";  //modifico il button dell elemento
            $(id).toggleClass("fas far");
        }
    })
}

function getCodiceCanzone(srcContent){//riceve il src di audio e ritorna il codice della canzone
    if(srcContent.substring(31,32)!="_")
        return srcContent.substring(28,32);
    else return srcContent.substring(28,31);
}

document.getElementById("player").addEventListener("play", function (){//catturo l evento play di audio
    let codiceCanzone = getCodiceCanzone(document.getElementsByTagName("source")[0].getAttribute("src")); //prendo il codice canzone dal src di source
    let classe = "." + codiceCanzone;
    $(classe).children("i").toggleClass("fa-play fa-pause");  //icona del button con id = codiceCanzone
    $(classe).removeAttr("onclick");  //rimuovo l onclik per settare la funzione stop
    $(classe).attr("onclick","stop('"+codiceCanzone+"',this)");
})
document.getElementById("player").addEventListener("pause", function (){//catturo l evento pause di audio
    let codiceCanzone = getCodiceCanzone(document.getElementsByTagName("source")[0].getAttribute("src"));
    let classe =  "." + codiceCanzone;
    $(classe).children("i").toggleClass("fa-play fa-pause");
    $(classe).removeAttr("onclick");
    $(classe).attr("onclick","play('"+codiceCanzone+"',this)");
})


function play(codice, element){
    $('.fa-pause').attr('class','fas fa-play'); //cambio l'icona del tasto che rappresenta la riproduzione precedente

   var elements = document.getElementsByClassName('play');//metto onclick=play a tutti i button (altrimenti i precedenti rimangono a stop)
   for (var x=0; x<elements.length; x++){
       var clickPrev = elements[x].getAttribute('onclick');
       elements[x].setAttribute('onclick',clickPrev.replace('stop','play'))
   }


    $.getJSON("./jsonCanzoneServlet?cod="+codice,function (result){            //prendo l'url di quel codice
        $('source').attr("src",result.url);         //lo setto nel src
        $('audio').trigger("load");                                   //invoco load per caricare la nuova canzone
        $('audio').trigger("play");                                     //faccio partire il player
        $(element).removeAttr("onclick");                              //rimuovo l'onclick play, e setto l'onclick stop
        $(element).attr("onclick","stop('"+codice+"',this)");
        $('footer div').eq(0).html("<b>Titolo:</b>"+result.titolo+" | <b>Artista:</b>"+result.artista);  //setto titolo e artista nel footer



        if(result.isLogged) { // se l utente è loggato sblocco la preferenza
            $('#prefFooter').attr('onclick', 'preferenza("' + codice + '",1)');
            $('#prefFooter .fa-heart').removeClass('far');
            $('#prefFooter .fa-heart').removeClass('fas');
            if (result.pref)  //se l elemento si trova nei preferiti
                $('#prefFooter .fa-heart').addClass('fas');
            else $('#prefFooter .fa-heart').addClass('far');
        }
    });
}

function stop(codice, element){
    document.getElementById("player").pause();               //stoppo la riproduzione
    document.getElementById("player").currentTime=0;         //setto la barra di riproduzione a 0
    $(element).attr("onclick","play('"+codice+"',this)");     //setto il click play
}

function cartAbbonamento(nome){ //acquista l abbonamento
  $.getJSON("./jsonAbbonamentoServlet?nome="+nome, function (result){
      if(result.flag)
          alert("Abbonamento acquistato correttamente");
      else alert("Si è verificato un errore. Riprova")
  })
}