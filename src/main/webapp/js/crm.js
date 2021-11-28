function loadJSONDoc(cod, page) { //mostra la tabella elementi
    $.getJSON("../JsonAdmin?cod="+cod+"&page="+page, function (result){
        $("ul li a").css("color","#6d6a6a");   //setto i colori del paginator
        $( "ul li:nth-child("+page+") a").css("color","#3DA2A6");
        switch (cod){
            case 1: //album
                $('#idCan').addClass('noDisplay');  //disabilito e abilito i paginator giusti
                $('#idArt').addClass('noDisplay');
                $('#idAlb').removeClass('noDisplay');
                $('#tableMOD').empty();  //svuoto la tabella
                $('#tableMOD').append("<tr><th>Codice</th><th>Nome</th><th>Anno</th><th>Descrizione</th><th>numCanzoni</th></tr>"); //setto l intestazione
                $.each(result, function (index, field){ //ciclo i dati ritornati dal server
                    var strObj = "<tr><td>"+field['codice']+"</td>"+
                                 "<td>"+field['nome']+"</td>"+
                                 "<td>"+field['anno']+"</td>"+
                                 "<td>"+field['descrizione']+"</td>"+
                                 "<td>"+field['numCanzoni']+"</td>"+
                                 "<td><img src='../icons/edit.svg' onclick='modify("+index+","+cod+")'></td></tr>";;
                    $('#tableMOD').append(strObj);
                })
                break;
            case 2:  //canzone
                $('#idCan').removeClass('noDisplay');
                $('#idArt').addClass('noDisplay');
                $('#idAlb').addClass('noDisplay');
                $('#tableMOD').empty();
                $('#tableMOD').append("<tr><th>Codice</th><th>Titolo</th><th>Durata</th><th>Anno</th><th>Prezzo</th></tr>");
                $.each(result, function (index, field){
                    var strObj = "<tr><td>"+field['codice']+"</td>"+
                        "<td>"+field['titolo']+"</td>"+
                        "<td>"+field['durata']+"</td>"+
                        "<td>"+field['anno']+"</td>"+
                        "<td>"+field['prezzo']+"</td>"+
                        "<td><img src='../icons/edit.svg' onclick='modify("+index+","+cod+")'></td></tr>";;
                    $('#tableMOD').append(strObj);
                })
                break;
            case 3: //artista
                $('#idCan').addClass('noDisplay');
                $('#idArt').removeClass('noDisplay');
                $('#idAlb').addClass('noDisplay');
                $('#tableMOD').empty();
                $('#tableMOD').append("<tr><th>CodFiscale</th><th>Nome</th><th>Cognome</th><th>nome d'arte</th></tr>");
                $.each(result, function (index, field){
                    var strObj = "<tr><td>"+field['codFiscale']+"</td>"+
                        "<td>"+field['nome']+"</td>"+
                        "<td>"+field['cognome']+"</td>"+
                        "<td>"+field['nomeDArte']+"</td>" +
                        "<td><img src='../icons/edit.svg' onclick='modify("+index+","+cod+")'></td></tr>";
                    $('#tableMOD').append(strObj);
                })
                break;
        }
    })
}



function modify(numRow, codElem){ //modifica elemento
    numRow++;
    var row = document.getElementsByTagName('tr').item(numRow).children; //prendo la riga della tabella
    switch (codElem) {
        case 1://album
            $('.modal-content h4').empty(); //svuoto il popup
            $('.modal-content form').empty();
            $('.modal-content h4').append('Modifica Album'); //setto l intestazione
            var strForm = "<label for='codice'>Codice</label><br>"+ //riempio il form
                "<input type='text' id='codice' name='codice' value='"+row[0].textContent.replace(/'/g,"`")+"' readonly/><br>"+
                "<label for='nome'>Nome</label><br>"+
                '<input type="text" id="nome" name="nome" value="'+row[1].textContent.replace(/'/g,"`")+'"/><br>'+
                "<label for='anno'>Anno</label><br>"+
                "<input type='number' id='anno' name='anno' value='"+row[2].textContent.replace(/'/g,"`")+"'/><br>"+
                "<label for='descrizione'>Descrizione</label><br>"+
                "<textarea rows='3' id='descrizione' name='descrizione'>"+row[3].textContent+"</textarea><br>"+
                "<label for='numCanzoni'>Numero Canzoni</label><br>"+
                "<input type='number' id='numCanzoni' name='numCanzoni' value='"+row[4].textContent.replace(/'/g,"`")+"'/><br>";
            $('.modal-content form').append(strForm);
            $('.modal-content form').append('<button value="submit" class="primary btn">Modifica</button>');
            $('.modal').css('display','block'); //mostro il popup
        break;

        case 2://canzone
            $('.modal-content h4').empty();
            $('.modal-content form').empty();
            $('.modal-content h4').append('Modifica Canzoni');
            var strForm = "<label for='codice'>Codice</label><br>"+
                "<input type='text' id='codice' name='codice' value='"+row[0].textContent.replace(/'/g,"`")+"' readonly/><br>"+
                "<label for='titolo'>Titolo</label><br>"+
                '<input type="text" id="titolo" name="titolo" value="'+row[1].textContent.replace(/'/g,"`")+'"/><br>'+
                "<label for='durata'>Durata</label><br>"+
                "<input type='number' id='durata' name='durata' value='"+row[2].textContent.replace(/'/g,"`")+"'/><br>"+
                "<label for='anno'>Anno</label><br>"+
                "<input type='number' id='anno' name='anno' value='"+row[3].textContent.replace(/'/g,"`")+"'/><br>"+
                "<label for='prezzo'>Prezzo</label><br>"+
                "<input type='number' id='prezzo' name='prezzo' value='"+row[4].textContent.replace(/'/g,"`")+"'/><br>";
            $('.modal-content form').append(strForm);
            $('.modal-content form').append('<button type="submit" class="primary btn">Modifica</button>');
            $('.modal').css('display','block');
            break;

        case 3://artista
            $('.modal-content h4').empty();
            $('.modal-content form').empty();
            $('.modal-content h4').append('Modifica Artisti');
            var strForm = "<label for='codice'>Codice Fiscale</label><br>"+
                "<input type='text' id='codice' name='codice' value='"+row[0].textContent.replace(/'/g,"`")+"' /><br>"+
                "<label for='nome'>Nome</label><br>"+
                '<input type="text" id="nome" name="nome" value="'+row[1].textContent.replace(/'/g,"`")+'"/><br>'+
                "<label for='cognome'>Cognome</label><br>"+
                '<input type="text" id="cognome" name="cognome" value="'+row[2].textContent.replace(/'/g,"`")+'"/><br>'+
                "<label for='nomeDArte'>Nome d'Arte</label><br>"+
                '<input type="text" id="nomeDArte" name="nomeDArte" value="'+row[3].textContent.replace(/'/g,"`")+'"/><br>';
            $('.modal-content form').append(strForm);
            $('.modal-content form').append('<button type="submit" class="primary btn">Modifica</button>');
            $('.modal').css('display','block');
            break;
    }
}


