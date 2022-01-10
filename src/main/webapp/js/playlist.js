function checkNote(){
    var regex = /^[a-zA-Z0-9_.-. ]{0,100}\w|^$$/;
    var text = document.getElementById("note").value;
    var flag = regex.test(text);
    if(flag==true){
        document.getElementById("errorNote").setAttribute("style","opacity=0;");
        return true;
    }else{
        document.getElementById("errorNote").setAttribute("style","opacity=1;");
        return false;
    }
}

function checkTitolo(){
    var regex = /^[a-zA-Z0-9_.-. ]{0,100}\w$/;
    var text = document.getElementById("titolo").value;
    var flag = regex.test(text);
    if(flag==true){
        document.getElementById("errorTitolo").setAttribute("style","opacity=0;");
        return true;
    }else{
        document.getElementById("errorTitolo").setAttribute("style","opacity=1;");
        return false;
    }
}