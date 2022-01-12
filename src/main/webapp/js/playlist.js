
function check(){
    return checkNote() && checkTitolo();
}

function checkNote(){
    //let regex = /^[a-zA-Z0-9_.-. ]{0,100}\w|^$$/;
    let regex = /^[a-zA-Z0-9_.-. \n]{0,100}\w|^$$/;
    let text = document.getElementById("note").value;
    let flag = regex.test(text);
    if(flag==true){
        document.getElementById("errorNote").style.opacity ="0";
        return true;
    }else{
        document.getElementById("errorNote").style.opacity ="1";
        return false;
    }
}

function checkTitolo(){
    let regex = /^[a-zA-Z0-9_.-. ]{0,100}\w$/;
    let text = document.getElementById("titolo").value;
    let flag = regex.test(text);
    if(flag==true){
        document.getElementById("errorTitolo").style.opacity ="0";
        return true;
    }else{
        document.getElementById("errorTitolo").style.opacity ="1";
        return false;
    }
}