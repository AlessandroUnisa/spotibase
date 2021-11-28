package model.Utente;

public abstract class UtenteQuery {
    public static String getQueryFetchPrefPlaylistAttByUsername(){
        return "SELECT ALB.*, CAN.*, ART.*, PLA.*, ATT.* FROM " +
                " utente UTE LEFT JOIN seguire SEG ON UTE.username=SEG.usernameUtente " +
                " LEFT JOIN artista ART ON SEG.codFiscale=ART.codFiscale " +
                " LEFT JOIN preferenza PRE ON UTE.username=PRE.usernameUtente " +
                " LEFT JOIN canzone CAN ON PRE.codiceCanzone=CAN.codice " +
                " LEFT JOIN interesse INTE ON INTE.usernameUtente=UTE.username " +
                " LEFT JOIN album ALB ON INTE.codiceAlbum=ALB.codice " +
                " LEFT JOIN playlist PLA ON UTE.username=PLA.username " +
                " LEFT JOIN attivazione ATT ON ATT.usernameUtente=UTE.username " +
                " WHERE UTE.username=?;";
    }

    public static String getQueryUtenteSave(){
        return "INSERT INTO utente (username,passwd,email) VALUES(?,?,?);";
    }

    public static String getQueryUtenteUpdate(){
        return "UPDATE utente SET passwd=?, email=?, WHERE username=?";
    }

    public static String getQueryFindAccount(){
        return "SELECT * FROM utente UTE WHERE UTE.email=? AND UTE.passwd=?;";
    }

    public static String getQueryFindAccountField(String field){
        return "SELECT * FROM utente UTE WHERE UTE."+field+"=?";
    }


}
