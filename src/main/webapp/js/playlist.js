
function checkPlay(){
    return checkNote() && checkTitolo() && checkTitoloPresente();
}

function checkNote(){
    //let regex = /^[a-zA-Z0-9_.-. ]{0,100}\w|^$$/;
    let regex = /^[a-zA-Z0-9_.-. \n]{0,100}\w|^$$/;
    let text = document.getElementById("note").value;
    let flag = regex.test(text);
    if(flag==true){
        document.getElementById("errorNote").style.display = "none";
        return true;
    }else{
        document.getElementById("errorNote").style.display ="inline";
        return false;
    }
}

function checkTitolo(){
    let regex = /^[a-zA-Z0-9_.-. ]{0,100}\w$/;
    let text = document.getElementById("titolo").value;
    let flag = regex.test(text);
    if(flag==true){
        document.getElementById("errorTitolo").style.display ="none";
        return true;
    }else{
        document.getElementById("errorTitolo").style.display ="inline";
        return false;
    }
}

function checkTitoloPresente(){
    let titolo = document.getElementById("titolo").value;
   /* console.log(titolo)
    let xyz = 0;
    $.getJSON("jsonPlaylistServlet?cod=check&titolo="+titolo, function (result){
        console.log(result)
        if(result.checkT == true){
            document.getElementById('errorTitoloPresente').style.display = 'inline';
            console.log("false----")

        }else{
            document.getElementById('errorTitoloPresente').style.display = 'none';
            console.log("true----")
            return 8;
        }
    })*/

    var xyz = 0;
    console.log(titolo)
    $.ajax({
        async:false, url:"jsonPlaylistServlet?cod=check&titolo="+titolo, success: function(result){
            if(result.checkT == true){
                document.getElementById('errorTitoloPresente').style.display = 'inline';
                console.log("false----")
                xyz = 0
            }else{
                document.getElementById('errorTitoloPresente').style.display = 'none';
                console.log("true----")
                xyz = 1;
            }
        }
    });
    return xyz==1;
}