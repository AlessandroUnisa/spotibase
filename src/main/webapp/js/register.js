function showPwd() {
    var input = document.getElementById('pwd');
    var input2 = document.getElementById('pwd2');

    if (input.type == "password") {
        input.type = "text";
        input2.type = "text";
    } else {
        input.type = "password";
        input2.type = "password";
    }
}

function testData(modulo){
    let flag = true;
   if(!/(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,20}/.test(modulo.passwd.value)){
       document.getElementById("message").style.display = "block";
       alert("Password non valida");
       flag = false;
   }

   if(modulo.passwd.value != modulo.passwdCheck.value){
        alert("Le password non coincidono");
        return false
   }

   return flag && check();
}

    var myInput = document.getElementById("pwd");
    var letter = document.getElementById("letter");
    var capital = document.getElementById("capital");
    var number = document.getElementById("number");
    var length = document.getElementById("length");

    //Quando l'utente fa clic sul campo della password, mostra la finestra del messaggio
    myInput.onfocus = function() {
        document.getElementById("message").style.display = "block";
    }

    // Quando l'utente fa clic al di fuori del campo della password, nasconde la finestra del messaggio
    myInput.onblur = function() {
        document.getElementById("message").style.display = "none";
    }

    // Quando l'utente inizia a digitare qualcosa all'interno del campo della password
    myInput.onkeyup = function() {
        //lettere minuscole
        var lowerCaseLetters = /[a-z]/g;
        if(myInput.value.match(lowerCaseLetters)) {
            letter.classList.remove("invalid");
            letter.classList.add("valid");
        } else {
            letter.classList.remove("valid");
            letter.classList.add("invalid");
        }

        // convalida lettere maiuscole
        var upperCaseLetters = /[A-Z]/g;
        if(myInput.value.match(upperCaseLetters)) {
            capital.classList.remove("invalid");
            capital.classList.add("valid");
        } else {
            capital.classList.remove("valid");
            capital.classList.add("invalid");
        }


        var numbers = /[0-9]/g;
        if(myInput.value.match(numbers)) {
            number.classList.remove("invalid");
            number.classList.add("valid");
        } else {
            number.classList.remove("valid");
            number.classList.add("invalid");
        }

        // convalida la lunghezza
        if(myInput.value.length >= 8 && myInput.value.length <= 20) {
            length.classList.remove("invalid");
            length.classList.add("valid");
        }else {
            length.classList.remove("valid");
            length.classList.add("invalid");
        }
    }

    function check(){
        let booleanoUser;
        console.log(document.getElementById("user").value);
        if(/^[a-zA-Z0-9\-_]{1,40}$/.test(document.getElementById("user").value)==true){
            document.getElementById("errorUser").setAttribute("style","visibility=hidden;");
            console.log("true username")
            booleanoUser = true;
        }else{
            document.getElementById("errorUser").setAttribute("style","visibility=visible;");
            console.log("false username")
            booleanoUser = false;
        }

        let booleanoMail;
        console.log(document.getElementById("email").value);
        if(/^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]{1,63}@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/.test(document.getElementById("email").value)==true){
            document.getElementById("errorMail").setAttribute("style","visibility=hidden;");
            console.log("true email")
            booleanoMail = true;
        }else{
            document.getElementById("errorMail").setAttribute("style","visibility=visible;");
            console.log("false email")
            booleanoMail = false;
        }

        return booleanoUser && booleanoMail;
    }

  /*  function checkUsername(){
        const regex = /^[a-zA-Z0-9\-_]{1,40}$/;
        let text = document.getElementById("user").value;
        let flag = regex.test(text);
        if(flag==true){
            document.getElementById("errorUser").setAttribute("style","visibility=hidden;");
            return true;
        }else{
            document.getElementById("errorUser").setAttribute("style","visibility=visible;");
            return false;
        }
    }
*/
    /*function checkMail(){
        let regex = /^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$/;
        let text = document.getElementById("email").value;
        let flag = regex.test(text);
        if(flag==true){
            document.getElementById("errorMail").setAttribute("style","visibility=hidden;");
            return true;
        }else{
            document.getElementById("errorMail").setAttribute("style","visibility=visible;");
            return false;
        }
    }*/

